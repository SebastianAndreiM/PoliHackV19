package com.assetguard.domain.usecase.ai

import core.model.ai.ChatMessage
import core.model.ai.IntentResponse
import core.model.ai.MessageRole
import core.model.user.UserType
import core.repository.AIGateway
import core.repository.ChatRepository
import core.usecase.ai.SendMessageUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SendMessageUseCaseTest {

    private val gateway = mockk<AIGateway>()
    private val repo    = mockk<ChatRepository>()
    private val useCase = SendMessageUseCase(gateway, repo)

    private val userId = UUID.fromString("00000000-0000-0000-0000-000000000001")

    private fun fakeMessage(role: MessageRole, content: String) = ChatMessage(
        id = UUID.randomUUID(),
        userId = userId,
        role = role,
        content = content,
        deepLink = null,
        uiHint = null,
        createdAt = LocalDateTime.now()
    )

    private fun fakeAssistantMessage(intent: IntentResponse) = ChatMessage(
        id        = UUID.randomUUID(),
        userId    = userId,
        role      = MessageRole.ASSISTANT,
        content   = intent.reply,
        deepLink  = intent.deepLink,
        uiHint    = intent.uiHint,
        createdAt = LocalDateTime.now()
    )

    @Test
    fun `send message saves user message then assistant reply`() = runTest {
        val intent = IntentResponse(
            intent     = "send_money",
            deepLink   = "/transfer",
            uiHint     = "highlight_send_button",
            reply      = "Sure! Tap Send to start a transfer.",
            confidence = 0.95
        )

        coEvery { repo.getHistory(userId, any()) } returns emptyList()
        coEvery { repo.saveMessage(match { it.role == MessageRole.USER }) } returns
                fakeMessage(MessageRole.USER, "I want to send money")
        coEvery { repo.saveMessage(match { it.role == MessageRole.ASSISTANT }) } returns
                fakeAssistantMessage(intent)
        coEvery { gateway.parseIntent(any(), any(), any()) } returns intent

        val result = useCase.execute(userId, UserType.DEFAULT, "I want to send money")

        assertEquals(MessageRole.ASSISTANT, result.role)
        assertEquals("/transfer", result.deepLink)
        assertEquals("highlight_send_button", result.uiHint)
        coVerify(exactly = 2) { repo.saveMessage(any()) }
    }

    @Test
    fun `send message passes chat history to gateway`() = runTest {
        val history = listOf(
            fakeMessage(MessageRole.USER,      "Hello"),
            fakeMessage(MessageRole.ASSISTANT, "Hi! How can I help?")
        )
        val intent = IntentResponse("greeting", null, null, "Hello!", 0.9)

        coEvery { repo.getHistory(userId, any()) } returns history
        coEvery { repo.saveMessage(any()) } returnsArgument 0
        coEvery { gateway.parseIntent(any(), any(), any()) } returns intent

        useCase.execute(userId, UserType.BUSINESS, "Send money to Maria")

        coVerify {
            gateway.parseIntent(
                message  = "Send money to Maria",
                userType = UserType.BUSINESS,
                history  = match { it.size == 2 }
            )
        }
    }

    @Test
    fun `send message with BUSINESS userType passes correct userType to gateway`() = runTest {
        val intent = IntentResponse("invoice", "/invoice/new", null, "Opening invoice.", 0.88)

        coEvery { repo.getHistory(userId, any()) } returns emptyList()
        coEvery { repo.saveMessage(any()) } returnsArgument 0
        coEvery { gateway.parseIntent(any(), any(), any()) } returns intent

        useCase.execute(userId, UserType.BUSINESS, "Create an invoice")

        coVerify { gateway.parseIntent(any(), UserType.BUSINESS, any()) }
    }

    @Test
    fun `send message returns assistant reply with deepLink`() = runTest {
        val intent = IntentResponse(
            intent     = "check_balance",
            deepLink   = "/balance",
            uiHint     = "show_balance_card",
            reply      = "Your balance is shown on the home screen.",
            confidence = 0.92
        )

        coEvery { repo.getHistory(userId, any()) } returns emptyList()
        coEvery { repo.saveMessage(match { it.role == MessageRole.USER }) } returnsArgument 0
        coEvery { repo.saveMessage(match { it.role == MessageRole.ASSISTANT }) } returns
                fakeAssistantMessage(intent)
        coEvery { gateway.parseIntent(any(), any(), any()) } returns intent

        val result = useCase.execute(userId, UserType.DEFAULT, "What's my balance?")

        assertEquals("/balance", result.deepLink)
        assertEquals("show_balance_card", result.uiHint)
    }

    @Test
    fun `send message with null deepLink still succeeds`() = runTest {
        val intent = IntentResponse("small_talk", null, null, "I'm here to help!", 0.6)

        coEvery { repo.getHistory(userId, any()) } returns emptyList()
        coEvery { repo.saveMessage(any()) } returnsArgument 0
        coEvery { gateway.parseIntent(any(), any(), any()) } returns intent

        val result = useCase.execute(userId, UserType.STUDENT, "How are you?")

        assertEquals(null, result.deepLink)
        assertNotNull(result.content)
    }
}
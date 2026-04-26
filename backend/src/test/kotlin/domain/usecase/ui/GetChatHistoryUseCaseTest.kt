package com.assetguard.domain.usecase.ui

import core.model.ai.ChatMessage
import core.model.ai.MessageRole
import core.repository.ChatRepository
import core.usecase.ai.GetChatHistoryUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test

class GetChatHistoryUseCaseTest {

    private val repo    = mockk<ChatRepository>()
    private val useCase = GetChatHistoryUseCase(repo)

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

    @Test
    fun `get history returns messages in order`() = runTest {
        val messages = listOf(
            fakeMessage(MessageRole.USER,      "Hello"),
            fakeMessage(MessageRole.ASSISTANT, "Hi! How can I help?"),
            fakeMessage(MessageRole.USER,      "Send money to Maria")
        )
        coEvery { repo.getHistory(userId, 10) } returns messages

        val result = useCase.execute(userId)

        assertEquals(3, result.size)
        assertEquals(MessageRole.USER, result.first().role)
        assertEquals(MessageRole.USER, result.last().role)
    }

    @Test
    fun `get history with custom limit passes limit to repo`() = runTest {
        coEvery { repo.getHistory(userId, 5) } returns emptyList()

        useCase.execute(userId, limit = 5)

        coVerify { repo.getHistory(userId, 5) }
    }

    @Test
    fun `get history returns empty list when no messages`() = runTest {
        coEvery { repo.getHistory(userId, any()) } returns emptyList()

        val result = useCase.execute(userId)

        assertTrue(result.isEmpty())
    }
}
package com.assetguard.presentation.routes

import core.model.ai.ChatMessage
import core.model.ai.MessageRole
import core.model.user.UserType
import core.usecase.ai.GetChatHistoryUseCase
import core.usecase.ai.SendMessageUseCase
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.route
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import presentation.plugins.configureStatusPages
import presentation.routes.aiAssistantRoutes
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class AIAssistantRoutesTest {

    private val sendMessage    = mockk<SendMessageUseCase>()
    private val getChatHistory = mockk<GetChatHistoryUseCase>()

    private val userId = UUID.fromString("00000000-0000-0000-0000-000000000001")

    private fun Application.configureTest() {
        install(ContentNegotiation) {
            json()
        }
        configureStatusPages()
    }

    private fun fakeReply(deepLink: String? = "/transfer") = ChatMessage(
        id = UUID.randomUUID(),
        userId = userId,
        role = MessageRole.ASSISTANT,
        content = "Sure! Tap Send to start a transfer.",
        deepLink = deepLink,
        uiHint = "highlight_send_button",
        createdAt = LocalDateTime.now()
    )

    // ── POST /ai/chat ────────────────────────────────────────────

    @Test
    fun `POST chat returns 200 with reply and deepLink`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { aiAssistantRoutes(sendMessage, getChatHistory) } }

        coEvery { sendMessage.execute(any(), any(), any()) } returns fakeReply()

        val response = client.post("/api/v1/ai/chat") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "userId": "$userId",
                  "userType": "DEFAULT",
                  "message": "I want to send money to Maria"
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertEquals("ASSISTANT", body["role"]?.jsonPrimitive?.content)
        assertEquals("/transfer", body["deepLink"]?.jsonPrimitive?.content)
        assertEquals("highlight_send_button", body["uiHint"]?.jsonPrimitive?.content)
    }

    @Test
    fun `POST chat with BUSINESS userType passes correct type to usecase`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { aiAssistantRoutes(sendMessage, getChatHistory) } }

        coEvery { sendMessage.execute(any(), UserType.BUSINESS, any()) } returns fakeReply("/invoice/new")

        val response = client.post("/api/v1/ai/chat") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "userId": "$userId",
                  "userType": "BUSINESS",
                  "message": "Create an invoice"
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.OK, response.status)
        coVerify { sendMessage.execute(any(), UserType.BUSINESS, "Create an invoice") }
    }

    @Test
    fun `POST chat with invalid userType falls back to DEFAULT`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { aiAssistantRoutes(sendMessage, getChatHistory) } }

        coEvery { sendMessage.execute(any(), UserType.DEFAULT, any()) } returns fakeReply()

        val response = client.post("/api/v1/ai/chat") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "userId": "$userId",
                  "userType": "UNKNOWN",
                  "message": "Hello"
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.OK, response.status)
        coVerify { sendMessage.execute(any(), UserType.DEFAULT, any()) }
    }

    @Test
    fun `POST chat with null deepLink returns null in response`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { aiAssistantRoutes(sendMessage, getChatHistory) } }

        coEvery { sendMessage.execute(any(), any(), any()) } returns fakeReply(deepLink = null)

        val response = client.post("/api/v1/ai/chat") {
            contentType(ContentType.Application.Json)
            setBody("""{"userId":"$userId","userType":"DEFAULT","message":"How are you?"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertTrue(body["deepLink"] is JsonNull || body["deepLink"] == null)
    }

    // ── GET /ai/history/{userId} ─────────────────────────────────

    @Test
    fun `GET history returns 200 with messages`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { aiAssistantRoutes(sendMessage, getChatHistory) } }

        coEvery { getChatHistory.execute(userId, any()) } returns listOf(
            ChatMessage(UUID.randomUUID(), userId, MessageRole.USER,
                "Send money", null, null, LocalDateTime.now()),
            fakeReply()
        )

        val response = client.get("/api/v1/ai/history/$userId")

        assertEquals(HttpStatusCode.OK, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertEquals(userId.toString(), body["userId"]?.jsonPrimitive?.content)
        assertEquals(2, body["messages"]?.jsonArray?.size)
    }

    @Test
    fun `GET history with limit param passes limit to usecase`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { aiAssistantRoutes(sendMessage, getChatHistory) } }

        coEvery { getChatHistory.execute(userId, 5) } returns emptyList()

        val response = client.get("/api/v1/ai/history/$userId?limit=5")

        assertEquals(HttpStatusCode.OK, response.status)
        coVerify { getChatHistory.execute(userId, 5) }
    }

    @Test
    fun `GET history returns empty messages array when no history`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { aiAssistantRoutes(sendMessage, getChatHistory) } }

        coEvery { getChatHistory.execute(userId, any()) } returns emptyList()

        val response = client.get("/api/v1/ai/history/$userId")

        assertEquals(HttpStatusCode.OK, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertTrue(body["messages"]?.jsonArray?.isEmpty() == true)
    }
}
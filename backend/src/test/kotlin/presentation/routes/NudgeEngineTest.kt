package com.assetguard.presentation.routes

import core.model.retention.Nudge
import core.model.retention.NudgeType
import core.usecase.retention.GetNudgesUseCase
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.route
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import presentation.plugins.configureStatusPages
import presentation.routes.retentionRoutes
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class RetentionRoutesTest {

    private val getNudges = mockk<GetNudgesUseCase>()
    private val userId = UUID.fromString("00000000-0000-0000-0000-000000000001")

    private fun Application.configureTest() {
        install(ContentNegotiation) { json() }
        configureStatusPages()
    }

    private fun fakeNudge(
        type: NudgeType = NudgeType.INACTIVITY,
        priority: Int = 1
    ) = Nudge(
        id = UUID.randomUUID(),
        userId = userId,
        type = type,
        title = "Test title",
        message = "Test message",
        deepLink = "/test",
        priority = priority,
        expiresAt = LocalDateTime.now().plusDays(1),
        createdAt = LocalDateTime.now()
    )

    @Test
    fun `GET nudges returns 200 with nudges list`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { retentionRoutes(getNudges) } }

        coEvery { getNudges.execute(userId) } returns listOf(
            fakeNudge(NudgeType.INACTIVITY, 1),
            fakeNudge(NudgeType.CASHBACK_PROXIMITY, 2)
        )

        val response = client.get("/api/v1/retention/nudges/$userId")

        assertEquals(HttpStatusCode.OK, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertEquals(userId.toString(), body["userId"]?.jsonPrimitive?.content)
        assertEquals(2, body["nudges"]?.jsonArray?.size)
    }

    @Test
    fun `GET nudges returns empty list when no nudges`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { retentionRoutes(getNudges) } }

        coEvery { getNudges.execute(userId) } returns emptyList()

        val response = client.get("/api/v1/retention/nudges/$userId")

        assertEquals(HttpStatusCode.OK, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertTrue(body["nudges"]?.jsonArray?.isEmpty() == true)
    }

    @Test
    fun `GET nudges response contains required fields`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { retentionRoutes(getNudges) } }

        coEvery { getNudges.execute(userId) } returns listOf(fakeNudge())

        val response = client.get("/api/v1/retention/nudges/$userId")

        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        val nudge = body["nudges"]?.jsonArray?.first()?.jsonObject!!

        assertTrue(nudge.containsKey("id"))
        assertTrue(nudge.containsKey("type"))
        assertTrue(nudge.containsKey("title"))
        assertTrue(nudge.containsKey("message"))
        assertTrue(nudge.containsKey("priority"))
        assertTrue(nudge.containsKey("createdAt"))
    }

    @Test
    fun `GET nudges returns nudges sorted by priority`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { retentionRoutes(getNudges) } }

        coEvery { getNudges.execute(userId) } returns listOf(
            fakeNudge(NudgeType.INACTIVITY, priority = 1),
            fakeNudge(NudgeType.CASHBACK_PROXIMITY, priority = 2),
            fakeNudge(NudgeType.STREAK_REWARD, priority = 3)
        )

        val response = client.get("/api/v1/retention/nudges/$userId")

        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        val priorities = body["nudges"]?.jsonArray
            ?.map { it.jsonObject["priority"]?.jsonPrimitive?.int }

        assertEquals(listOf(1, 2, 3), priorities)
    }

    @Test
    fun `GET nudges for ABANDON type has transfer deepLink`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { retentionRoutes(getNudges) } }

        coEvery { getNudges.execute(userId) } returns listOf(
            fakeNudge(NudgeType.ABANDON_RECOVERY, 1).copy(deepLink = "/transfer")
        )

        val response = client.get("/api/v1/retention/nudges/$userId")

        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        val nudge = body["nudges"]?.jsonArray?.first()?.jsonObject!!
        assertEquals("/transfer", nudge["deepLink"]?.jsonPrimitive?.content)
    }
}
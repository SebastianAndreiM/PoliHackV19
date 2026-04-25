package com.assetguard.presentation.routes

import core.model.telemetry.EventType
import core.model.telemetry.HeatmapEntry
import core.model.telemetry.HeatmapResponse
import core.model.telemetry.Session
import core.model.telemetry.TelemetryEvent
import core.usecase.telemetry.EndSessionUseCase
import core.usecase.telemetry.GetHeatmapUseCase
import core.usecase.telemetry.LogEventUseCase
import core.usecase.telemetry.StartSessionUseCase
import io.ktor.client.request.get
import io.ktor.client.request.patch
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
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import presentation.plugins.configureStatusPages
import presentation.routes.telemetryRoutes
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TelemetryRoutesTest {

    private val logEvent     = mockk<LogEventUseCase>()
    private val startSession = mockk<StartSessionUseCase>()
    private val endSession   = mockk<EndSessionUseCase>()
    private val getHeatmap   = mockk<GetHeatmapUseCase>()

    private fun fakeSession(ended: Boolean = false) = Session(
        id = UUID.fromString("00000000-0000-0000-0000-000000000010"),
        userId = UUID.fromString("00000000-0000-0000-0000-000000000001"),
        deviceInfo = "Android 14",
        startedAt = LocalDateTime.now(),
        endedAt = if (ended) LocalDateTime.now() else null
    )

    private fun fakeEvent() = TelemetryEvent(
        id = UUID.fromString("00000000-0000-0000-0000-000000000020"),
        sessionId = UUID.fromString("00000000-0000-0000-0000-000000000010"),
        userId = UUID.fromString("00000000-0000-0000-0000-000000000001"),
        eventType = EventType.TAP,
        screen = "dashboard",
        componentKey = "balance_card",
        durationMs = 150L,
        metadata = emptyMap(),
        createdAt = LocalDateTime.now()
    )

    private fun Application.configureTest() {
        install(ContentNegotiation) { json() }
        configureStatusPages()
    }

    private fun testApp(block: suspend ApplicationTestBuilder.() -> Unit) = testApplication {
        application { configureTest() }
        routing {
            route("/api/v1") {
                telemetryRoutes(logEvent, startSession, endSession, getHeatmap)
            }
        }
        block()
    }


    @Test
    fun `POST sessions returns 201 with session id`() = testApp {
        coEvery { startSession.execute(any(), any()) } returns fakeSession()

        val response = client.post("/api/v1/telemetry/sessions") {
            contentType(ContentType.Application.Json)
            setBody("""{"userId":"00000000-0000-0000-0000-000000000001","deviceInfo":"Android 14"}""")
        }

        assertEquals(HttpStatusCode.Created, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertNotNull(body["id"])
        assertEquals("00000000-0000-0000-0000-000000000010", body["id"]?.jsonPrimitive?.content)
    }

    @Test
    fun `POST sessions without deviceInfo returns 201`() = testApp {
        coEvery { startSession.execute(any(), null) } returns fakeSession()

        val response = client.post("/api/v1/telemetry/sessions") {
            contentType(ContentType.Application.Json)
            setBody("""{"userId":"00000000-0000-0000-0000-000000000001"}""")
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    // ── PATCH /sessions/{id}/end ─────────────────────────────────

    @Test
    fun `PATCH sessions end returns 200 with endedAt`() = testApp {
        val id = UUID.fromString("00000000-0000-0000-0000-000000000010")
        coEvery { endSession.execute(id) } returns fakeSession(ended = true)

        val response = client.patch("/api/v1/telemetry/sessions/$id/end")

        assertEquals(HttpStatusCode.OK, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertNotNull(body["endedAt"])
    }

    @Test
    fun `PATCH sessions end returns 404 when not found`() = testApp {
        val id = UUID.fromString("00000000-0000-0000-0000-000000000099")
        coEvery { endSession.execute(id) } returns null

        val response = client.patch("/api/v1/telemetry/sessions/$id/end")

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    // ── POST /events ─────────────────────────────────────────────

    @Test
    fun `POST events returns 201 with event`() = testApp {
        coEvery { logEvent.execute(any()) } returns fakeEvent()

        val response = client.post("/api/v1/telemetry/events") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "sessionId": "00000000-0000-0000-0000-000000000010",
                  "userId": "00000000-0000-0000-0000-000000000001",
                  "eventType": "TAP",
                  "screen": "dashboard",
                  "componentKey": "balance_card",
                  "durationMs": 150
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.Created, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertEquals("TAP", body["eventType"]?.jsonPrimitive?.content)
        assertEquals("dashboard", body["screen"]?.jsonPrimitive?.content)
    }

    @Test
    fun `POST events with invalid eventType returns 400`() = testApp {
        val response = client.post("/api/v1/telemetry/events") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "sessionId": "00000000-0000-0000-0000-000000000010",
                  "userId": "00000000-0000-0000-0000-000000000001",
                  "eventType": "INVALID",
                  "screen": "dashboard"
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `POST events without optional fields returns 201`() = testApp {
        coEvery { logEvent.execute(any()) } returns fakeEvent()

        val response = client.post("/api/v1/telemetry/events") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                  "sessionId": "00000000-0000-0000-0000-000000000010",
                  "userId": "00000000-0000-0000-0000-000000000001",
                  "eventType": "SCREEN_VIEW",
                  "screen": "home"
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    // ── GET /heatmap ─────────────────────────────────────────────

    @Test
    fun `GET heatmap returns 200 with entries`() = testApp {
        val heatmap = HeatmapResponse(
            totalEvents = 100L,
            uniqueUsers = 20L,
            entries = listOf(
                HeatmapEntry("dashboard", "balance_card", 50, 10, 5, 200.0, 0.2)
            )
        )
        coEvery { getHeatmap.execute(null) } returns heatmap

        val response = client.get("/api/v1/telemetry/heatmap")

        assertEquals(HttpStatusCode.OK, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertEquals(100L, body["totalEvents"]?.jsonPrimitive?.long)
        assertEquals(20L, body["uniqueUsers"]?.jsonPrimitive?.long)
        assertEquals(1, body["entries"]?.jsonArray?.size)
    }

    @Test
    fun `GET heatmap with screen filter passes param`() = testApp {
        val heatmap = HeatmapResponse(50L, 10L, emptyList())
        coEvery { getHeatmap.execute("dashboard") } returns heatmap

        val response = client.get("/api/v1/telemetry/heatmap?screen=dashboard")

        assertEquals(HttpStatusCode.OK, response.status)
        coVerify { getHeatmap.execute("dashboard") }
    }

    @Test
    fun `GET heatmap with empty entries returns valid response`() = testApp {
        coEvery { getHeatmap.execute(null) } returns HeatmapResponse(0L, 0L, emptyList())

        val response = client.get("/api/v1/telemetry/heatmap")

        assertEquals(HttpStatusCode.OK, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertEquals(0, body["entries"]?.jsonArray?.size)
    }
}
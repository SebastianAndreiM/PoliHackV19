package com.assetguard.presentation.routes

import core.model.user.UserProfile
import core.model.user.UserType
import core.usecase.ui.GetAdaptiveLayoutUseCase
import core.usecase.user.GetProfileUseCase
import infra.service.AdaptiveLayoutServiceImpl
import io.ktor.client.request.get
import io.ktor.client.request.header
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
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import presentation.plugins.configureStatusPages
import presentation.routes.adaptiveUiRoutes
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class AdaptiveUIRoutesTest {

    private val getProfile = mockk<GetProfileUseCase>()
    private val getLayout  = GetAdaptiveLayoutUseCase(layoutService = AdaptiveLayoutServiceImpl())

    private fun Application.configureTest() {
        install(ContentNegotiation) { json() }
        configureStatusPages()
    }

    private fun fakeProfile(userType: UserType) = UserProfile(
        id = UUID.fromString("00000000-0000-0000-0000-000000000001"),
        externalId = "ext-123",
        userType = userType,
        locale = "en",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    // ── GET /ui/layout/{userType} ────────────────────────────────

    @Test
    fun `GET layout for BUSINESS returns professional theme`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { adaptiveUiRoutes(getLayout, getProfile) } }

        val response = client.get("/api/v1/ui/layout/BUSINESS")

        assertEquals(HttpStatusCode.OK, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertEquals("professional", body["theme"]?.jsonPrimitive?.content)
        assertEquals("BUSINESS", body["userType"]?.jsonPrimitive?.content)
    }

    @Test
    fun `GET layout for STUDENT returns modern theme`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { adaptiveUiRoutes(getLayout, getProfile) } }

        val response = client.get("/api/v1/ui/layout/STUDENT")

        assertEquals(HttpStatusCode.OK, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertEquals("modern", body["theme"]?.jsonPrimitive?.content)
    }

    @Test
    fun `GET layout for SENIOR returns accessible theme`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { adaptiveUiRoutes(getLayout, getProfile) } }

        val response = client.get("/api/v1/ui/layout/SENIOR")

        assertEquals(HttpStatusCode.OK, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertEquals("accessible", body["theme"]?.jsonPrimitive?.content)
    }

    @Test
    fun `GET layout for invalid userType returns DEFAULT layout`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { adaptiveUiRoutes(getLayout, getProfile) } }

        val response = client.get("/api/v1/ui/layout/UNKNOWN_TYPE")

        assertEquals(HttpStatusCode.OK, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertEquals("DEFAULT", body["userType"]?.jsonPrimitive?.content)
    }

    @Test
    fun `GET layout response contains components array`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { adaptiveUiRoutes(getLayout, getProfile) } }

        val response = client.get("/api/v1/ui/layout/BUSINESS")

        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        val components = body["components"]?.jsonArray
        assertTrue(components != null && components.isNotEmpty())
    }

    @Test
    fun `GET layout response contains deepLinks`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { adaptiveUiRoutes(getLayout, getProfile) } }

        val response = client.get("/api/v1/ui/layout/BUSINESS")

        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        val deepLinks = body["deepLinks"]?.jsonObject
        assertTrue(deepLinks?.containsKey("primaryAction") == true)
    }

    // ── GET /ui/layout (header-based) ───────────────────────────

    @Test
    fun `GET layout with X-User-Id header returns correct layout`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { adaptiveUiRoutes(getLayout, getProfile) } }

        val id = UUID.fromString("00000000-0000-0000-0000-000000000001")
        coEvery { getProfile.execute(id) } returns fakeProfile(UserType.STUDENT)

        val response = client.get("/api/v1/ui/layout") {
            header("X-User-Id", id.toString())
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertEquals("STUDENT", body["userType"]?.jsonPrimitive?.content)
    }

    @Test
    fun `GET layout without X-User-Id header returns 400`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { adaptiveUiRoutes(getLayout, getProfile) } }

        val response = client.get("/api/v1/ui/layout")

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `GET layout with unknown user returns DEFAULT layout`() = testApplication {
        application { configureTest() }
        routing { route("/api/v1") { adaptiveUiRoutes(getLayout, getProfile) } }

        val id = UUID.fromString("00000000-0000-0000-0000-000000000099")
        coEvery { getProfile.execute(id) } returns null

        val response = client.get("/api/v1/ui/layout") {
            header("X-User-Id", id.toString())
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertEquals("DEFAULT", body["userType"]?.jsonPrimitive?.content)
    }
}
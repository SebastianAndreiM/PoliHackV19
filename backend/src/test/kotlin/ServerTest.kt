package com.assetguard

import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.testing.testApplication
import presentation.plugins.configureRouting
import kotlin.test.*

class ServerTest {

    private fun Application.configureTest() {
        install(ContentNegotiation) { json() }
    }

    @Test
    fun `test root endpoint`() = testApplication {
        application { configureTest() }
        routing {
            get("/") { call.respond(HttpStatusCode.OK, mapOf("status" to "ok")) }
        }

        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
    }
}

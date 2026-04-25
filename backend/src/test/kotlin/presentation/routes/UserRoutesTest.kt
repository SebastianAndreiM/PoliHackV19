// test/presentation/routes/UserRoutesTest.kt
package presentation.routes


import core.model.user.UserProfile
import core.model.user.UserType
import core.usecase.GetProfileUseCase
import core.usecase.RegisterUserUseCase
import core.usecase.UpdateUserTypeUseCase
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.mockk.*
import org.junit.Test
import presentation.plugins.configureStatusPages
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertContains
import kotlin.test.assertEquals

class UserRoutesTest {

    private val register   = mockk<RegisterUserUseCase>()
    private val getProfile = mockk<GetProfileUseCase>()
    private val updateType = mockk<UpdateUserTypeUseCase>()

    private fun fakeProfile(userType: UserType = UserType.DEFAULT) = UserProfile(
        id = UUID.fromString("00000000-0000-0000-0000-000000000001"),
        externalId = "ext-123",
        userType = userType,
        locale = "en",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    private fun Application.configureTest() {
        install(ContentNegotiation) { json() }
        configureStatusPages()
    }

    private fun testApp(block: suspend ApplicationTestBuilder.() -> Unit) = testApplication {
        application {
            configureTest()
        }
        routing {
            route("/api/v1") {
                userRoutes(register, getProfile, updateType)
            }
        }
        block()
    }

    @Test
    fun `POST register returns 201 with profile`() = testApp {
        val profile = fakeProfile()
        coEvery { register.execute("ext-123", "en") } returns profile

        val response = client.post("/api/v1/users/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"externalId":"ext-123","locale":"en"}""")
        }

        assertEquals(HttpStatusCode.Created, response.status)
        assertContains(response.bodyAsText(), "ext-123")
    }

    @Test
    fun `POST register is idempotent for existing user`() = testApp {
        val profile = fakeProfile()
        coEvery { register.execute("ext-123", "en") } returns profile

        val r1 = client.post("/api/v1/users/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"externalId":"ext-123","locale":"en"}""")
        }
        val r2 = client.post("/api/v1/users/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"externalId":"ext-123","locale":"en"}""")
        }

        assertEquals(HttpStatusCode.Created, r1.status)
        assertEquals(HttpStatusCode.Created, r2.status)
    }

    @Test
    fun `GET profile returns 200 when user exists`() = testApp {
        val id      = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val profile = fakeProfile()
        coEvery { getProfile.execute(id) } returns profile

        val response = client.get("/api/v1/users/$id/profile")

        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(response.bodyAsText(), "ext-123")
    }

    @Test
    fun `GET profile returns 404 when user not found`() = testApp {
        val id = UUID.fromString("00000000-0000-0000-0000-000000000002")
        coEvery { getProfile.execute(id) } returns null

        val response = client.get("/api/v1/users/$id/profile")

        assertEquals(HttpStatusCode.NotFound, response.status)
        assertContains(response.bodyAsText(), "error")
    }

    @Test
    fun `PATCH type returns 200 with updated userType`() = testApp {
        val id      = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val updated = fakeProfile(UserType.BUSINESS)
        coEvery { updateType.execute(id, UserType.BUSINESS) } returns updated

        val response = client.patch("/api/v1/users/$id/type") {
            contentType(ContentType.Application.Json)
            setBody("""{"userType":"BUSINESS"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(response.bodyAsText(), "BUSINESS")
    }

    @Test
    fun `PATCH type returns 200 for STUDENT`() = testApp {
        val id      = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val updated = fakeProfile(UserType.STUDENT)
        coEvery { updateType.execute(id, UserType.STUDENT) } returns updated

        val response = client.patch("/api/v1/users/$id/type") {
            contentType(ContentType.Application.Json)
            setBody("""{"userType":"STUDENT"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(response.bodyAsText(), "STUDENT")
    }

    @Test
    fun `PATCH type returns 400 for invalid userType`() = testApp {
        val id = UUID.fromString("00000000-0000-0000-0000-000000000001")

        val response = client.patch("/api/v1/users/$id/type") {
            contentType(ContentType.Application.Json)
            setBody("""{"userType":"INVALID_TYPE"}""")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `PATCH type returns 400 when user not found`() = testApp {
        val id = UUID.fromString("00000000-0000-0000-0000-000000000001")
        coEvery { updateType.execute(id, UserType.SENIOR) } throws
                IllegalArgumentException("User $id not found")

        val response = client.patch("/api/v1/users/$id/type") {
            contentType(ContentType.Application.Json)
            setBody("""{"userType":"SENIOR"}""")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}
package presentation.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import kotlinx.serialization.Serializable


fun Application.configureStatusPages() {
    install(StatusPages) {

        exception<IllegalArgumentException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    status = HttpStatusCode.BadRequest.value,
                    error = "Bad Request",
                    message = cause.message ?: "Invalid input"
                )
            )
        }

        exception<IllegalStateException> { call, cause ->
            call.respond(
                HttpStatusCode.Conflict,
                ErrorResponse(
                    status = HttpStatusCode.Conflict.value,
                    error = "Conflict",
                    message = cause.message ?: "State conflict"
                )
            )
        }

        exception<NoSuchElementException> { call, cause ->
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(
                    status = HttpStatusCode.NotFound.value,
                    error = "Not Found",
                    message = cause.message ?: "Resource not found"
                )
            )
        }

        exception<Throwable> { call, cause ->
            call.application.log.error("Unhandled exception", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    status = HttpStatusCode.InternalServerError.value,
                    error = "Internal Server Error",
                    message = "An unexpected error occurred"
                )
            )
        }

        status(HttpStatusCode.NotFound) { call, status ->
            call.respond(
                status,
                ErrorResponse(
                    status = status.value,
                    error = "Not Found",
                    message = "Route not found"
                )
            )
        }

        status(HttpStatusCode.Unauthorized) { call, status ->
            call.respond(
                status,
                ErrorResponse(
                    status = status.value,
                    error = "Unauthorized",
                    message = "Authentication required"
                )
            )
        }

        status(HttpStatusCode.MethodNotAllowed) { call, status ->
            call.respond(
                status,
                ErrorResponse(
                    status = status.value,
                    error = "Method Not Allowed",
                    message = "HTTP method not supported for this route"
                )
            )
        }
    }
}

@Serializable
data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String
)
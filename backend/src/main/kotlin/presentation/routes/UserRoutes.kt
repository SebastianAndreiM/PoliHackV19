package presentation.routes

import core.model.user.UserType
import core.usecase.user.GetProfileUseCase
import core.usecase.user.RegisterUserUseCase
import core.usecase.user.UpdateUserTypeUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import presentation.dto.user.RegisterRequest
import presentation.dto.user.UpdateUserTypeRequest
import presentation.mapper.toResponse
import java.util.UUID

fun Route.userRoutes(
    register: RegisterUserUseCase,
    getProfile: GetProfileUseCase,
    updateType: UpdateUserTypeUseCase
) {
    route("/users") {

        post("/register") {
            val req     = call.receive<RegisterRequest>()
            val profile = register.execute(req.externalId, req.locale)
            call.respond(HttpStatusCode.Created, profile.toResponse())
        }

        get("/{id}/profile") {
            val id      = UUID.fromString(call.parameters["id"])
            val profile = getProfile.execute(id)
                ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(profile.toResponse())
        }

        patch("/{id}/type") {
            val id   = UUID.fromString(call.parameters["id"])
            val req  = call.receive<UpdateUserTypeRequest>()
            val type = runCatching { UserType.valueOf(req.userType.uppercase()) }
                .getOrElse {
                    return@patch call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("error" to "Invalid userType: ${req.userType}")
                    )
                }
            val updated = updateType.execute(id, type)
            call.respond(updated.toResponse())
        }
    }
}
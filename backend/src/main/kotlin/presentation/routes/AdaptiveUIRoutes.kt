package presentation.routes

import core.model.user.UserType
import core.usecase.ui.GetAdaptiveLayoutUseCase
import core.usecase.user.GetProfileUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import presentation.mapper.toDto
import presentation.plugins.ErrorResponse
import java.util.UUID

fun Route.adaptiveUiRoutes(
    getLayout: GetAdaptiveLayoutUseCase,
    getProfile: GetProfileUseCase
) {
    route("/ui") {

        get("/layout") {
            val userId = call.request.headers["X-User-Id"]
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse(400, "Bad Request", "Missing X-User-Id header")
                )

            val userType = getProfile.execute(UUID.fromString(userId))
                ?.userType ?: UserType.DEFAULT

            val layout = getLayout.execute(userType)
            call.respond(layout.toDto())
        }

        get("/layout/{userType}") {
            val type = runCatching {
                UserType.valueOf(call.parameters["userType"]!!.uppercase())
            }.getOrElse { UserType.DEFAULT }

            val layout = getLayout.execute(type)
            call.respond(layout.toDto())
        }
    }
}
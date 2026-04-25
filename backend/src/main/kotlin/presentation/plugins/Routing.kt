package presentation.plugins


import core.usecase.GetProfileUseCase
import core.usecase.RegisterUserUseCase
import core.usecase.UpdateUserTypeUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject
import presentation.routes.userRoutes


fun Application.configureRouting() {
    val register by inject<RegisterUserUseCase>()
    val getProfile by inject<GetProfileUseCase>()
    val updateType by inject<UpdateUserTypeUseCase>()

    routing {
        get("/health") {
            call.respond(HttpStatusCode.OK, mapOf("status" to "ok"))
        }
        route("/api/v1") {
            userRoutes(register, getProfile, updateType)
        }
    }
}
package presentation.plugins


import core.usecase.retention.GetNudgesUseCase
import core.usecase.telemetry.EndSessionUseCase
import core.usecase.telemetry.GetHeatmapUseCase
import core.usecase.telemetry.LogEventUseCase
import core.usecase.telemetry.StartSessionUseCase
import core.usecase.ui.GetAdaptiveLayoutUseCase
import core.usecase.user.GetProfileUseCase
import core.usecase.user.RegisterUserUseCase
import core.usecase.user.UpdateUserTypeUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject
import presentation.routes.adaptiveUiRoutes
import presentation.routes.retentionRoutes
import presentation.routes.telemetryRoutes
import presentation.routes.userRoutes


fun Application.configureRouting() {
    val register by inject<RegisterUserUseCase>()
    val getProfile by inject<GetProfileUseCase>()
    val updateType by inject<UpdateUserTypeUseCase>()

    val getLayout by inject<GetAdaptiveLayoutUseCase>()

    val logEvent by inject<LogEventUseCase>()
    val startSession by inject<StartSessionUseCase>()
    val endSession by inject<EndSessionUseCase>()
    val getHeatmap by inject<GetHeatmapUseCase>()

    val getNudges by inject<GetNudgesUseCase>()

    routing {
        get("/health") {
            call.respond(HttpStatusCode.OK, mapOf("status" to "ok"))
        }

        route("/api/v1") {
            userRoutes(register, getProfile, updateType)
            adaptiveUiRoutes(getLayout, getProfile)
            telemetryRoutes(logEvent, startSession, endSession, getHeatmap)
            retentionRoutes(getNudges)
        }
    }
}
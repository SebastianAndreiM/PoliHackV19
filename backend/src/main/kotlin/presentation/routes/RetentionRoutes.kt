package presentation.routes

import core.usecase.retention.GetNudgesUseCase
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import presentation.dto.retention.NudgeDto
import presentation.dto.retention.NudgesResponse
import java.util.UUID

fun Route.retentionRoutes(getNudges: GetNudgesUseCase) {
    route("/retention") {

        get("/nudges/{userId}") {
            val userId = UUID.fromString(call.parameters["userId"])
            val nudges = getNudges.execute(userId)

            call.respond(
                NudgesResponse(
                    userId = userId.toString(),
                    nudges = nudges.map {
                        NudgeDto(
                            id = it.id.toString(),
                            type = it.type.name,
                            title = it.title,
                            message = it.message,
                            deepLink = it.deepLink,
                            priority = it.priority,
                            expiresAt = it.expiresAt?.toString(),
                            createdAt = it.createdAt.toString()
                        )
                    }
                ))
        }
    }
}
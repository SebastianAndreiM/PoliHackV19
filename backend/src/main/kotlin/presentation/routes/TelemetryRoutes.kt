package presentation.routes

import core.model.telemetry.EventType
import core.model.telemetry.TelemetryEvent
import core.usecase.telemetry.EndSessionUseCase
import core.usecase.telemetry.GetHeatmapUseCase
import core.usecase.telemetry.LogEventUseCase
import core.usecase.telemetry.StartSessionUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import jdk.internal.vm.ScopedValueContainer.call
import presentation.dto.telemetry.HeatmapEntryDto
import presentation.dto.telemetry.HeatmapResponseDto
import presentation.dto.telemetry.LogEventRequest
import presentation.dto.telemetry.SessionResponse
import presentation.dto.telemetry.StartSessionRequest
import presentation.dto.telemetry.TelemetryEventResponse
import java.time.LocalDateTime
import java.util.UUID

fun Route.telemetryRoutes(
    logEvent: LogEventUseCase,
    startSession: StartSessionUseCase,
    endSession: EndSessionUseCase,
    getHeatmap: GetHeatmapUseCase
) {
    route("/telemetry") {

        post("/sessions") {
            val req = call.receive<StartSessionRequest>()
            val session = startSession.execute(
                userId = UUID.fromString(req.userId),
                deviceInfo = req.deviceInfo
            )
            call.respond(
                HttpStatusCode.Created, SessionResponse(
                    id = session.id.toString(),
                    userId = session.userId.toString(),
                    startedAt = session.startedAt.toString(),
                    endedAt = session.endedAt?.toString()
                )
            )
        }

        patch("/sessions/{id}/end") {
            val id = UUID.fromString(call.parameters["id"])
            val session = endSession.execute(id)
                ?: return@patch call.respond(HttpStatusCode.NotFound)
            call.respond(
                SessionResponse(
                    id = session.id.toString(),
                    userId = session.userId.toString(),
                    startedAt = session.startedAt.toString(),
                    endedAt = session.endedAt?.toString()
                )
            )
        }

        post("/events") {
            val req = call.receive<LogEventRequest>()
            val type = runCatching { EventType.valueOf(req.eventType.uppercase()) }
                .getOrElse { throw IllegalArgumentException("Invalid eventType: ${req.eventType}") }

            val event = logEvent.execute(
                TelemetryEvent(
                    id = UUID.randomUUID(),
                    sessionId = UUID.fromString(req.sessionId),
                    userId = UUID.fromString(req.userId),
                    eventType = type,
                    screen = req.screen,
                    componentKey = req.componentKey,
                    durationMs = req.durationMs,
                    metadata = req.metadata,
                    createdAt = LocalDateTime.now()
                )
            )
            call.respond(
                HttpStatusCode.Created, TelemetryEventResponse(
                    id = event.id.toString(),
                    sessionId = event.sessionId.toString(),
                    eventType = event.eventType.name,
                    screen = event.screen,
                    componentKey = event.componentKey,
                    durationMs = event.durationMs,
                    createdAt = event.createdAt.toString()
                )
            )
        }

        get("/heatmap") {
            val screen = call.request.queryParameters["screen"]
            val heatmap = getHeatmap.execute(screen)
            call.respond(
                HeatmapResponseDto(
                    totalEvents = heatmap.totalEvents,
                    uniqueUsers = heatmap.uniqueUsers,
                    entries = heatmap.entries.map {
                        HeatmapEntryDto(
                            screen = it.screen,
                            componentKey = it.componentKey,
                            tapCount = it.tapCount,
                            abandonCount = it.abandonCount,
                            hesitateCount = it.hesitateCount,
                            avgDurationMs = it.avgDurationMs,
                            dropOffRate = it.dropOffRate
                        )
                    }
                ))
        }
    }
}
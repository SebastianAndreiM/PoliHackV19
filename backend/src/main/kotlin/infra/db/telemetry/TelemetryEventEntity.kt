package infra.db.telemetry

import core.model.telemetry.TelemetryEvent
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class TelemetryEventEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<TelemetryEventEntity>(TelemetryEventsTable)

    var sessionId    by TelemetryEventsTable.sessionId
    var userId       by TelemetryEventsTable.userId
    var eventType    by TelemetryEventsTable.eventType
    var screen       by TelemetryEventsTable.screen
    var componentKey by TelemetryEventsTable.componentKey
    var durationMs   by TelemetryEventsTable.durationMs
    var metadata     by TelemetryEventsTable.metadata
    var createdAt    by TelemetryEventsTable.createdAt

    fun toDomain() = TelemetryEvent(
        id = this.id.value,
        sessionId = sessionId,
        userId = userId,
        eventType = eventType,
        screen = screen,
        componentKey = componentKey,
        durationMs = durationMs,
        metadata = Json.decodeFromString(metadata),
        createdAt = createdAt
    )
}
package infra.db.telemetry

import core.model.telemetry.EventType
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object TelemetryEventsTable : UUIDTable("telemetry_events") {
    val sessionId    = uuid("session_id")
    val userId       = uuid("user_id")
    val eventType    = enumerationByName("event_type", 20, EventType::class)
    val screen       = varchar("screen", 128)
    val componentKey = varchar("component_key", 128).nullable()
    val durationMs   = long("duration_ms").nullable()
    val metadata     = text("metadata").default("{}")
    val createdAt    = datetime("created_at")
}
package core.model.telemetry

import java.time.LocalDateTime
import java.util.UUID

data class TelemetryEvent(
    val id: UUID,
    val sessionId: UUID,
    val userId: UUID,
    val eventType: EventType,
    val screen: String,
    val componentKey: String?,
    val durationMs: Long?,
    val metadata: Map<String, String>,
    val createdAt: LocalDateTime
)
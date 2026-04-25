package presentation.dto.telemetry

import kotlinx.serialization.Serializable

@Serializable
data class TelemetryEventResponse(
    val id: String,
    val sessionId: String,
    val eventType: String,
    val screen: String,
    val componentKey: String?,
    val durationMs: Long?,
    val createdAt: String
)
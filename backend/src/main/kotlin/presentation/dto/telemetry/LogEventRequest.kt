package presentation.dto.telemetry

import kotlinx.serialization.Serializable

@Serializable
data class LogEventRequest(
    val sessionId: String,
    val userId: String,
    val eventType: String,
    val screen: String,
    val componentKey: String? = null,
    val durationMs: Long? = null,
    val metadata: Map<String, String> = emptyMap()
)
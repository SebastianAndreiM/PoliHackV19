package presentation.dto.telemetry

import kotlinx.serialization.Serializable

@Serializable
data class SessionResponse(
    val id: String,
    val userId: String,
    val startedAt: String,
    val endedAt: String?
)
package presentation.dto.telemetry

import kotlinx.serialization.Serializable

@Serializable
data class StartSessionRequest(
    val userId: String,
    val deviceInfo: String? = null
)
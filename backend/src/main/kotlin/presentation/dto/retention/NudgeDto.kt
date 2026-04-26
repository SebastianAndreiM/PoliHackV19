package presentation.dto.retention

import kotlinx.serialization.Serializable

@Serializable
data class NudgeDto(
    val id: String,
    val type: String,
    val title: String,
    val message: String,
    val deepLink: String?,
    val priority: Int,
    val expiresAt: String?,
    val createdAt: String
)

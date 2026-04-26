package presentation.dto.ai

import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val id: String,
    val reply: String,
    val deepLink: String?,
    val uiHint: String?,
    val role: String,
    val createdAt: String
)
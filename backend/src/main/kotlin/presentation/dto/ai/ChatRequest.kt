package presentation.dto.ai

import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val userId: String,
    val userType: String = "DEFAULT",
    val message: String
)
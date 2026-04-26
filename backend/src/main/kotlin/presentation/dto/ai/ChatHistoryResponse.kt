package presentation.dto.ai

import kotlinx.serialization.Serializable

@Serializable
data class ChatHistoryResponse(
    val userId: String,
    val messages: List<ChatResponse>
)
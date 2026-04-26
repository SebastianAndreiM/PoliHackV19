package core.model.ai

import java.time.LocalDateTime
import java.util.UUID

data class ChatMessage(
    val id: UUID,
    val userId: UUID,
    val role: MessageRole,
    val content: String,
    val deepLink: String?,
    val uiHint: String?,
    val createdAt: LocalDateTime
)

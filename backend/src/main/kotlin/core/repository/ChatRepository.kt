package core.repository

import core.model.ai.ChatMessage
import java.util.UUID

interface ChatRepository {
    suspend fun saveMessage(message: ChatMessage): ChatMessage
    suspend fun getHistory(userId: UUID, limit: Int = 10): List<ChatMessage>
}
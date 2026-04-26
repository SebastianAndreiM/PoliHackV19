package core.usecase.ai

import core.model.ai.ChatMessage
import core.repository.ChatRepository
import java.util.UUID

class GetChatHistoryUseCase(private val repo: ChatRepository) {
    suspend fun execute(userId: UUID, limit: Int = 10): List<ChatMessage> =
        repo.getHistory(userId, limit)
}
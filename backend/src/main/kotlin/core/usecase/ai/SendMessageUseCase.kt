package core.usecase.ai

import core.model.ai.ChatMessage
import core.model.ai.IntentResponse
import core.model.ai.MessageRole
import core.model.user.UserType
import core.repository.AIGateway
import core.repository.ChatRepository
import java.time.LocalDateTime
import java.util.UUID

class SendMessageUseCase(
    private val gateway: AIGateway,
    private val repo: ChatRepository
) {
    suspend fun execute(
        userId: UUID,
        userType: UserType,
        message: String
    ): ChatMessage {
        val history = repo.getHistory(userId)
            .map { it.role.name.lowercase() to it.content }

        repo.saveMessage(ChatMessage(
            id        = UUID.randomUUID(),
            userId    = userId,
            role      = MessageRole.USER,
            content   = message,
            deepLink  = null,
            uiHint    = null,
            createdAt = LocalDateTime.now()
        ))

        val intent = try {
            gateway.parseIntent(message, userType, history)
        } catch (e: Exception) {
            println("AI Gateway failed: ${e::class.simpleName}: ${e.message}")
            IntentResponse(
                intent = "unknown",
                deepLink = null,
                uiHint = null,
                reply = "Încearcă din nou, te rog.",
                confidence = 0.0
            )
        }

        return repo.saveMessage(ChatMessage(
            id        = UUID.randomUUID(),
            userId    = userId,
            role      = MessageRole.ASSISTANT,
            content   = intent.reply,
            deepLink  = intent.deepLink,
            uiHint    = intent.uiHint,
            createdAt = LocalDateTime.now()
        ))
    }
}
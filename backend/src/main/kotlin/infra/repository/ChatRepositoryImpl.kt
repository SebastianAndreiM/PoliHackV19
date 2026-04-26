package infra.repository

import core.model.ai.ChatMessage
import core.repository.ChatRepository
import infra.db.ai.AIChatLogEntity
import infra.db.ai.AIChatLogsTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

class ChatRepositoryImpl : ChatRepository {

    override suspend fun saveMessage(message: ChatMessage): ChatMessage =
        newSuspendedTransaction(Dispatchers.IO) {
            AIChatLogEntity.new(message.id) {
                userId    = message.userId
                role      = message.role
                content   = message.content
                deepLink  = message.deepLink
                uiHint    = message.uiHint
                createdAt = message.createdAt
            }.toDomain()
        }

    override suspend fun getHistory(userId: UUID, limit: Int): List<ChatMessage> =
        newSuspendedTransaction(Dispatchers.IO) {
            AIChatLogEntity
                .find { AIChatLogsTable.userId eq userId }
                .orderBy(AIChatLogsTable.createdAt to SortOrder.DESC)
                .limit(limit)
                .map { it.toDomain() }
                .reversed()
        }
}
package infra.db.ai

import core.model.ai.ChatMessage
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class AIChatLogEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<AIChatLogEntity>(AIChatLogsTable)

    var userId    by AIChatLogsTable.userId
    var role      by AIChatLogsTable.role
    var content   by AIChatLogsTable.content
    var deepLink  by AIChatLogsTable.deepLink
    var uiHint    by AIChatLogsTable.uiHint
    var createdAt by AIChatLogsTable.createdAt

    fun toDomain() = ChatMessage(
        id = this.id.value,
        userId = userId,
        role = role,
        content = content,
        deepLink = deepLink,
        uiHint = uiHint,
        createdAt = createdAt
    )
}
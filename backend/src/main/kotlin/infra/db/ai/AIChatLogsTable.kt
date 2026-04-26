package infra.db.ai

import core.model.ai.MessageRole
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object AIChatLogsTable : UUIDTable("ai_chat_logs") {
    val userId = uuid("user_id")
    val role = enumerationByName("role", 16, MessageRole::class)
    val content = text("content")
    val deepLink = varchar("deep_link", 256).nullable()
    val uiHint = varchar("ui_hint", 128).nullable()
    val createdAt = datetime("created_at")
}
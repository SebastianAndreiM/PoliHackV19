package core.model.retention

import java.time.LocalDateTime
import java.util.UUID

data class Nudge(
    val id: UUID,
    val userId: UUID,
    val type: NudgeType,
    val title: String,
    val message: String,
    val deepLink: String?,
    val priority: Int,
    val expiresAt: LocalDateTime?,
    val createdAt: LocalDateTime
)

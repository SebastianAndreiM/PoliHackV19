package core.model.retention

import core.model.user.UserType
import java.util.UUID

data class RetentionContext(
    val userId: UUID,
    val userType: UserType,
    val daysSinceLastSession: Long,
    val totalSessions: Long,
    val abandonCount: Long,
    val recentScreens: List<String>,
    val consecutiveDays: Long
)
package core.model.user

import java.time.LocalDateTime
import java.util.UUID

data class UserProfile(
    val id: UUID,
    val externalId: String,
    val userType: UserType,
    val locale: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
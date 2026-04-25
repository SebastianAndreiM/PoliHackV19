package core.repository

import core.model.user.UserProfile
import core.model.user.UserType
import java.util.UUID

interface UserRepository {
    suspend fun findById(id: UUID): UserProfile?
    suspend fun findByExternalId(externalId: String): UserProfile?
    suspend fun upsert(profile: UserProfile): UserProfile
    suspend fun updateUserType(id: UUID, userType: UserType): UserProfile?
}
package infra.repository

import core.model.user.UserProfile
import core.model.user.UserType
import core.repository.UserRepository
import infra.db.UserEntity
import infra.db.UsersTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime
import java.util.UUID

class UserRepositoryImpl : UserRepository {

    override suspend fun findById(id: UUID): UserProfile? =
        newSuspendedTransaction(Dispatchers.IO) {
            UserEntity.findById(id)?.toDomain()
        }

    override suspend fun findByExternalId(externalId: String): UserProfile? =
        newSuspendedTransaction(Dispatchers.IO) {
            UserEntity
                .find { UsersTable.externalId eq externalId }
                .firstOrNull()
                ?.toDomain()
        }

    override suspend fun upsert(profile: UserProfile): UserProfile =
        newSuspendedTransaction(Dispatchers.IO) {
            val existing = UserEntity.findById(profile.id)
            if (existing != null) {
                existing.userType = profile.userType
                existing.locale = profile.locale
                existing.updatedAt = LocalDateTime.now()
                existing.toDomain()
            } else {
                UserEntity.new(profile.id) {
                    externalId = profile.externalId
                    userType = profile.userType
                    locale = profile.locale
                    createdAt = profile.createdAt
                    updatedAt = profile.updatedAt
                }.toDomain()
            }
        }

    override suspend fun updateUserType(id: UUID, userType: UserType): UserProfile? =
        newSuspendedTransaction(Dispatchers.IO) {
            UserEntity.findById(id)?.apply {
                this.userType = userType
                this.updatedAt = LocalDateTime.now()
            }?.toDomain()
        }
}
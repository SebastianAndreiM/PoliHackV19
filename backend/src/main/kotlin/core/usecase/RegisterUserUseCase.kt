package core.usecase

import core.model.user.UserProfile
import core.model.user.UserType
import core.repository.UserRepository
import java.time.LocalDateTime
import java.util.UUID

class RegisterUserUseCase(private val repo: UserRepository) {
    suspend fun execute(externalId: String, locale: String): UserProfile {
        val existing = repo.findByExternalId(externalId)
        if (existing != null) return existing
        return repo.upsert(
            UserProfile(
                id = UUID.randomUUID(),
                externalId = externalId,
                userType = UserType.DEFAULT,
                locale = locale,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )
    }
}
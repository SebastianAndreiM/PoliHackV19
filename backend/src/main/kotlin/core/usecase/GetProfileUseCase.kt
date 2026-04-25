package core.usecase

import core.model.user.UserProfile
import core.repository.UserRepository
import java.util.UUID

class GetProfileUseCase(private val repo: UserRepository) {
    suspend fun execute(id: UUID): UserProfile? = repo.findById(id)
}
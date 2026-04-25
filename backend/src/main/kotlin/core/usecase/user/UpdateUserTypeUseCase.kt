package core.usecase.user

import core.model.user.UserProfile
import core.model.user.UserType
import core.repository.UserRepository
import java.util.UUID

class UpdateUserTypeUseCase(private val repo: UserRepository) {
    suspend fun execute(id: UUID, userType: UserType): UserProfile =
        repo.updateUserType(id, userType)
            ?: throw IllegalArgumentException("User $id not found")
}
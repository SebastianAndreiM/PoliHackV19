package com.assetguard.domain.usecase

import core.model.user.UserProfile
import core.model.user.UserType
import core.repository.UserRepository
import core.usecase.UpdateUserTypeUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UpdateUserTypeUseCaseTest {

    private val repo    = mockk<UserRepository>()
    private val useCase = UpdateUserTypeUseCase(repo)

    @Test
    fun `updates user type successfully`() = runTest {
        val id      = UUID.randomUUID()
        val updated = UserProfile(id, "ext-1", UserType.BUSINESS, "en", LocalDateTime.now(), LocalDateTime.now())
        coEvery { repo.updateUserType(id, UserType.BUSINESS) } returns updated

        val result = useCase.execute(id, UserType.BUSINESS)

        assertEquals(UserType.BUSINESS, result.userType)
    }

    @Test
    fun `throws when user not found`() = runTest {
        val id = UUID.randomUUID()
        coEvery { repo.updateUserType(id, any()) } returns null

        assertFailsWith<IllegalArgumentException> {
            useCase.execute(id, UserType.SENIOR)
        }
    }
}
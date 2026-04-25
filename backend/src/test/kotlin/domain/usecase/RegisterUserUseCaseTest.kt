package com.assetguard.domain.usecase

import core.model.user.UserProfile
import core.model.user.UserType
import core.repository.UserRepository
import core.usecase.user.RegisterUserUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class RegisterUserUseCaseTest {

    private val repo = mockk<UserRepository>()
    private val useCase = RegisterUserUseCase(repo)

    private fun fakeProfile(
        externalId: String = "ext-123",
        userType: UserType = UserType.DEFAULT
    ) = UserProfile(
        id = UUID.randomUUID(),
        externalId = externalId,
        userType = userType,
        locale = "en",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    @Test
    fun `register new user returns saved profile`() = runTest {
        val profile = fakeProfile()
        coEvery { repo.findByExternalId(any()) } returns null
        coEvery { repo.upsert(any()) } returns profile

        val result = useCase.execute("ext-123", "en")

        assertEquals(profile.externalId, result.externalId)
        assertEquals(UserType.DEFAULT, result.userType)
        coVerify(exactly = 1) { repo.upsert(any()) }
    }

    @Test
    fun `register existing user returns existing profile without upsert`() = runTest {
        val existing = fakeProfile()
        coEvery { repo.findByExternalId("ext-123") } returns existing

        val result = useCase.execute("ext-123", "en")

        assertEquals(existing.id, result.id)
        coVerify(exactly = 0) { repo.upsert(any()) }
    }
}
package com.assetguard.domain.usecase

import core.model.user.UserProfile
import core.model.user.UserType
import core.repository.UserRepository
import core.usecase.GetProfileUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class GetProfileUseCaseTest {

    private val repo    = mockk<UserRepository>()
    private val useCase = GetProfileUseCase(repo)

    @Test
    fun `returns profile when user exists`() = runTest {
        val id      = UUID.randomUUID()
        val profile = UserProfile(id, "ext-1", UserType.STUDENT, "ro", LocalDateTime.now(), LocalDateTime.now())
        coEvery { repo.findById(id) } returns profile

        val result = useCase.execute(id)

        assertNotNull(result)
        coVerify(exactly = 1) { repo.findById(id) }
    }

    @Test
    fun `returns null when user does not exist`() = runTest {
        val id = UUID.randomUUID()
        coEvery { repo.findById(id) } returns null

        val result = useCase.execute(id)

        assertNull(result)
    }
}
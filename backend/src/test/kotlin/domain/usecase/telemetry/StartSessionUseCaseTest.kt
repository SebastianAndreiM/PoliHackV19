package com.assetguard.domain.usecase.telemetry

import core.model.telemetry.Session
import core.repository.TelemetryRepository
import core.usecase.telemetry.StartSessionUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StartSessionUseCaseTest {

    private val repo    = mockk<TelemetryRepository>()
    private val useCase = StartSessionUseCase(repo)

    @Test
    fun `start session creates session with null endedAt`() = runTest {
        val userId = UUID.randomUUID()
        val session = Session(
            id         = UUID.randomUUID(),
            userId     = userId,
            deviceInfo = "Android 14",
            startedAt  = LocalDateTime.now(),
            endedAt    = null
        )
        coEvery { repo.saveSession(any()) } returns session

        val result = useCase.execute(userId, "Android 14")

        assertNull(result.endedAt)
        assertEquals(userId, result.userId)
        coVerify(exactly = 1) { repo.saveSession(any()) }
    }

    @Test
    fun `start session with null deviceInfo is valid`() = runTest {
        val userId = UUID.randomUUID()
        val session = Session(UUID.randomUUID(), userId, null, LocalDateTime.now(), null)
        coEvery { repo.saveSession(any()) } returns session

        val result = useCase.execute(userId, null)

        assertNull(result.deviceInfo)
    }
}
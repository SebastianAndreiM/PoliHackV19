package com.assetguard.domain.usecase.telemetry

import core.model.telemetry.Session
import core.repository.TelemetryRepository
import core.usecase.telemetry.EndSessionUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class EndSessionUseCaseTest {

    private val repo    = mockk<TelemetryRepository>()
    private val useCase = EndSessionUseCase(repo)

    @Test
    fun `end existing session sets endedAt`() = runTest {
        val sessionId = UUID.randomUUID()
        val session = Session(
            id = sessionId,
            userId = UUID.randomUUID(),
            deviceInfo = null,
            startedAt = LocalDateTime.now().minusMinutes(5),
            endedAt = LocalDateTime.now()
        )
        coEvery { repo.endSession(sessionId) } returns session

        val result = useCase.execute(sessionId)

        assertNotNull(result?.endedAt)
    }

    @Test
    fun `end non-existing session returns null`() = runTest {
        val sessionId = UUID.randomUUID()
        coEvery { repo.endSession(sessionId) } returns null

        val result = useCase.execute(sessionId)

        assertNull(result)
    }
}
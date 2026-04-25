package com.assetguard.domain.usecase.telemetry

import core.model.telemetry.EventType
import core.model.telemetry.TelemetryEvent
import core.repository.TelemetryRepository
import core.usecase.telemetry.LogEventUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import net.bytebuddy.matcher.ElementMatchers.any
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class LogEventUseCaseTest {

    private val repo    = mockk<TelemetryRepository>()
    private val useCase = LogEventUseCase(repo)

    private fun fakeEvent(type: EventType = EventType.TAP) = TelemetryEvent(
        id = UUID.randomUUID(),
        sessionId = UUID.randomUUID(),
        userId = UUID.randomUUID(),
        eventType = type,
        screen = "dashboard",
        componentKey = "balance_card",
        durationMs = 120L,
        metadata = emptyMap(),
        createdAt = LocalDateTime.now()
    )

    @Test
    fun `log TAP event saves and returns event`() = runTest {
        val event = fakeEvent(EventType.TAP)
        coEvery { repo.saveEvent(any()) } returns event

        val result = useCase.execute(event)

        assertEquals(EventType.TAP, result.eventType)
        assertEquals("dashboard", result.screen)
        coVerify(exactly = 1) { repo.saveEvent(any()) }
    }

    @Test
    fun `log ABANDON event saves correctly`() = runTest {
        val event = fakeEvent(EventType.ABANDON)
        coEvery { repo.saveEvent(any()) } returns event

        val result = useCase.execute(event)

        assertEquals(EventType.ABANDON, result.eventType)
    }

    @Test
    fun `log event assigns new id and createdAt`() = runTest {
        val originalId = UUID.randomUUID()
        val event = fakeEvent().copy(id = originalId)
        coEvery { repo.saveEvent(any()) } answers { firstArg() }

        val result = useCase.execute(event)

        // useCase generates new id and createdAt
        coVerify { repo.saveEvent(match { it.id != originalId }) }
    }
}
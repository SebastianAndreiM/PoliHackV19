package com.assetguard.domain.usecase.retention

import core.model.retention.Nudge
import core.model.retention.NudgeType
import core.model.retention.RetentionContext
import core.model.user.UserType
import core.repository.RetentionRepository
import core.service.NudgeEngine
import core.usecase.retention.GetNudgesUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import net.bytebuddy.matcher.ElementMatchers.any
import sun.java2d.cmm.ProfileDataVerifier.verify
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test

class GetNudgesUseCaseTest {

    private val repo = mockk<RetentionRepository>()
    private val engine = mockk<NudgeEngine>()
    private val useCase = GetNudgesUseCase(repo, engine)

    private val userId = UUID.fromString("00000000-0000-0000-0000-000000000001")

    private fun fakeContext(
        userType: UserType = UserType.DEFAULT,
        daysSinceLastSession: Long = 0,
        totalSessions: Long = 5,
        abandonCount: Long = 0,
        recentScreens: List<String> = emptyList(),
        consecutiveDays: Long = 1
    ) = RetentionContext(
        userId = userId,
        userType = userType,
        daysSinceLastSession = daysSinceLastSession,
        totalSessions = totalSessions,
        abandonCount = abandonCount,
        recentScreens = recentScreens,
        consecutiveDays = consecutiveDays
    )

    private fun fakeNudge(type: NudgeType, priority: Int) = Nudge(
        id = UUID.randomUUID(),
        userId = userId,
        type = type,
        title = "Test",
        message = "Test message",
        deepLink = "/test",
        priority = priority,
        expiresAt = LocalDateTime.now().plusDays(1),
        createdAt = LocalDateTime.now()
    )

    @Test
    fun `get nudges returns empty list when no context found`() = runTest {
        coEvery { repo.getContext(userId) } returns null

        val result = useCase.execute(userId)

        assertTrue(result.isEmpty())
        verify(exactly = 0) { engine.evaluate(any()) }
    }

    @Test
    fun `get nudges returns nudges from engine`() = runTest {
        val context = fakeContext()
        val nudges = listOf(
            fakeNudge(NudgeType.CASHBACK_PROXIMITY, 2),
            fakeNudge(NudgeType.INACTIVITY, 1)
        )
        coEvery { repo.getContext(userId) } returns context
        every { engine.evaluate(context) } returns nudges

        val result = useCase.execute(userId)

        assertEquals(2, result.size)
    }

    @Test
    fun `get nudges returns nudges sorted by priority`() = runTest {
        val context = fakeContext()
        val nudges = listOf(
            fakeNudge(NudgeType.STREAK_REWARD, 3),
            fakeNudge(NudgeType.INACTIVITY, 1),
            fakeNudge(NudgeType.CASHBACK_PROXIMITY, 2)
        )
        coEvery { repo.getContext(userId) } returns context
        every { engine.evaluate(context) } returns nudges

        val result = useCase.execute(userId)

        assertEquals(listOf(1, 2, 3), result.map { it.priority })
    }

    @Test
    fun `get nudges calls engine with correct context`() = runTest {
        val context = fakeContext(
            userType = UserType.BUSINESS,
            daysSinceLastSession = 10,
            totalSessions = 20
        )
        coEvery { repo.getContext(userId) } returns context
        every { engine.evaluate(any()) } returns emptyList()

        useCase.execute(userId)

        verify { engine.evaluate(context) }
    }
}
package com.assetguard.domain.service

import core.model.retention.NudgeType
import core.model.retention.RetentionContext
import core.model.user.UserType
import infra.service.NudgeEngineImpl
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import java.util.UUID
import kotlin.test.Test

class NudgeEngineTest {

    private val engine = NudgeEngineImpl()
    private val userId = UUID.fromString("00000000-0000-0000-0000-000000000001")

    private fun context(
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

    // ── INACTIVITY ───────────────────────────────────────────────

    @Test
    fun `evaluates INACTIVITY nudge when inactive 7 days`() {
        val result = engine.evaluate(context(daysSinceLastSession = 7))
        assertTrue(result.any { it.type == NudgeType.INACTIVITY })
    }

    @Test
    fun `INACTIVITY nudge has priority 1 when inactive 7+ days`() {
        val result = engine.evaluate(context(daysSinceLastSession = 7))
        val nudge = result.first { it.type == NudgeType.INACTIVITY }
        assertEquals(1, nudge.priority)
    }

    @Test
    fun `evaluates INACTIVITY nudge with priority 2 when inactive 3 days`() {
        val result = engine.evaluate(context(daysSinceLastSession = 3))
        val nudge = result.first { it.type == NudgeType.INACTIVITY }
        assertEquals(2, nudge.priority)
    }

    @Test
    fun `no INACTIVITY nudge when active recently`() {
        val result = engine.evaluate(context(daysSinceLastSession = 0))
        assertTrue(result.none { it.type == NudgeType.INACTIVITY })
    }

    @Test
    fun `INACTIVITY message differs per userType`() {
        val student = engine.evaluate(context(userType = UserType.STUDENT, daysSinceLastSession = 7))
        val business = engine.evaluate(context(userType = UserType.BUSINESS, daysSinceLastSession = 7))
        val senior = engine.evaluate(context(userType = UserType.SENIOR, daysSinceLastSession = 7))

        val studentMsg = student.first { it.type == NudgeType.INACTIVITY }.message
        val businessMsg = business.first { it.type == NudgeType.INACTIVITY }.message
        val seniorMsg = senior.first { it.type == NudgeType.INACTIVITY }.message

        assertTrue(studentMsg != businessMsg)
        assertTrue(businessMsg != seniorMsg)
    }

    // ── CASHBACK PROXIMITY ───────────────────────────────────────

    @Test
    fun `CASHBACK_PROXIMITY nudge has deepLink to cashback`() {
        val result = engine.evaluate(context(totalSessions = 4))
        val nudge = result.first { it.type == NudgeType.CASHBACK_PROXIMITY }
        assertEquals("/cashback", nudge.deepLink)
    }

    @Test
    fun `evaluates CASHBACK_PROXIMITY nudge when close to threshold`() {
        val result = engine.evaluate(context(totalSessions = 4)) // 1 away from reward
        assertTrue(result.any { it.type == NudgeType.CASHBACK_PROXIMITY })
    }

    // ── ABANDON RECOVERY ─────────────────────────────────────────

    @Test
    fun `evaluates ABANDON_RECOVERY nudge when transfer abandoned`() {
        val result = engine.evaluate(
            context(
                abandonCount = 1,
                recentScreens = listOf("transfer")
            )
        )
        assertTrue(result.any { it.type == NudgeType.ABANDON_RECOVERY })
    }

    @Test
    fun `no ABANDON_RECOVERY nudge when no abandon events`() {
        val result = engine.evaluate(
            context(
                abandonCount = 0,
                recentScreens = listOf("transfer")
            )
        )
        assertTrue(result.none { it.type == NudgeType.ABANDON_RECOVERY })
    }

    @Test
    fun `ABANDON_RECOVERY nudge has priority 1`() {
        val result = engine.evaluate(
            context(
                abandonCount = 2,
                recentScreens = listOf("transfer")
            )
        )
        val nudge = result.first { it.type == NudgeType.ABANDON_RECOVERY }
        assertEquals(1, nudge.priority)
    }

    @Test
    fun `ABANDON_RECOVERY deepLink is transfer`() {
        val result = engine.evaluate(
            context(
                abandonCount = 1,
                recentScreens = listOf("transfer")
            )
        )
        val nudge = result.first { it.type == NudgeType.ABANDON_RECOVERY }
        assertEquals("/transfer", nudge.deepLink)
    }

    // ── SAVINGS MILESTONE ────────────────────────────────────────

    @Test
    fun `evaluates SAVINGS_MILESTONE for STUDENT with enough sessions`() {
        val result = engine.evaluate(
            context(
                userType = UserType.STUDENT,
                totalSessions = 5
            )
        )
        assertTrue(result.any { it.type == NudgeType.SAVINGS_MILESTONE })
    }

    @Test
    fun `no SAVINGS_MILESTONE for BUSINESS userType`() {
        val result = engine.evaluate(
            context(
                userType = UserType.BUSINESS,
                totalSessions = 5
            )
        )
        assertTrue(result.none { it.type == NudgeType.SAVINGS_MILESTONE })
    }

    // ── STREAK REWARD ────────────────────────────────────────────

    @Test
    fun `evaluates STREAK_REWARD when 3 consecutive days`() {
        val result = engine.evaluate(context(consecutiveDays = 3))
        assertTrue(result.any { it.type == NudgeType.STREAK_REWARD })
    }

    @Test
    fun `no STREAK_REWARD when less than 3 consecutive days`() {
        val result = engine.evaluate(context(consecutiveDays = 2))
        assertTrue(result.none { it.type == NudgeType.STREAK_REWARD })
    }

    @Test
    fun `STREAK_REWARD has priority 3`() {
        val result = engine.evaluate(context(consecutiveDays = 3))
        val nudge = result.first { it.type == NudgeType.STREAK_REWARD }
        assertEquals(3, nudge.priority)
    }

    @Test
    fun `STREAK_REWARD title changes for 7+ days`() {
        val result5 = engine.evaluate(context(consecutiveDays = 5))
        val result7 = engine.evaluate(context(consecutiveDays = 7))
        val title5 = result5.first { it.type == NudgeType.STREAK_REWARD }.title
        val title7 = result7.first { it.type == NudgeType.STREAK_REWARD }.title
        assertTrue(title7.contains("săptămână"))
        assertTrue(title5 != title7)
    }

    // ── GENERAL ──────────────────────────────────────────────────

    @Test
    fun `returns empty list for perfectly active user`() {
        val result = engine.evaluate(
            context(
                daysSinceLastSession = 0,
                totalSessions = 1,
                abandonCount = 0,
                consecutiveDays = 1
            )
        )
        assertTrue(result.isEmpty())
    }

    @Test
    fun `nudges have non-blank titles and messages`() {
        val result = engine.evaluate(
            context(
                daysSinceLastSession = 7,
                abandonCount = 1,
                recentScreens = listOf("transfer"),
                consecutiveDays = 3
            )
        )
        result.forEach {
            assertTrue(it.title.isNotBlank())
            assertTrue(it.message.isNotBlank())
        }
    }
}
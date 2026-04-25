package com.assetguard.domain.usecase.telemetry

import core.model.telemetry.HeatmapEntry
import core.model.telemetry.HeatmapResponse
import core.repository.TelemetryRepository
import core.usecase.telemetry.GetHeatmapUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class GetHeatmapUseCaseTest {

    private val repo    = mockk<TelemetryRepository>()
    private val useCase = GetHeatmapUseCase(repo)

    private fun fakeHeatmap(entries: List<HeatmapEntry> = emptyList()) =
        HeatmapResponse(totalEvents = 100L, uniqueUsers = 20L, entries = entries)

    @Test
    fun `get heatmap without screen filter returns all data`() = runTest {
        coEvery { repo.getHeatmap(null) } returns fakeHeatmap()

        val result = useCase.execute(null)

        assertEquals(100L, result.totalEvents)
        coVerify { repo.getHeatmap(null) }
    }

    @Test
    fun `get heatmap with screen filter passes filter to repo`() = runTest {
        coEvery { repo.getHeatmap("dashboard") } returns fakeHeatmap()

        useCase.execute("dashboard")

        coVerify { repo.getHeatmap("dashboard") }
    }

    @Test
    fun `heatmap entries are returned correctly`() = runTest {
        val entries = listOf(
            HeatmapEntry("dashboard", "balance_card", 50, 10, 5, 200.0, 0.2),
            HeatmapEntry("transfer", "confirm_btn",  30, 15, 8, 500.0, 0.5)
        )
        coEvery { repo.getHeatmap(null) } returns fakeHeatmap(entries)

        val result = useCase.execute(null)

        assertEquals(2, result.entries.size)
        assertEquals("dashboard", result.entries[0].screen)
        assertEquals(0.5, result.entries[1].dropOffRate)
    }
}
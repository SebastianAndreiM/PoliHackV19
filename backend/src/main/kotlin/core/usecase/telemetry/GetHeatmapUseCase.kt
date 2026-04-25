package core.usecase.telemetry

import core.model.telemetry.HeatmapResponse
import core.repository.TelemetryRepository

class GetHeatmapUseCase(private val repo: TelemetryRepository) {
    suspend fun execute(screen: String? = null): HeatmapResponse =
        repo.getHeatmap(screen)
}
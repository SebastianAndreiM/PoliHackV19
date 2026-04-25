package presentation.dto.telemetry

import kotlinx.serialization.Serializable

@Serializable
data class HeatmapResponseDto(
    val totalEvents: Long,
    val uniqueUsers: Long,
    val entries: List<HeatmapEntryDto>
)
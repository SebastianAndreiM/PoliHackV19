package core.model.telemetry

data class HeatmapResponse(
    val totalEvents: Long,
    val uniqueUsers: Long,
    val entries: List<HeatmapEntry>
)
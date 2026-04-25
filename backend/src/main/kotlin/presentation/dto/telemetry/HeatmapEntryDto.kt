package presentation.dto.telemetry

import kotlinx.serialization.Serializable

@Serializable
data class HeatmapEntryDto(
    val screen: String,
    val componentKey: String,
    val tapCount: Long,
    val abandonCount: Long,
    val hesitateCount: Long,
    val avgDurationMs: Double,
    val dropOffRate: Double
)
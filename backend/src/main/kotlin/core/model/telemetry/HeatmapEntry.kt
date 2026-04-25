package core.model.telemetry

data class HeatmapEntry(
    val screen: String,
    val componentKey: String,
    val tapCount: Long,
    val abandonCount: Long,
    val hesitateCount: Long,
    val avgDurationMs: Double,
    val dropOffRate: Double
)
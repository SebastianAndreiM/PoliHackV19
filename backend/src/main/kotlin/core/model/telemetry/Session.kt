package core.model.telemetry

import java.time.LocalDateTime
import java.util.UUID

data class Session(
    val id: UUID,
    val userId: UUID,
    val deviceInfo: String?,
    val startedAt: LocalDateTime,
    val endedAt: LocalDateTime?
)
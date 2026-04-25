package core.usecase.telemetry

import core.model.telemetry.TelemetryEvent
import core.repository.TelemetryRepository
import java.time.LocalDateTime
import java.util.UUID

class LogEventUseCase(private val repo: TelemetryRepository) {
    suspend fun execute(event: TelemetryEvent): TelemetryEvent =
        repo.saveEvent(event.copy(
            id        = UUID.randomUUID(),
            createdAt = LocalDateTime.now()
        ))
}
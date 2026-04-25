package core.usecase.telemetry

import core.model.telemetry.Session
import core.repository.TelemetryRepository
import java.util.UUID

class EndSessionUseCase(private val repo: TelemetryRepository) {
    suspend fun execute(sessionId: UUID): Session? =
        repo.endSession(sessionId)
}
package core.usecase.telemetry

import core.model.telemetry.Session
import core.repository.TelemetryRepository
import java.time.LocalDateTime
import java.util.UUID

class StartSessionUseCase(private val repo: TelemetryRepository) {
    suspend fun execute(userId: UUID, deviceInfo: String?): Session =
        repo.saveSession(Session(
            id         = UUID.randomUUID(),
            userId     = userId,
            deviceInfo = deviceInfo,
            startedAt  = LocalDateTime.now(),
            endedAt    = null
        ))
}
package core.repository

import core.model.telemetry.HeatmapResponse
import core.model.telemetry.Session
import core.model.telemetry.TelemetryEvent
import java.util.UUID

interface TelemetryRepository {
    suspend fun saveEvent(event: TelemetryEvent): TelemetryEvent
    suspend fun saveSession(session: Session): Session
    suspend fun endSession(sessionId: UUID): Session?
    suspend fun getEventsBySession(sessionId: UUID): List<TelemetryEvent>
    suspend fun getHeatmap(screen: String?): HeatmapResponse
    suspend fun getDropOffRate(): Double
}

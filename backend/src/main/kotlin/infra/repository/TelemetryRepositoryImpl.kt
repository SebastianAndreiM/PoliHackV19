package infra.repository

import core.model.telemetry.EventType
import core.model.telemetry.HeatmapEntry
import core.model.telemetry.HeatmapResponse
import core.model.telemetry.Session
import core.model.telemetry.TelemetryEvent
import core.repository.TelemetryRepository
import infra.db.telemetry.SessionEntity
import infra.db.telemetry.TelemetryEventEntity
import infra.db.telemetry.TelemetryEventsTable
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime
import java.util.UUID

class TelemetryRepositoryImpl : TelemetryRepository {

    override suspend fun saveEvent(event: TelemetryEvent): TelemetryEvent =
        newSuspendedTransaction(Dispatchers.IO) {
            TelemetryEventEntity.new(event.id) {
                sessionId    = event.sessionId
                userId       = event.userId
                eventType    = event.eventType
                screen       = event.screen
                componentKey = event.componentKey
                durationMs   = event.durationMs
                metadata     = Json.encodeToString(event.metadata)
                createdAt    = event.createdAt
            }.toDomain()
        }

    override suspend fun saveSession(session: Session): Session =
        newSuspendedTransaction(Dispatchers.IO) {
            SessionEntity.new(session.id) {
                userId     = session.userId
                deviceInfo = session.deviceInfo
                startedAt  = session.startedAt
                endedAt    = session.endedAt
            }.toDomain()
        }

    override suspend fun endSession(sessionId: UUID): Session? =
        newSuspendedTransaction(Dispatchers.IO) {
            SessionEntity.findById(sessionId)?.apply {
                endedAt = LocalDateTime.now()
            }?.toDomain()
        }

    override suspend fun getEventsBySession(sessionId: UUID): List<TelemetryEvent> =
        newSuspendedTransaction(Dispatchers.IO) {
            TelemetryEventEntity
                .find { TelemetryEventsTable.sessionId eq sessionId }
                .map { it.toDomain() }
        }

    override suspend fun getHeatmap(screen: String?): HeatmapResponse =
        newSuspendedTransaction(Dispatchers.IO) {
            val query = if (screen != null)
                TelemetryEventsTable.select { TelemetryEventsTable.screen eq screen }
            else
                TelemetryEventsTable.selectAll()

            val events = query.toList()
            val totalEvents  = events.size.toLong()
            val uniqueUsers  = events.map { it[TelemetryEventsTable.userId] }.distinct().size.toLong()

            val grouped = events.groupBy {
                it[TelemetryEventsTable.screen] to (it[TelemetryEventsTable.componentKey] ?: "unknown")
            }

            val entries = grouped.map { (key, rows) ->
                val (sc, comp) = key
                val tapCount      = rows.count { it[TelemetryEventsTable.eventType] == EventType.TAP }.toLong()
                val abandonCount  = rows.count { it[TelemetryEventsTable.eventType] == EventType.ABANDON }.toLong()
                val hesitateCount = rows.count { it[TelemetryEventsTable.eventType] == EventType.HESITATE }.toLong()
                val avgDuration   = rows.mapNotNull { it[TelemetryEventsTable.durationMs] }.average().takeIf { !it.isNaN() } ?: 0.0
                val dropOffRate   = if (tapCount > 0) abandonCount.toDouble() / tapCount else 0.0

                HeatmapEntry(
                    screen = sc,
                    componentKey = comp,
                    tapCount = tapCount,
                    abandonCount = abandonCount,
                    hesitateCount = hesitateCount,
                    avgDurationMs = avgDuration,
                    dropOffRate = dropOffRate
                )
            }.sortedByDescending { it.abandonCount }

            HeatmapResponse(totalEvents, uniqueUsers, entries)
        }

    override suspend fun getDropOffRate(): Double =
        newSuspendedTransaction(Dispatchers.IO) {
            val total   = TelemetryEventsTable.selectAll().count()
            val abandon = TelemetryEventsTable
                .select { TelemetryEventsTable.eventType eq EventType.ABANDON }
                .count()
            if (total > 0) abandon.toDouble() / total else 0.0
        }
}
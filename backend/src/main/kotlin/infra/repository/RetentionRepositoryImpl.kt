package infra.repository

import core.model.retention.RetentionContext
import core.model.telemetry.EventType
import core.repository.RetentionRepository
import infra.db.telemetry.SessionsTable
import infra.db.telemetry.TelemetryEventsTable
import infra.db.user.UserEntity
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime
import java.util.UUID

class RetentionRepositoryImpl : RetentionRepository {

    override suspend fun getContext(userId: UUID): RetentionContext? =
        newSuspendedTransaction(Dispatchers.IO) {

            val user = UserEntity.findById(userId) ?: return@newSuspendedTransaction null

            val sessions = SessionsTable
                .select { SessionsTable.userId eq userId }
                .orderBy(SessionsTable.startedAt to SortOrder.DESC)
                .toList()

            val totalSessions = sessions.size.toLong()

            val lastSession = sessions.firstOrNull()
            val daysSinceLastSession = if (lastSession != null) {
                java.time.temporal.ChronoUnit.DAYS.between(
                    lastSession[SessionsTable.startedAt],
                    LocalDateTime.now()
                )
            } else 999L

            val abandonCount = TelemetryEventsTable
                .select {
                    (TelemetryEventsTable.userId eq userId) and
                            (TelemetryEventsTable.eventType eq EventType.ABANDON) and
                            (TelemetryEventsTable.createdAt greaterEq LocalDateTime.now().minusHours(24))
                }
                .count()

            val recentScreens = TelemetryEventsTable
                .slice(TelemetryEventsTable.screen)
                .select {
                    (TelemetryEventsTable.userId eq userId) and
                            (TelemetryEventsTable.createdAt greaterEq LocalDateTime.now().minusHours(24))
                }
                .orderBy(TelemetryEventsTable.createdAt to SortOrder.DESC)
                .limit(10)
                .map { it[TelemetryEventsTable.screen] }
                .distinct()

            val consecutiveDays = sessions
                .map { it[SessionsTable.startedAt].toLocalDate() }
                .distinct()
                .sorted()
                .reversed()
                .zipWithNext()
                .takeWhile { (a, b) ->
                    java.time.temporal.ChronoUnit.DAYS.between(b, a) == 1L
                }
                .size
                .toLong() + 1

            RetentionContext(
                userId = userId,
                userType = user.userType,
                daysSinceLastSession = daysSinceLastSession,
                totalSessions = totalSessions,
                abandonCount = abandonCount,
                recentScreens = recentScreens,
                consecutiveDays = consecutiveDays
            )
        }
}
package infra.db.telemetry

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object SessionsTable : UUIDTable("sessions") {
    val userId     = uuid("user_id")
    val deviceInfo = varchar("device_info", 512).nullable()
    val startedAt  = datetime("started_at")
    val endedAt    = datetime("ended_at").nullable()
}

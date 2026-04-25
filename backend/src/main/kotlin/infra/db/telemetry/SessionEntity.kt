package infra.db.telemetry

import core.model.telemetry.Session
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class SessionEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SessionEntity>(SessionsTable)

    var userId     by SessionsTable.userId
    var deviceInfo by SessionsTable.deviceInfo
    var startedAt  by SessionsTable.startedAt
    var endedAt    by SessionsTable.endedAt

    fun toDomain() = Session(
        id = this.id.value,
        userId = userId,
        deviceInfo = deviceInfo,
        startedAt = startedAt,
        endedAt = endedAt
    )
}
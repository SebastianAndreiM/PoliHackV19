package infra.db

import core.model.user.UserProfile
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(UsersTable)

    var externalId by UsersTable.externalId
    var userType   by UsersTable.userType
    var locale     by UsersTable.locale
    var createdAt  by UsersTable.createdAt
    var updatedAt  by UsersTable.updatedAt

    fun toDomain(): UserProfile = UserProfile(
        id         = id.value,
        externalId = externalId,
        userType   = userType,
        locale     = locale,
        createdAt  = createdAt,
        updatedAt  = updatedAt
    )
}
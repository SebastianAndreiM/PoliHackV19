package infra.db.user

import core.model.user.UserType
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object UsersTable : UUIDTable("users") {
    val externalId = varchar("external_id", 128).uniqueIndex()
    val userType   = enumerationByName("user_type", 20, UserType::class)
    val locale     = varchar("locale", 10).default("en")
    val createdAt  = datetime("created_at")
    val updatedAt  = datetime("updated_at")
}
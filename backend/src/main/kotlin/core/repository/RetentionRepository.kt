package core.repository

import core.model.retention.RetentionContext
import java.util.UUID

interface RetentionRepository {
    suspend fun getContext(userId: UUID): RetentionContext?
}
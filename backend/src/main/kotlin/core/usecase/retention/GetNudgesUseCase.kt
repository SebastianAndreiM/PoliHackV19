package core.usecase.retention

import core.model.retention.Nudge
import core.repository.RetentionRepository
import core.service.NudgeEngine
import java.util.UUID

class GetNudgesUseCase(
    private val repo: RetentionRepository,
    private val engine: NudgeEngine
) {
    suspend fun execute(userId: UUID): List<Nudge> {
        val context = repo.getContext(userId) ?: return emptyList()
        return engine.evaluate(context).sortedBy { it.priority }
    }
}
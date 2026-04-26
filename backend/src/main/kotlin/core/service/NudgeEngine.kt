package core.service

import core.model.retention.Nudge
import core.model.retention.RetentionContext

interface NudgeEngine {
    fun evaluate(context: RetentionContext): List<Nudge>
}
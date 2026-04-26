package core.repository

import core.model.ai.IntentResponse
import core.model.user.UserType

interface AIGateway {
    suspend fun parseIntent(
        message: String,
        userType: UserType,
        history: List<Pair<String, String>>
    ): IntentResponse
}
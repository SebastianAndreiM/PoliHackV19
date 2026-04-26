package core.model.ai

data class IntentResponse(
    val intent: String,
    val deepLink: String?,
    val uiHint: String?,
    val reply: String,
    val confidence: Double
)

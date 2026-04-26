package presentation.dto.retention

import kotlinx.serialization.Serializable

@Serializable
data class NudgesResponse(
    val userId: String,
    val nudges: List<NudgeDto>
)
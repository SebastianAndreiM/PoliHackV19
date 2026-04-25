package presentation.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val externalId: String,
    val locale: String = "en"
)
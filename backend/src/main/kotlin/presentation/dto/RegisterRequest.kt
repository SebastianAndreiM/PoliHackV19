package presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val externalId: String,
    val locale: String = "en"
)
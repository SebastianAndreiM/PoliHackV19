package presentation.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val id: String,
    val externalId: String,
    val userType: String,
    val locale: String,
    val createdAt: String
)
package presentation.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserTypeRequest(
    val userType: String
)
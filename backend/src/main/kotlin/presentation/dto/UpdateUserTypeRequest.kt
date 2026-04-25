package presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserTypeRequest(
    val userType: String
)
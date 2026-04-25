package presentation.dto.ui

import kotlinx.serialization.Serializable

@Serializable
data class LayoutResponseDto(
    val userType: String,
    val theme: String,
    val components: List<UIComponentDto>,
    val deepLinks: Map<String, String>
)
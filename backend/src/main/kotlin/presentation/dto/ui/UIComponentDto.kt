package presentation.dto.ui

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class UIComponentDto(
    val key: String,
    val order: Int,
    val visible: Boolean,
    val config: JsonObject
)
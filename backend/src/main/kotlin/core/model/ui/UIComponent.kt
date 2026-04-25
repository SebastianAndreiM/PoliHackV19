package core.model.ui

import kotlinx.serialization.json.JsonObject

data class UIComponent(
    val key: String,
    val order: Int,
    val visible: Boolean,
    val config: JsonObject
)

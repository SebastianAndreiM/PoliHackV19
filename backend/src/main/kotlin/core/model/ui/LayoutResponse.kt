package core.model.ui

data class LayoutResponse(
    val userType: String,
    val theme: String,
    val components: List<UIComponent>,
    val deepLinks: Map<String, String>
)

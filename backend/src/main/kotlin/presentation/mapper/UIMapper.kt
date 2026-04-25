package presentation.mapper

import core.model.ui.LayoutResponse
import presentation.dto.ui.LayoutResponseDto
import presentation.dto.ui.UIComponentDto

fun LayoutResponse.toDto() = LayoutResponseDto(
    userType = userType,
    theme = theme,
    components = components.map {
        UIComponentDto(it.key, it.order, it.visible, it.config)
    },
    deepLinks = deepLinks
)
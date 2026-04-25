package core.usecase.ui

import core.model.ui.LayoutResponse
import core.model.user.UserType
import core.service.AdaptiveLayoutService

class GetAdaptiveLayoutUseCase(
    private val layoutService: AdaptiveLayoutService
) {
    fun execute(userType: UserType): LayoutResponse =
        layoutService.resolveLayout(userType)
}
package core.service

import core.model.ui.LayoutResponse
import core.model.user.UserType

interface AdaptiveLayoutService {
    fun resolveLayout(userType: UserType): LayoutResponse
}
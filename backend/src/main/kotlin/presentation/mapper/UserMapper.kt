package presentation.mapper

import core.model.user.UserProfile
import presentation.dto.ProfileResponse

fun UserProfile.toResponse() = ProfileResponse(
    id = id.toString(),
    externalId = externalId,
    userType = userType.name,
    locale = locale,
    createdAt = createdAt.toString()
)
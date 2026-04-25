package infra.service

import core.model.ui.LayoutResponse
import core.model.ui.UIComponent
import core.model.user.UserType
import core.service.AdaptiveLayoutService
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AdaptiveLayoutServiceImpl : AdaptiveLayoutService {

    override fun resolveLayout(userType: UserType): LayoutResponse =
        when (userType) {
            UserType.BUSINESS -> businessLayout()
            UserType.STUDENT  -> studentLayout()
            UserType.SENIOR   -> seniorLayout()
            UserType.DEFAULT  -> defaultLayout()
        }

    private fun businessLayout() = LayoutResponse(
        userType   = "BUSINESS",
        theme      = "professional",
        components = listOf(
            UIComponent(
                key     = "quick_actions",
                order   = 1,
                visible = true,
                config  = buildJsonObject {
                    put("actions", buildJsonArray {
                        add("send"); add("invoice"); add("fx_rates")
                    })
                }
            ),
            UIComponent(
                key     = "balance_card",
                order   = 2,
                visible = true,
                config  = buildJsonObject { put("showWeeklyDelta", true) }
            ),
            UIComponent(
                key     = "recent_transactions",
                order   = 3,
                visible = true,
                config  = buildJsonObject { put("limit", 10) }
            ),
            UIComponent(
                key     = "fx_rates",
                order   = 4,
                visible = true,
                config  = buildJsonObject { }
            ),
            UIComponent(
                key     = "invoices",
                order   = 5,
                visible = true,
                config  = buildJsonObject { }
            ),
            UIComponent(
                key     = "savings_nudge",
                order   = 6,
                visible = false,
                config  = buildJsonObject { }
            )
        ),
        deepLinks = mapOf(
            "primaryAction"   to "/transfer",
            "secondaryAction" to "/invoice/new"
        )
    )

    private fun studentLayout() = LayoutResponse(
        userType   = "STUDENT",
        theme      = "modern",
        components = listOf(
            UIComponent(
                key     = "balance_card",
                order   = 1,
                visible = true,
                config  = buildJsonObject { put("showWeeklyDelta", false) }
            ),
            UIComponent(
                key     = "quick_actions",
                order   = 2,
                visible = true,
                config  = buildJsonObject {
                    put("actions", buildJsonArray {
                        add("send"); add("request")
                    })
                }
            ),
            UIComponent(
                key     = "savings_nudge",
                order   = 3,
                visible = true,
                config  = buildJsonObject { put("goalLabel", "Vacation fund") }
            ),
            UIComponent(
                key     = "recent_transactions",
                order   = 4,
                visible = true,
                config  = buildJsonObject { put("limit", 5) }
            ),
            UIComponent(
                key     = "cashback_banner",
                order   = 5,
                visible = true,
                config  = buildJsonObject { }
            ),
            UIComponent(
                key     = "fx_rates",
                order   = 6,
                visible = false,
                config  = buildJsonObject { }
            )
        ),
        deepLinks = mapOf(
            "primaryAction"   to "/send",
            "secondaryAction" to "/savings/new"
        )
    )

    private fun seniorLayout() = LayoutResponse(
        userType   = "SENIOR",
        theme      = "accessible",
        components = listOf(
            UIComponent(
                key     = "balance_card",
                order   = 1,
                visible = true,
                config  = buildJsonObject { put("largeText", true) }
            ),
            UIComponent(
                key     = "quick_actions",
                order   = 2,
                visible = true,
                config  = buildJsonObject {
                    put("actions", buildJsonArray {
                        add("send"); add("request")
                    })
                }
            ),
            UIComponent(
                key     = "recent_transactions",
                order   = 3,
                visible = true,
                config  = buildJsonObject {
                    put("limit", 5)
                    put("largeText", true)
                }
            ),
            UIComponent(
                key     = "support_banner",
                order   = 4,
                visible = true,
                config  = buildJsonObject { }
            ),
            UIComponent(
                key     = "fx_rates",
                order   = 5,
                visible = false,
                config  = buildJsonObject { }
            ),
            UIComponent(
                key     = "invoices",
                order   = 6,
                visible = false,
                config  = buildJsonObject { }
            )
        ),
        deepLinks = mapOf(
            "primaryAction"   to "/send",
            "secondaryAction" to "/support"
        )
    )

    private fun defaultLayout() = LayoutResponse(
        userType   = "DEFAULT",
        theme      = "modern",
        components = listOf(
            UIComponent(
                key     = "balance_card",
                order   = 1,
                visible = true,
                config  = buildJsonObject { }
            ),
            UIComponent(
                key     = "quick_actions",
                order   = 2,
                visible = true,
                config  = buildJsonObject {
                    put("actions", buildJsonArray {
                        add("send"); add("request")
                    })
                }
            ),
            UIComponent(
                key     = "recent_transactions",
                order   = 3,
                visible = true,
                config  = buildJsonObject { put("limit", 5) }
            ),
            UIComponent(
                key     = "savings_nudge",
                order   = 4,
                visible = false,
                config  = buildJsonObject { }
            ),
            UIComponent(
                key     = "fx_rates",
                order   = 5,
                visible = false,
                config  = buildJsonObject { }
            )
        ),
        deepLinks = mapOf(
            "primaryAction"   to "/send",
            "secondaryAction" to "/profile"
        )
    )
}
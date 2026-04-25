package com.assetguard.domain.usecase.ui

import core.model.user.UserType
import core.usecase.ui.GetAdaptiveLayoutUseCase
import infra.service.AdaptiveLayoutServiceImpl
import junit.framework.TestCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetAdaptiveLayoutUseCaseTest {

    private val service = AdaptiveLayoutServiceImpl()
    private val useCase = GetAdaptiveLayoutUseCase(service)

    @Test
    fun `BUSINESS layout has correct theme`() {
        val layout = useCase.execute(UserType.BUSINESS)
        assertEquals("professional", layout.theme)
        assertEquals("BUSINESS", layout.userType)
    }

    @Test
    fun `BUSINESS layout contains invoices component`() {
        val layout = useCase.execute(UserType.BUSINESS)
        val keys = layout.components.map { it.key }
        TestCase.assertTrue(keys.contains("invoices"))
        TestCase.assertTrue(keys.contains("fx_rates"))
    }

    @Test
    fun `STUDENT layout has correct theme`() {
        val layout = useCase.execute(UserType.STUDENT)
        assertEquals("modern", layout.theme)
        assertEquals("STUDENT", layout.userType)
    }

    @Test
    fun `STUDENT layout has savings nudge visible`() {
        val layout = useCase.execute(UserType.STUDENT)
        val nudge = layout.components.find { it.key == "savings_nudge" }
        TestCase.assertTrue(nudge?.visible == true)
    }

    @Test
    fun `STUDENT layout has fx_rates hidden`() {
        val layout = useCase.execute(UserType.STUDENT)
        val fx = layout.components.find { it.key == "fx_rates" }
        TestCase.assertTrue(fx?.visible == false)
    }

    @Test
    fun `SENIOR layout has correct theme`() {
        val layout = useCase.execute(UserType.SENIOR)
        assertEquals("accessible", layout.theme)
    }

    @Test
    fun `SENIOR layout has support banner visible`() {
        val layout = useCase.execute(UserType.SENIOR)
        val support = layout.components.find { it.key == "support_banner" }
        TestCase.assertTrue(support?.visible == true)
    }

    @Test
    fun `DEFAULT layout is returned for DEFAULT user type`() {
        val layout = useCase.execute(UserType.DEFAULT)
        assertEquals("DEFAULT", layout.userType)
        assertEquals("modern", layout.theme)
    }

    @Test
    fun `all layouts have balance_card as visible`() {
        UserType.entries.forEach { type ->
            val layout = useCase.execute(type)
            val card = layout.components.find { it.key == "balance_card" }
            assertTrue(card?.visible == true, "balance_card should be visible for $type")
        }
    }

    @Test
    fun `all layouts have components sorted by order`() {
        UserType.entries.forEach { type ->
            val layout = useCase.execute(type)
            val orders = layout.components.map { it.order }
            assertEquals(orders.sorted(), orders, "Components not sorted for $type")
        }
    }

    @Test
    fun `all layouts have primaryAction deepLink`() {
        UserType.entries.forEach { type ->
            val layout = useCase.execute(type)
            assertTrue(layout.deepLinks.containsKey("primaryAction"), "Missing primaryAction for $type")
        }
    }

    @Test
    fun `BUSINESS primaryAction is transfer`() {
        val layout = useCase.execute(UserType.BUSINESS)
        assertEquals("/transfer", layout.deepLinks["primaryAction"])
    }
}
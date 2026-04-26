package infra.service

import core.model.ai.IntentResponse
import core.model.user.UserType
import core.repository.AIGateway

class MockAIGateway : AIGateway {

    override suspend fun parseIntent(
        message: String,
        userType: UserType,
        history: List<Pair<String, String>>
    ): IntentResponse {
        val lower = message.lowercase()

        return when {
            lower.containsAny("transfer", "trimite", "send", "plateste", "plata", "pay") ->
                IntentResponse(
                    intent     = "send_money",
                    deepLink   = "/transfer",
                    uiHint     = "highlight_send_button",
                    reply      = replyFor(userType,
                        student  = "Sigur! Apasă pe Send să trimiți bani 💸",
                        business = "Inițiez procesul de transfer. Accesați secțiunea Transfer.",
                        senior   = "Apăsați butonul Send pentru a trimite bani.",
                        default  = "Redirecționez către Transfer."
                    ),
                    confidence = 0.93
                )

            lower.containsAny("sold", "balance", "cont", "bani", "cat am") ->
                IntentResponse(
                    intent     = "check_balance",
                    deepLink   = "/balance",
                    uiHint     = "show_balance_card",
                    reply      = replyFor(userType,
                        student  = "Soldul tău e pe ecranul principal 👀",
                        business = "Soldul curent este disponibil în dashboard.",
                        senior   = "Puteți vedea soldul pe pagina principală.",
                        default  = "Verificați soldul pe ecranul principal."
                    ),
                    confidence = 0.95
                )

            lower.containsAny("factura", "invoice", "facturare") ->
                IntentResponse(
                    intent     = "create_invoice",
                    deepLink   = "/invoice/new",
                    uiHint     = "open_invoice_form",
                    reply      = replyFor(userType,
                        student  = "Hmm, facturi? Încearcă secțiunea Invoice! 📄",
                        business = "Accesați modulul de facturare pentru a crea o factură nouă.",
                        senior   = "Mergeți la secțiunea Facturi pentru a crea una nouă.",
                        default  = "Redirecționez către facturare."
                    ),
                    confidence = 0.91
                )

            lower.containsAny("tranzactii", "transactions", "istoric", "history", "cheltuieli") ->
                IntentResponse(
                    intent     = "view_transactions",
                    deepLink   = "/transactions",
                    uiHint     = "show_transactions_list",
                    reply      = replyFor(userType,
                        student  = "Istoricul tranzacțiilor e la un tap distanță 📋",
                        business = "Istoricul complet al tranzacțiilor este disponibil.",
                        senior   = "Apăsați pe Tranzacții pentru a vedea istoricul.",
                        default  = "Redirecționez către tranzacții."
                    ),
                    confidence = 0.92
                )

            lower.containsAny("economii", "savings", "economisesc", "economisire", "obiectiv") ->
                IntentResponse(
                    intent     = "view_savings",
                    deepLink   = "/savings",
                    uiHint     = "show_savings_card",
                    reply      = replyFor(userType,
                        student  = "Economisești? Super! Iată obiectivele tale 🎯",
                        business = "Consultați planurile de economisire disponibile.",
                        senior   = "Secțiunea Economii vă arată progresul.",
                        default  = "Redirecționez către economii."
                    ),
                    confidence = 0.89
                )

            lower.containsAny("cashback", "reward", "puncte", "bonus") ->
                IntentResponse(
                    intent     = "view_cashback",
                    deepLink   = "/cashback",
                    uiHint     = "show_cashback_banner",
                    reply      = replyFor(userType,
                        student  = "Ai cashback disponibil! 🎉 Verifică recompensele tale.",
                        business = "Consultați programul de recompense pentru detalii.",
                        senior   = "Aveți recompense disponibile. Apăsați aici pentru detalii.",
                        default  = "Redirecționez către cashback."
                    ),
                    confidence = 0.88
                )

            lower.containsAny("curs", "valutar", "fx", "euro", "dolar", "schimb") ->
                IntentResponse(
                    intent     = "view_fx_rates",
                    deepLink   = "/fx-rates",
                    uiHint     = "show_fx_rates",
                    reply      = replyFor(userType,
                        student  = "Cursul valutar e în secțiunea FX Rates 💱",
                        business = "Consultați cursurile valutare actuale în modulul FX.",
                        senior   = "Cursul valutar se află în secțiunea Schimb Valutar.",
                        default  = "Redirecționez către cursuri valutare."
                    ),
                    confidence = 0.90
                )

            lower.containsAny("ajutor", "help", "support", "problema", "eroare", "nu merge") ->
                IntentResponse(
                    intent     = "open_support",
                    deepLink   = "/support",
                    uiHint     = "show_support_banner",
                    reply      = replyFor(userType,
                        student  = "Suntem aici să ajutăm! 🙋 Deschid suportul.",
                        business = "Conectez cu echipa de suport dedicată.",
                        senior   = "Vă conectez cu un specialist. Nu vă faceți griji.",
                        default  = "Redirecționez către suport."
                    ),
                    confidence = 0.96
                )

            lower.containsAny("profil", "profile", "setari", "settings", "cont", "date personale") ->
                IntentResponse(
                    intent     = "view_profile",
                    deepLink   = "/profile",
                    uiHint     = "open_profile_menu",
                    reply      = replyFor(userType,
                        student  = "Profilul tău e aici! 👤",
                        business = "Accesați setările contului din meniu.",
                        senior   = "Profilul dumneavoastră se află în meniu.",
                        default  = "Redirecționez către profil."
                    ),
                    confidence = 0.87
                )

            lower.containsAny("card", "blocat", "pierdut", "furat", "block") ->
                IntentResponse(
                    intent     = "block_card",
                    deepLink   = "/card/block",
                    uiHint     = "show_card_options",
                    reply      = replyFor(userType,
                        student  = "Îți blochez cardul imediat! 🔒 Siguranța pe primul loc.",
                        business = "Inițiez procedura de blocare a cardului.",
                        senior   = "Vă blochez cardul acum pentru siguranță.",
                        default  = "Redirecționez către gestionarea cardului."
                    ),
                    confidence = 0.97
                )

            else ->
                IntentResponse(
                    intent = "general_inquiry",
                    deepLink = null,
                    uiHint = null,
                    reply = replyFor(
                        userType,
                        student = "Hmm, nu am înțeles exact. Poți reformula? 🤔",
                        business = "Vă rog să detaliați solicitarea pentru a putea asista.",
                        senior = "Nu am înțeles bine. Puteți reformula, vă rog?",
                        default = "Încearcă să reformulezi întrebarea."
                    ),
                    confidence = 0.4
                )
        }
    }

    private fun String.containsAny(vararg keywords: String): Boolean =
        keywords.any { this.contains(it) }

    private fun replyFor(
        userType: UserType,
        student: String,
        business: String,
        senior: String,
        default: String
    ) = when (userType) {
        UserType.STUDENT  -> student
        UserType.BUSINESS -> business
        UserType.SENIOR   -> senior
        UserType.DEFAULT  -> default
    }
}
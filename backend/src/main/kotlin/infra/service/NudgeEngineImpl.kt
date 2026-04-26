package infra.service

import core.model.retention.Nudge
import core.model.retention.NudgeType
import core.model.retention.RetentionContext
import core.model.user.UserType
import core.service.NudgeEngine
import java.time.LocalDateTime
import java.util.UUID

class NudgeEngineImpl : NudgeEngine {

    override fun evaluate(context: RetentionContext): List<Nudge> {
        val nudges = mutableListOf<Nudge>()

        if (context.daysSinceLastSession >= 7) {
            nudges.add(
                Nudge(
                    id = UUID.randomUUID(),
                    userId = context.userId,
                    type = NudgeType.INACTIVITY,
                    title = inactivityTitle(context.userType),
                    message = inactivityMessage(context.userType, context.daysSinceLastSession),
                    deepLink = "/home",
                    priority = 1,
                    expiresAt = LocalDateTime.now().plusDays(1),
                    createdAt = LocalDateTime.now()
                )
            )
        } else if (context.daysSinceLastSession >= 3) {
            nudges.add(
                Nudge(
                    id = UUID.randomUUID(),
                    userId = context.userId,
                    type = NudgeType.INACTIVITY,
                    title = "Ne-ai lipsit! 👋",
                    message = "Nu te-am văzut de ${context.daysSinceLastSession} zile. Ce mai faci?",
                    deepLink = "/home",
                    priority = 2,
                    expiresAt = LocalDateTime.now().plusDays(2),
                    createdAt = LocalDateTime.now()
                )
            )
        }

        val sessionsToNextReward = 5 - (context.totalSessions % 5)
        if (context.totalSessions >= 1 && sessionsToNextReward in 1..2) {
            nudges.add(Nudge(
                id        = UUID.randomUUID(),
                userId    = context.userId,
                type      = NudgeType.CASHBACK_PROXIMITY,
                title     = "Cashback aproape! 🎁",
                message   = cashbackMessage(context.userType, sessionsToNextReward),
                deepLink  = "/cashback",
                priority  = 2,
                expiresAt = LocalDateTime.now().plusDays(3),
                createdAt = LocalDateTime.now()
            ))
        }

        if (context.abandonCount > 0 &&
            context.recentScreens.contains("transfer")
        ) {
            nudges.add(
                Nudge(
                    id = UUID.randomUUID(),
                    userId = context.userId,
                    type = NudgeType.ABANDON_RECOVERY,
                    title = abandonTitle(context.userType),
                    message = abandonMessage(context.userType),
                    deepLink = "/transfer",
                    priority = 1,
                    expiresAt = LocalDateTime.now().plusHours(6),
                    createdAt = LocalDateTime.now()
                )
            )
        }

        if (context.userType == UserType.STUDENT &&
            context.totalSessions >= 5
        ) {
            nudges.add(
                Nudge(
                    id = UUID.randomUUID(),
                    userId = context.userId,
                    type = NudgeType.SAVINGS_MILESTONE,
                    title = "Obiectivul tău e aproape! 🎯",
                    message = "Ești la 80% din obiectivul de economii. Continuă!",
                    deepLink = "/savings",
                    priority = 2,
                    expiresAt = LocalDateTime.now().plusDays(7),
                    createdAt = LocalDateTime.now()
                )
            )
        }

        if (context.consecutiveDays >= 3) {
            nudges.add(
                Nudge(
                    id = UUID.randomUUID(),
                    userId = context.userId,
                    type = NudgeType.STREAK_REWARD,
                    title = streakTitle(context.consecutiveDays),
                    message = streakMessage(context.userType, context.consecutiveDays),
                    deepLink = "/rewards",
                    priority = 3,
                    expiresAt = LocalDateTime.now().plusDays(1),
                    createdAt = LocalDateTime.now()
                )
            )
        }

        return nudges
    }


    private fun inactivityTitle(userType: UserType) = when (userType) {
        UserType.STUDENT -> "Hei, unde ai dispărut? 👀"
        UserType.BUSINESS -> "Activitate redusă detectată"
        UserType.SENIOR -> "Vă așteptăm înapoi!"
        UserType.DEFAULT -> "Ne-ai lipsit!"
    }

    private fun inactivityMessage(userType: UserType, days: Long) = when (userType) {
        UserType.STUDENT -> "Ai lipsit $days zile! Verifică ce s-a mai întâmplat în contul tău 🔍"
        UserType.BUSINESS -> "Nu ați accesat aplicația de $days zile. Verificați situația contului."
        UserType.SENIOR -> "Nu v-ați conectat de $days zile. Suntem aici dacă aveți nevoie de ajutor."
        UserType.DEFAULT -> "Nu te-am văzut de $days zile. Verifică-ți contul."
    }

    private fun cashbackMessage(userType: UserType, remaining: Long) = when (userType) {
        UserType.STUDENT -> "Mai ai $remaining acțiuni până la cashback! Hai să le facem 💰"
        UserType.BUSINESS -> "La $remaining tranzacții distanță de bonusul de cashback."
        UserType.SENIOR -> "Mai aveți $remaining pași până la recompensa dvs."
        UserType.DEFAULT -> "Ești la $remaining acțiuni de cashback."
    }

    private fun abandonTitle(userType: UserType) = when (userType) {
        UserType.STUDENT -> "Ai uitat ceva? 🤔"
        UserType.BUSINESS -> "Transfer incomplet"
        UserType.SENIOR -> "Ați uitat un transfer?"
        UserType.DEFAULT -> "Transfer nefinalizat"
    }

    private fun abandonMessage(userType: UserType) = when (userType) {
        UserType.STUDENT -> "Ai început un transfer dar nu l-ai terminat. Vrei să continui? 💸"
        UserType.BUSINESS -> "Aveți un transfer inițiat nefinalizat. Doriți să continuați?"
        UserType.SENIOR -> "Ați început să trimiteți bani dar nu ați terminat. Apăsați pentru a continua."
        UserType.DEFAULT -> "Ai un transfer nefinalizat. Continuă de unde ai rămas."
    }

    private fun streakTitle(days: Long) = when {
        days >= 7 -> "O săptămână consecutivă! 🔥"
        days >= 5 -> "$days zile la rând! 🌟"
        else -> "$days zile consecutive! ⚡"
    }

    private fun streakMessage(userType: UserType, days: Long) = when (userType) {
        UserType.STUDENT -> "Ești activ de $days zile la rând! Keep it up! 🚀"
        UserType.BUSINESS -> "Activitate consecventă de $days zile. Excelent management financiar."
        UserType.SENIOR -> "Felicitări! Folosiți aplicația de $days zile consecutiv."
        UserType.DEFAULT -> "Ești activ de $days zile consecutiv. Continuă!"
    }
}
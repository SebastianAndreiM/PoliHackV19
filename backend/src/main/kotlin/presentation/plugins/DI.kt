package presentation.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import presentation.di.adaptiveUiModule
import presentation.di.aiAssistantModule
import presentation.di.telemetryModule
import presentation.di.userModule


fun Application.configureDI() {
    val apiKey = environment.config
        .propertyOrNull("ai.anthropic.apiKey")
        ?.getString()
        ?: System.getenv("ANTHROPIC_API_KEY")
        ?: error("ANTHROPIC_API_KEY not configured")

    install(Koin) {
        slf4jLogger()
        modules(userModule, adaptiveUiModule, telemetryModule, aiAssistantModule(apiKey))
    }
}

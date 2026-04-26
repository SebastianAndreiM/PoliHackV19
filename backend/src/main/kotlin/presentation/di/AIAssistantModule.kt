package presentation.di

import core.repository.AIGateway
import core.repository.ChatRepository
import core.usecase.ai.GetChatHistoryUseCase
import core.usecase.ai.SendMessageUseCase
import infra.repository.ChatRepositoryImpl
import infra.service.GeminiGateway
import infra.service.MockAIGateway
import org.koin.dsl.module

fun aiAssistantModule(apiKey: String) = module {
    single<ChatRepository> { ChatRepositoryImpl() }
    single<AIGateway> {
        if (apiKey.isNotBlank()) {
            println("Using GeminiGateway")
            MockAIGateway()
        } else {
            println("Using MockAIGateway")
            GeminiGateway(apiKey = apiKey)
        }
    }
    factory { SendMessageUseCase(get(), get()) }
    factory { GetChatHistoryUseCase(get()) }
}
package presentation.di

import core.repository.AIGateway
import core.repository.ChatRepository
import core.usecase.ai.GetChatHistoryUseCase
import core.usecase.ai.SendMessageUseCase
import infra.repository.ChatRepositoryImpl
import infra.service.MockAIGateway
import org.koin.dsl.module

fun aiAssistantModule() = module {
    single<ChatRepository> { ChatRepositoryImpl() }
    single<AIGateway> { MockAIGateway() }
    factory { SendMessageUseCase(get(), get()) }
    factory { GetChatHistoryUseCase(get()) }
}
package presentation.di

import core.service.AdaptiveLayoutService
import core.usecase.ui.GetAdaptiveLayoutUseCase
import infra.service.AdaptiveLayoutServiceImpl
import org.koin.dsl.module

val adaptiveUiModule = module {
    single<AdaptiveLayoutService> { AdaptiveLayoutServiceImpl() }
    factory { GetAdaptiveLayoutUseCase(get()) }
}
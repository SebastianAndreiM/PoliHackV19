package presentation.di

import core.repository.RetentionRepository
import core.service.NudgeEngine
import core.usecase.retention.GetNudgesUseCase
import infra.repository.RetentionRepositoryImpl
import infra.service.NudgeEngineImpl
import org.koin.dsl.module


val retentionModule = module {
    single<RetentionRepository> { RetentionRepositoryImpl() }
    single<NudgeEngine> { NudgeEngineImpl() }
    factory { GetNudgesUseCase(get(), get()) }
}
package presentation.di

import core.repository.TelemetryRepository
import core.usecase.telemetry.EndSessionUseCase
import core.usecase.telemetry.GetHeatmapUseCase
import core.usecase.telemetry.LogEventUseCase
import core.usecase.telemetry.StartSessionUseCase
import infra.repository.TelemetryRepositoryImpl
import org.koin.dsl.module

val telemetryModule = module {
    single<TelemetryRepository> { TelemetryRepositoryImpl() }
    factory { LogEventUseCase(get()) }
    factory { StartSessionUseCase(get()) }
    factory { EndSessionUseCase(get()) }
    factory { GetHeatmapUseCase(get()) }
}
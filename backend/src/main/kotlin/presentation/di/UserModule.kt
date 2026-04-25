package presentation.di

import core.repository.UserRepository
import core.usecase.GetProfileUseCase
import core.usecase.RegisterUserUseCase
import core.usecase.UpdateUserTypeUseCase
import infra.repository.UserRepositoryImpl
import org.koin.dsl.module


val userModule = module {

    single<UserRepository> { UserRepositoryImpl() }

    factory { RegisterUserUseCase(get()) }
    factory { GetProfileUseCase(get()) }
    factory { UpdateUserTypeUseCase(get()) }
}
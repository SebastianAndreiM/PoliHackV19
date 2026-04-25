package presentation.di

import core.repository.UserRepository
import core.usecase.user.GetProfileUseCase
import core.usecase.user.RegisterUserUseCase
import core.usecase.user.UpdateUserTypeUseCase
import infra.repository.UserRepositoryImpl
import org.koin.dsl.module


val userModule = module {

    single<UserRepository> { UserRepositoryImpl() }

    factory { RegisterUserUseCase(get()) }
    factory { GetProfileUseCase(get()) }
    factory { UpdateUserTypeUseCase(get()) }
}
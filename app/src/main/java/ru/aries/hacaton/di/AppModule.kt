package ru.aries.hacaton.di

import org.koin.dsl.module
import ru.aries.hacaton.data.api_client.ApiSignIn
import ru.aries.hacaton.data.api_client.Client
import ru.aries.hacaton.data.data_store.DataStorePrefs
import ru.aries.hacaton.screens.module_authorization.AuthModel
import ru.aries.hacaton.screens.splash.SplashModel
import ru.aries.hacaton.use_case.UseCaseSignIn


val setMainModule = module {
    single { DataStorePrefs(get()) }
    single { Client(get()) }
}

val setApiRoute = module {
    factory { ApiSignIn(get()) }
}

val setUseCase = module {
    factory { UseCaseSignIn(get(), get()) }
}

val setModels = module {
    factory { AuthModel(get()) }
    factory { SplashModel() }
}
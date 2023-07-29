package ru.aries.hacaton.screens.splash

import cafe.adriel.voyager.core.model.coroutineScope
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.aries.hacaton.base.BaseModel
import ru.aries.hacaton.data.gDSetScreenMain
import ru.aries.hacaton.models.api.GettingUser
import ru.aries.hacaton.models.api.StepNotEnoughReg
import ru.aries.hacaton.screens.module_authorization.AuthScreen
import ru.aries.hacaton.screens.module_main.core_main.HomeMainScreen
import ru.aries.hacaton.screens.module_main.core_main.ScreensHome
import ru.aries.hacaton.screens.module_registration.main.RegMainScreen
import ru.aries.hacaton.use_case.UseCaseLocations
import ru.aries.hacaton.use_case.UseCaseUser

class SplashScreenModel(
    private val apiUser: UseCaseUser,
    private val apiLocations: UseCaseLocations
) : BaseModel() {

    companion object {
        private const val delayMillis = 1200L
    }

    private fun debugTestScreen(): Screen? = null

    fun startApp() = coroutineScope.launch {
        apiLocations.initDdCities {
            coroutineScope.launch {
                debugTestScreen()?.let {
                    delay(delayMillis)
                    navigator.push(it)
                    return@launch
                }

                apiUser.getMe(
                    flowStart = {},
                    flowSuccess = { user ->
                        chooseScreen(user)
                    },
                    flowError = {
                        chooseScreen(null)
                    },
                    flowMessage = {}
                )
            }
        }
    }

    private fun chooseScreen(user: GettingUser?) {
        if (user == null) {
            goToAuth()
            return
        }

        goToMain()
    }

    private fun goToAuth() = coroutineScope.launch {
        delay(delayMillis)
        navigator.push(AuthScreen())
    }

    private fun goToMain() = coroutineScope.launch {
        delay(delayMillis)
        gDSetScreenMain(ScreensHome.RIBBON_SCREEN)
        navigator.push(HomeMainScreen())
    }
}



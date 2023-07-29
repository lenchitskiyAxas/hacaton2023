package ru.aries.hacaton.screens.module_authorization

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.launch
import ru.aries.hacaton.base.BaseModel
import ru.aries.hacaton.data.gDSetLoader
import ru.aries.hacaton.data.gDSetScreenMain
import ru.aries.hacaton.models.api.GettingUser
import ru.aries.hacaton.models.api.StepNotEnoughReg
import ru.aries.hacaton.screens.module_main.core_main.HomeMainScreen
import ru.aries.hacaton.screens.module_main.core_main.ScreensHome
import ru.aries.hacaton.screens.module_registration.main.RegMainScreen
import ru.aries.hacaton.use_case.UseCaseSignIn

class AuthScreenModel(
    private val apiSignIn: UseCaseSignIn
) : BaseModel() {

    fun authorization(email: String, password: String) = coroutineScope.launch {
        apiSignIn.postAuthorization(
            email = email,
            password = password,
            flowMessage = ::message,
            flowStart = {
                gDSetLoader(true)
            },
            flowSuccess = { user->
                chooseScreen(user)
                gDSetLoader(false)
            },
            flowError = {
                gDSetLoader(false)
            }
        )
    }

    private fun chooseScreen(user: GettingUser?) {
        val statusRegistration = user?.stepRegStatus()
        when (user?.stepRegStatus()) {
            StepNotEnoughReg.STEP_SUCCESS -> {
                goToMain()
                return
            }
            else -> {
                goToReg(statusRegistration)
                return
            }
        }
    }

    private fun goToMain(){
        gDSetScreenMain(ScreensHome.RIBBON_SCREEN)
        navigator.push(HomeMainScreen())
    }


    fun goToReg(statusReg: StepNotEnoughReg? = null) {
        navigator.push(RegMainScreen(statusReg))
    }

}
package ru.aries.hacaton.screens.module_authorization

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.launch
import ru.aries.hacaton.base.BaseModel
import ru.aries.hacaton.data.gDSetLoader
import ru.aries.hacaton.screens.module_main.HomeMainScreen
import ru.aries.hacaton.use_case.UseCaseSignIn

class AuthModel(
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
            flowSuccess = { user ->

                goToMain()
                gDSetLoader(false)
            },
            flowError = {
                gDSetLoader(false)
            })
    }

    private fun goToMain() {
        navigator.push(HomeMainScreen())
    }


}
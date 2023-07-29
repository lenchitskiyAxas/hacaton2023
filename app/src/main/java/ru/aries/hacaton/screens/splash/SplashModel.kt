package ru.aries.hacaton.screens.splash

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.aries.hacaton.base.BaseModel
import ru.aries.hacaton.screens.module_authorization.AuthScreen

class SplashModel : BaseModel() {

    companion object {
        private const val delayMillis = 1200L
    }

    fun startApp() = coroutineScope.launch {
        goToAuth()
    }

    private fun goToAuth() = coroutineScope.launch {
        delay(delayMillis)
        navigator.push(AuthScreen())
    }
}



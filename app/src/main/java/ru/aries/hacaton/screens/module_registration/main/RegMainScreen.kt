package ru.aries.hacaton.screens.module_registration.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScaleTransition
import cafe.adriel.voyager.transitions.SlideTransition
import ru.aries.hacaton.base.common_composable.ButtonAccentTextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.models.api.StepNotEnoughReg
import ru.aries.hacaton.screens.module_registration.RegStepsModel
import ru.aries.hacaton.screens.module_registration.step_1.StepOneScreen
import ru.aries.hacaton.screens.module_registration.step_2.StepTwoScreen
import ru.aries.hacaton.screens.splash.SplashScr

class RegMainScreen(
    private val statusReg: StepNotEnoughReg?=null
) : Screen {

    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    override fun Content() {
        SplashScr()
        Navigator(
            onBackPressed = { false },
            screen = statusReg?.getScreen() ?: StepOneScreen()){nav ->
            SlideTransition(nav)
        }
    }
}
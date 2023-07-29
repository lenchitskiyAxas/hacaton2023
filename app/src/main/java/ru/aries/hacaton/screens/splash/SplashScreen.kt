package ru.aries.hacaton.screens.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import kotlinx.coroutines.delay
import ru.aries.hacaton.R
import ru.aries.hacaton.base.common_composable.BoxImageLoad
import ru.aries.hacaton.base.common_composable.LogoRow
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.LifeScreen
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberModel

class SplashScreen : Screen {

    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<SplashScreenModel>()
        SplashScr()
        LifeScreen(onStart = { model.startApp() })
    }
}

@Composable
fun SplashScr() {
    var isAnimatedStart by remember { mutableStateOf(true) }
    val durationMillis = remember { 2000 }

    val imageAlpha: Float by animateFloatAsState(
        targetValue = if (isAnimatedStart) .6f else 1f,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing,
        )
    )
    LaunchedEffect(Unit) {
        while (true) {
            isAnimatedStart = !isAnimatedStart
            delay(durationMillis.toLong())
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(ThemeApp.colors.primary)) {
        LogoRow(alpha = imageAlpha)
    }
}


package ru.aries.hacaton.base.theme

import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import ru.aries.hacaton.base.res.LightPaletteApp
import ru.aries.hacaton.base.res.ShapesApp
import ru.aries.hacaton.base.res.TypographyApp
import ru.aries.hacaton.base.util.rememberState
import ru.aries.hacaton.data.DataSingleLive
import ru.aries.hacaton.data.GlobalDada
import ru.aries.hacaton.data.GlobalDataObserver
import ru.aries.hacaton.data.LocalGlobalData

@Composable
fun ArielTheme(
    colors: ColorsSchemeApp = LightPaletteApp,
    typography: TypographySchemeApp = TypographyApp,
    shape: ShapeSchemeApp = ShapesApp,
    content: @Composable () -> Unit
             ) {
    var globalData by rememberState { GlobalDada.value ?: DataSingleLive() }
    GlobalDataObserver { globalData = it }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            window.statusBarColor = colors.primary.copy(.5f).toArgb()
            window.navigationBarColor = colors.primary.copy(.8f).toArgb()
            WindowCompat.getInsetsController(window,view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window,view).isAppearanceLightNavigationBars = false
        }
    }

    CompositionLocalProvider(
        LocalColorsApp provides colors,
        LocalTypographyApp provides typography,
        LocalShapeApp provides shape,
        LocalGlobalData provides globalData,
        content = content
    )
}
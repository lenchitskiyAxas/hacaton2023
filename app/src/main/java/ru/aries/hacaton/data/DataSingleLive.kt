package ru.aries.hacaton.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import ru.aries.hacaton.base.res.ShapesApp
import ru.aries.hacaton.base.util.EventProject
import ru.aries.hacaton.base.util.SingleLiveEvent

var GlobalDada = SingleLiveEvent<DataSingleLive>()

data class DataSingleLive(
    val isLoad: Boolean = false,
    val messageSnack: EventProject<String?> = EventProject(null),
    val isVisibleNavBottom: Boolean = true,
    val navHeight: Dp = 0.dp,
)


fun gDNavHeight(navHeight: Dp) {
    GlobalDada.value = GlobalDada.value?.copy(navHeight = navHeight)
        ?: DataSingleLive().copy(navHeight = navHeight)
}

fun gDSetVisibleNavBottom(isVisible: Boolean) {
    GlobalDada.value = GlobalDada.value?.copy(isVisibleNavBottom = isVisible)
        ?: DataSingleLive().copy(isVisibleNavBottom = isVisible)
}


fun gDMessage(text: String?) {
    GlobalDada.value =
        GlobalDada.value?.copy(messageSnack = EventProject(text))
            ?: DataSingleLive().copy(messageSnack = EventProject(null))
}
fun gDSetLoader(isLoad: Boolean) {
    GlobalDada.value = GlobalDada.value?.copy(isLoad = isLoad)
        ?: DataSingleLive().copy(isLoad = isLoad)
}


@Composable
fun GlobalDataObserver(observe: (DataSingleLive) -> Unit) {
    DisposableEffect(GlobalDada) {
        val observer = Observer<DataSingleLive> { newValue ->
            observe.invoke(newValue)
        }

        GlobalDada.observeForever(observer)

        onDispose {
            GlobalDada.removeObserver(observer)
        }
    }
}

val LocalGlobalData = staticCompositionLocalOf { DataSingleLive() }




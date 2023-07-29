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
import ru.aries.hacaton.base.util.rememberState
import ru.aries.hacaton.models.api.City
import ru.aries.hacaton.models.local.DataForPostingMedia
import ru.aries.hacaton.screens.module_main.core_main.ScreensHome


var GlobalDada = SingleLiveEvent<DataSingleLive>()

data class DataSingleLive(
    val isLoad: Boolean = false,
    val messageSnack: EventProject<String?> = EventProject(null),
    val isVisibleNavBottom: Boolean = true,
    val navHeight: Dp = 0.dp,
    val screen : ScreensHome = ScreensHome.RIBBON_SCREEN,
    val listCities: List<City> = listOf(),
    val listImageForUpload: List<DataForPostingMedia> = listOf()
)

fun gDSetCities(listCities: List<City>) {
    GlobalDada.value = GlobalDada.value?.copy(listCities = listCities)
        ?: DataSingleLive().copy(listCities = listCities)
}

fun gDSetScreenMain(screen : ScreensHome) {
    GlobalDada.value = GlobalDada.value?.copy(screen = screen)
        ?: DataSingleLive().copy(screen = screen)
}

fun gDGetCities(nameCity: String? = null): List<City> {
    val list = if (nameCity.isNullOrEmpty()) {
        GlobalDada.value?.listCities?.take(30)
    } else {
        GlobalDada.value?.listCities?.filter { it.name.contains(nameCity, true) }
    }
    return list ?: listOf()
}

fun gDGetCity(id: Int): City? {
    return GlobalDada.value?.listCities?.firstOrNull { it.id == id }
}


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

fun gDSetListImage(listImageForUpload: List<DataForPostingMedia>) {
    GlobalDada.value = GlobalDada.value?.copy(listImageForUpload = listImageForUpload)
        ?: DataSingleLive().copy(listImageForUpload = listImageForUpload)
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




package ru.aries.hacaton.screens.module_main.core_main

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import ru.aries.hacaton.R
import ru.aries.hacaton.base.common_composable.BottomNavBar
import ru.aries.hacaton.base.common_composable.DialogBackPressExit
import ru.aries.hacaton.base.common_composable.LevelDestinationBar
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberState
import ru.aries.hacaton.data.LocalGlobalData
import ru.aries.hacaton.data.gDSetScreenMain
import ru.aries.hacaton.screens.module_main.main_affairs.AffairsScreen
import ru.aries.hacaton.screens.module_main.main_calendar.CalendarScreen
import ru.aries.hacaton.screens.module_main.main_chats.ChatsScreen
import ru.aries.hacaton.screens.module_main.main_gifts.GiftsScreen
import ru.aries.hacaton.screens.module_main.main_ribbon.RibbonScreen

class HomeMainScreen() : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        DialogBackPressExit()
        val screen = LocalGlobalData.current.screen
        Navigator(
            onBackPressed = { false },
            disposeBehavior = NavigatorDisposeBehavior(
                disposeNestedNavigators = true,
                disposeSteps = false
            ),
            screen = screen.getScreen()
        ) { nav ->
            LaunchedEffect(key1 = Unit, key2 = screen, block = {
                nav.replaceAll(screen.getScreen())
            })
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .background(ThemeApp.colors.background)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize()
                        .weight(1f)
                ) {
                    CurrentScreen()
//                    SlideTransition(nav)
                }
                BottomNavigationBar()
            }
        }
    }
}

@Composable
private fun BottomNavigationBar() {
    val navigator: Navigator? = LocalNavigator.current
    val isCheck by rememberState(navigator?.lastItem?.key) {
        LIST_BAR_HOME.any {
            it.route == navigator?.lastItem?.key
        }
    }
    BottomNavBar(
        visible = isCheck,
        isTextEnable = true,
        animation = false,
        selectedDestination = navigator?.lastItem?.key ?: "",
        listMenuBar = LIST_BAR_HOME
    ) { replyDestination ->
        val enumScr = ScreensHome.getScreenEnum(replyDestination.route)
        gDSetScreenMain(enumScr)
    }
}

val LIST_BAR_HOME = listOf(
    LevelDestinationBar(
        route = RibbonScreen.keyName,
        iconIdOn = R.raw.ic_tape,
        iconIdOff = R.raw.ic_tape,
        iconText = TextApp.titleRibbon
    ),
    LevelDestinationBar(
        route = AffairsScreen.keyName,
        iconIdOn = R.raw.ic_case,
        iconIdOff = R.raw.ic_case,
        iconText = TextApp.titleAffairs
    ),
    LevelDestinationBar(
        route = ChatsScreen.keyName,
        iconIdOn = R.raw.ic_messages,
        iconIdOff = R.raw.ic_messages,
        iconText = TextApp.titleChats
    ),
    LevelDestinationBar(
        route = CalendarScreen.keyName,
        iconIdOn = R.raw.ic_calendar,
        iconIdOff = R.raw.ic_calendar,
        iconText = TextApp.titleCalendar
    ),
    LevelDestinationBar(
        route = GiftsScreen.keyName,
        iconIdOn = R.raw.ic_gifts,
        iconIdOff = R.raw.ic_gifts,
        iconText = TextApp.titleGifts
    ),
)


enum class ScreensHome {
    RIBBON_SCREEN,
    AFFAIRS_SCREEN,
    CHATS_SCREEN,
    CALENDAR_SCREEN,
    GIFTS_SCREEN;

    companion object {
        fun getScreenEnum(rote: String?) = when (rote) {
            RibbonScreen.keyName   -> RIBBON_SCREEN
            AffairsScreen.keyName  -> AFFAIRS_SCREEN
            ChatsScreen.keyName    -> CHATS_SCREEN
            CalendarScreen.keyName -> CALENDAR_SCREEN
            GiftsScreen.keyName    -> GIFTS_SCREEN
            else                   -> RIBBON_SCREEN
        }
    }

    fun getScreen() = when (this) {
        RIBBON_SCREEN   -> RibbonScreen()
        AFFAIRS_SCREEN  -> AffairsScreen()
        CHATS_SCREEN    -> ChatsScreen()
        CALENDAR_SCREEN -> CalendarScreen()
        GIFTS_SCREEN    -> GiftsScreen()
    }

    fun getRote() = when (this) {
        RIBBON_SCREEN   -> RibbonScreen.keyName
        AFFAIRS_SCREEN  -> AffairsScreen.keyName
        CHATS_SCREEN    -> ChatsScreen.keyName
        CALENDAR_SCREEN -> CalendarScreen.keyName
        GIFTS_SCREEN    -> GiftsScreen.keyName
    }
}



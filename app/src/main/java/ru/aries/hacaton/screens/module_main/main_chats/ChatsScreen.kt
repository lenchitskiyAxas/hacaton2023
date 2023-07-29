package ru.aries.hacaton.screens.module_main.main_chats

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.aries.hacaton.base.common_composable.DialogBackPressExit
import ru.aries.hacaton.base.common_composable.TextTitleMedium
import ru.aries.hacaton.base.util.getQualifiedName

class ChatsScreen : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {

        DialogBackPressExit()
        Box(modifier = Modifier
            .fillMaxSize()) {
            TextTitleMedium(
                modifier = Modifier.align(Alignment.Center),
                text = "ChatsScreen")
        }
    }
}
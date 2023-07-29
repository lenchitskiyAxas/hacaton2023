package ru.aries.hacaton.screens.module_main.main_ribbon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.aries.hacaton.R
import ru.aries.hacaton.base.common_composable.BoxImageLoad
import ru.aries.hacaton.base.common_composable.DialogBackPressExit
import ru.aries.hacaton.base.common_composable.IconApp
import ru.aries.hacaton.base.common_composable.IconButtonApp
import ru.aries.hacaton.base.common_composable.TextTitleMedium
import ru.aries.hacaton.base.extension.clickableRipple
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberImageRaw
import ru.aries.hacaton.base.util.rememberModel

class RibbonScreen : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<RibbonModel>()
        val userData by model.userData.collectAsState()
        DialogBackPressExit()


        StepOneStr(
            avatarUser = userData.avatar,
            nameUser = userData.first_name,
            haveNewNotifications = true,
            onClickProfile = model::goToProfile
        )

    }
}

@Composable
fun StepOneStr(
    avatarUser: String?,
    nameUser: String?,
    haveNewNotifications: Boolean,
    onClickProfile:()->Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
            .systemBarsPadding()
    ) {
        PanelTopRibbon(
            onClickProfile = onClickProfile,
            onClickFilter = {},
            onClickNotifications = {},
            text = nameUser,
            avatar = avatarUser,
            haveNewNotifications = haveNewNotifications

        )
    }
}


@Composable
private fun PanelTopRibbon(
    onClickProfile: () -> Unit,
    onClickFilter: () -> Unit,
    onClickNotifications: () -> Unit,
    text: String?,
    avatar: String?,
    haveNewNotifications: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(DimApp.heightTopNavigationPanel)
            .shadow(elevation = DimApp.shadowElevation)
            .background(ThemeApp.colors.backgroundVariant),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        BoxImageLoad(
            modifier = Modifier
                .padding(DimApp.screenPadding)
                .size(DimApp.iconSizeBig)
                .clip(CircleShape)
                .clickableRipple { onClickProfile.invoke() },
            drawableError = R.drawable.stab_avatar,
            drawablePlaceholder = R.drawable.stab_avatar,
            image = avatar
        )
        TextTitleMedium(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            textAlign = TextAlign.Start,
            text = text ?: ""
        )
        IconButtonApp(
            modifier = Modifier,
            onClick = onClickFilter
        ) {
            IconApp(painter = rememberImageRaw(R.raw.ic_search))
        }
        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(end = DimApp.screenPadding * 0.5f),
            contentAlignment = Alignment.Center
        ) {
            IconButtonApp(
                onClick = onClickNotifications
            ) {
                IconApp(painter = rememberImageRaw(R.raw.ic_notifications))
            }

            if (haveNewNotifications) {
                Box(
                    modifier = Modifier
                        .padding(DimApp.badgePadding)
                        .size(DimApp.badgeLittle)
                        .align(Alignment.TopEnd)
                        .background(
                            color = ThemeApp.colors.primary,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}
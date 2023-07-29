package ru.aries.hacaton.screens.module_registration.step_5_join_cell_2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.aries.hacaton.R
import ru.aries.hacaton.base.common_composable.HyperlinkText
import ru.aries.hacaton.base.common_composable.IconApp
import ru.aries.hacaton.base.common_composable.PanelNavBackTop
import ru.aries.hacaton.base.common_composable.TextTitleLarge
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.BackPressHandler
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberImageRaw
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.screens.module_registration.RegStepsModel

class StepFifthJoinInCellSecond : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<RegStepsModel>()
        val familyRequest by model.familyRequest.collectAsState()
        BackPressHandler(onBackPressed = model::goBackToSplashScreen)
        StepFifthJoinInCellSecondScr(
            onClickBack = model::goBackToSplashScreen,
            idCell = "${familyRequest?.id ?: "-"}",
            headOfTheFamily = familyRequest?.user?.last_name ?: "-",
        )
    }
}


@Composable
private fun StepFifthJoinInCellSecondScr(
    onClickBack: () -> Unit,
    idCell: String,
    headOfTheFamily: String
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.backgroundVariant)
            .systemBarsPadding()
    ) {
        PanelNavBackTop(onClickBack = onClickBack)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = DimApp.screenPadding)
        ) {

            Column(modifier = Modifier.fillMaxWidth()) {
                TextTitleLarge(text = TextApp.textWelcomeFamilyVibe)
                HyperlinkText(
                    fullText = TextApp.formatSentARequestToJoin(headOfTheFamily, idCell),
                    hyperLinks = listOf(headOfTheFamily, idCell)
                ) { item, index ->

                }
            }
            IconApp(
                modifier = Modifier.align(Alignment.Center),
                painter = rememberImageRaw(id = R.raw.ic_time),
                size = DimApp.iconStubBig,
                tint = ThemeApp.colors.containerVariant
            )


        }
    }

}
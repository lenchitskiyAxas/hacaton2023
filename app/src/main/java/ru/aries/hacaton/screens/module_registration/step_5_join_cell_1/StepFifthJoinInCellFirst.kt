package ru.aries.hacaton.screens.module_registration.step_5_join_cell_1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.aries.hacaton.base.common_composable.BoxFillWeight
import ru.aries.hacaton.base.common_composable.BoxSpacer
import ru.aries.hacaton.base.common_composable.ButtonAccentApp
import ru.aries.hacaton.base.common_composable.PanelBottom
import ru.aries.hacaton.base.common_composable.PanelNavBackTop
import ru.aries.hacaton.base.common_composable.TextBodyMedium
import ru.aries.hacaton.base.common_composable.TextButtonStyle
import ru.aries.hacaton.base.common_composable.TextFieldOutlinesApp
import ru.aries.hacaton.base.common_composable.TextTitleLarge
import ru.aries.hacaton.base.extension.onlyDigit
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.BackPressHandler
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.base.util.rememberState
import ru.aries.hacaton.screens.module_registration.RegStepsModel

class StepFifthJoinInCellFirst : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<RegStepsModel>()
        BackPressHandler(onBackPressed = model::goBackStack)
        StepFifthJoinInCellFirstScr(
            onClickBack = model::goBackStack,
            onClickNext = model::sendIdCell,
        )
    }
}

@Composable
private fun StepFifthJoinInCellFirstScr(
    onClickBack: () -> Unit,
    onClickNext: (String) -> Unit,
    ) {
    val focusManager = LocalFocusManager.current
    var idCell by rememberState { TextFieldValue() }
    val checkData: Boolean = remember(idCell) { !idCell.text.ifEmpty { null }.isNullOrEmpty() }
    val nextStep = remember(checkData) {
        {
            idCell.text.ifEmpty { null }?.let {
                onClickNext.invoke(it)
            } ?: run {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.backgroundVariant)
            .systemBarsPadding()
            .imePadding()
    ) {
        PanelNavBackTop(onClickBack = onClickBack)
        TextButtonStyle(
            modifier = Modifier.padding(horizontal = DimApp.screenPadding),
            text = TextApp.formatStepFrom(4, 4)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())

        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding)
            ) {
                TextTitleLarge(text = TextApp.textWelcomeFamilyVibe)
                TextBodyMedium(text = TextApp.textEnterFamilyCellID)
                BoxSpacer()
                BoxSpacer()
                TextBodyMedium(text = TextApp.textFamilyCellID)
                BoxSpacer(0.5f)
                TextFieldOutlinesApp(
                    modifier = Modifier.fillMaxWidth(),
                    value = idCell,
                    onValueChange = { idCell = it.copy(text = it.text.onlyDigit()) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (!focusManager.moveFocus(FocusDirection.Down)){
                                focusManager.clearFocus()
                                nextStep.invoke()
                            }
                        }
                    ),
                )
            }
            BoxFillWeight()
            PanelBottom {
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding),
                    enabled = checkData,
                    onClick = nextStep,
                    text = TextApp.titleNext
                )
            }
        }
    }
}
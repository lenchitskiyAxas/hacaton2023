package ru.aries.hacaton.screens.module_main.edit_media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.aries.hacaton.R
import ru.aries.hacaton.base.common_composable.BoxFillWeight
import ru.aries.hacaton.base.common_composable.BoxSpacer
import ru.aries.hacaton.base.common_composable.ButtonAccentApp
import ru.aries.hacaton.base.common_composable.ButtonWeakApp
import ru.aries.hacaton.base.common_composable.DialogDataPickerddmmyyy
import ru.aries.hacaton.base.common_composable.IconApp
import ru.aries.hacaton.base.common_composable.PanelBottom
import ru.aries.hacaton.base.common_composable.PanelNavBackTop
import ru.aries.hacaton.base.common_composable.TextBodyMedium
import ru.aries.hacaton.base.common_composable.TextCaption
import ru.aries.hacaton.base.common_composable.TextFieldOutlinesApp
import ru.aries.hacaton.base.extension.toDateString
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.logD
import ru.aries.hacaton.base.util.rememberImageRaw
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.base.util.rememberState

class EditMediaScreen(
    private val idPhoto: Int,
) : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<EditMediaModel>()
        val media by model.media.collectAsState()

        LifecycleEffect(onStarted = {
            model.getMediaById(idPhoto)
        })
        EditMediaScr(
            description = media?.description,
            customDate = media?.happened,
            address = media?.address,
            onClickBack = model::goBackStack,
            onClickCreate = model::editMedia,
        )
    }
}

@Composable
private fun EditMediaScr(
    description: String?,
    customDate: Long?,
    address: String?,
    onClickBack: () -> Unit,
    onClickCreate: (
        descriptionEnter: String,
        customDateEnter: Long?,
        addressEnter: String?,
    ) -> Unit,
) {
    val descriptionEnter = rememberState(description) { TextFieldValue(text = description ?: "") }
    val customDateEnter = rememberState(customDate) { customDate }
    val addressEnter = rememberState(address) { TextFieldValue(address ?: "") }

    val create = {
        onClickCreate.invoke(
            descriptionEnter.value.text,
            customDateEnter.value,
            addressEnter.value.text
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
            .imePadding()
            .systemBarsPadding()
    ) {
        PanelNavBackTop(
            modifier = Modifier,
            painter = rememberImageRaw(R.raw.ic_close),
            onClickBack = onClickBack,
            text = TextApp.textTitleEditPhoto
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            ContentEditMediaData(
                descriptionEnter = descriptionEnter,
                customDateEnter = customDateEnter,
                addressEnter = addressEnter,
            )
            BoxFillWeight()

            PanelBottom {
                ButtonWeakApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = DimApp.screenPadding),
                    onClick = onClickBack,
                    text = TextApp.titleCancel
                )
                BoxSpacer()
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = DimApp.screenPadding),
                    enabled = true,
                    onClick = create,
                    text = TextApp.holderSave
                )
            }
        }
    }
}

@Composable
private fun ContentEditMediaData(
    descriptionEnter: MutableState<TextFieldValue>,
    customDateEnter: MutableState<Long?>,
    addressEnter: MutableState<TextFieldValue>,
    descriptionLight: Int = 150,
) {

    val focusManager = LocalFocusManager.current
    var focusCustomDate by rememberState { false }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(DimApp.screenPadding)
    ) {
        BoxSpacer()
        TextBodyMedium(text = TextApp.textDescription)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = descriptionEnter.value,
            onValueChange = {
                if (descriptionEnter.value.text.length < descriptionLight) {
                    descriptionEnter.value = it
                }
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (!focusManager.moveFocus(FocusDirection.Down)) {
                        focusManager.clearFocus()
                    }
                }
            ),
        )
        BoxSpacer()
        TextBodyMedium(text = TextApp.textCustomDay)
        BoxSpacer(0.5f)
        Box(
            modifier = Modifier
                .onFocusChanged { focusCustomDate = it.hasFocus },
        ) {
            TextFieldOutlinesApp(
                modifier = Modifier.fillMaxWidth(),
                value = TextFieldValue(customDateEnter.value?.toDateString() ?: ""),
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                placeholder = {
                    Text(text = TextApp.textNotSpecified)
                },
                trailingIcon = {
                    IconButton(onClick = { focusCustomDate = true }) {
                        IconApp(painter = rememberImageRaw(id = R.raw.ic_drop))
                    }
                },
            )
        }
        BoxSpacer()
        TextBodyMedium(text = TextApp.textAddressOrPlace)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = addressEnter.value,
            onValueChange = { addressEnter.value = it },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (!focusManager.moveFocus(FocusDirection.Down)) {
                        focusManager.clearFocus()
                    }
                }
            ),
        )
        BoxSpacer()
    }

    if (focusCustomDate) {
        DialogDataPickerddmmyyy(
            initTime = customDateEnter.value,
            onDismissDialog = {
                focusCustomDate = false
                if (!focusManager.moveFocus(FocusDirection.Down)) {
                    focusManager.clearFocus()
                }
            },
            onDataMillisSet = { customDateEnter.value = it },
        )
    }
}
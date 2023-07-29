package ru.aries.hacaton.screens.module_main.profile_redaction

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import ru.aries.hacaton.base.common_composable.DropMenuCities
import ru.aries.hacaton.base.common_composable.PanelBottom
import ru.aries.hacaton.base.common_composable.PanelNavBackTop
import ru.aries.hacaton.base.common_composable.TextBodyMedium
import ru.aries.hacaton.base.common_composable.TextFieldOutlinesApp
import ru.aries.hacaton.base.extension.formattedNumberPhoneRuNoSeven
import ru.aries.hacaton.base.extension.getFormattedNumberRuNoSeven
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.BackPressHandler
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.base.util.rememberState
import ru.aries.hacaton.models.api.City

class ProfileRedactionContactsScreen : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<ProfileRedactionModel>()
        val userData by model.userData.collectAsState()
        val locationBirth by model.locationBirth.collectAsState()
        val locationResidence by model.locationResidence.collectAsState()
        val location by model.location.collectAsState()

        BackPressHandler(onBackPressed = model::goBackStack)
        ProfileRedactionContactsScr(
            onClickBack = model::goBackStack,
            locationBirth = locationBirth,
            locationResidence = locationResidence,
            tg = userData.tg,
            tel = userData.tel,
            onClickSave = model::saveContacts,
            locations = location,
            onSearchLocation = model::onSearchLocation,
        )
    }
}

@Composable
private fun ProfileRedactionContactsScr(
    onClickBack: () -> Unit,
    onClickSave: (
        locationBirth: City?,
        locationResidence: City?,
        tg: String?,
        tel: String?,
    ) -> Unit,
    onSearchLocation: (String?) -> Unit,
    locationBirth: City?,
    locationResidence: City?,
    tg: String?,
    tel: String?,
    locations: List<City>,
) {

    val locationResidenceEnter = rememberState(locationResidence) { locationResidence }
    val locationBirthEnter = rememberState(locationBirth) { locationBirth }
    val tgEnter = rememberState(tg) { TextFieldValue(tg ?: "") }
    val telEnter = rememberState(tel) { TextFieldValue(tel?.formattedNumberPhoneRuNoSeven() ?: "") }

    val checkData: Boolean = remember(
        locationResidenceEnter.value,
        locationBirthEnter.value,
        tgEnter.value,
        telEnter.value,
    ) {
        locationResidenceEnter.value?.id != locationResidence?.id ||
                locationBirthEnter.value?.id != locationBirth?.id ||
                tgEnter.value.text.ifEmpty { null } != tg ||
                telEnter.value.text.ifEmpty { null } != tel
    }

    val onSave = remember(checkData) {
        {
            if (checkData) onClickSave.invoke(
                locationBirthEnter.value,
                locationResidenceEnter.value,
                tgEnter.value.text,
                telEnter.value.text,
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.backgroundVariant)
            .systemBarsPadding()
            .imePadding()
    ) {
        PanelNavBackTop(
            modifier = Modifier.shadow(DimApp.shadowElevation),
            onClickBack = onClickBack,
            text = TextApp.holderContacts
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            ColumnContactsData(
                locationBirthEnter = locationBirthEnter,
                locationResidenceEnter = locationResidenceEnter,
                tgEnter = tgEnter,
                telEnter = telEnter,
                locations = locations,
                onSearchLocation = onSearchLocation
            )
            BoxFillWeight()
            PanelBottom {
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding),
                    enabled = checkData,
                    onClick = onSave,
                    text = TextApp.holderSave
                )
            }
        }
    }
}

@Composable
private fun ColumnContactsData(
    locationBirthEnter: MutableState<City?>,
    locationResidenceEnter: MutableState<City?>,
    tgEnter: MutableState<TextFieldValue>,
    telEnter: MutableState<TextFieldValue>,
    locations: List<City>,
    onSearchLocation: (String?) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(DimApp.screenPadding)
    ) {
        BoxSpacer()
        TextBodyMedium(text = TextApp.textCityOfBirth)
        BoxSpacer(0.5f)
        DropMenuCities(
            items = locations,
            modifier = Modifier.fillMaxWidth(),
            enterNameLocation = onSearchLocation,
            onClickClear = {
                locationBirthEnter.value = null
                onSearchLocation.invoke(null)
            },
            checkItem = {
                locationBirthEnter.value = it
                onSearchLocation.invoke(null)
            },
            locationChooser = locationBirthEnter.value?.name
        )
        BoxSpacer()
        TextBodyMedium(text = TextApp.textCityOfResidence)
        BoxSpacer(0.5f)
        DropMenuCities(
            items = locations,
            modifier = Modifier.fillMaxWidth(),
            enterNameLocation = onSearchLocation,
            onClickClear = {
                locationResidenceEnter.value = null
                onSearchLocation.invoke(null)
            },
            checkItem = {
                locationResidenceEnter.value = it
                onSearchLocation.invoke(null)
            },
            locationChooser = locationResidenceEnter.value?.name
        )

        BoxSpacer()
        TextBodyMedium(text = TextApp.textMobilePhone)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = telEnter.value,
            onValueChange = { telEnter.value = it.getFormattedNumberRuNoSeven() },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
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
        TextBodyMedium(text = TextApp.textTelegram)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = tgEnter.value,
            onValueChange = { tgEnter.value = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
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
}
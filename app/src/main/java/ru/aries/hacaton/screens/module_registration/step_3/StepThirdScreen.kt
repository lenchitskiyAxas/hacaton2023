package ru.aries.hacaton.screens.module_registration.step_3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.aries.hacaton.R
import ru.aries.hacaton.base.common_composable.BoxFillWeight
import ru.aries.hacaton.base.common_composable.BoxSpacer
import ru.aries.hacaton.base.common_composable.ButtonAccentApp
import ru.aries.hacaton.base.common_composable.DialogDataPickerddmmyyy
import ru.aries.hacaton.base.common_composable.DropMenuCities
import ru.aries.hacaton.base.common_composable.HyperlinkText
import ru.aries.hacaton.base.common_composable.IconApp
import ru.aries.hacaton.base.common_composable.PanelBottom
import ru.aries.hacaton.base.common_composable.PanelNavBackTop
import ru.aries.hacaton.base.common_composable.TextBodyLarge
import ru.aries.hacaton.base.common_composable.TextBodyMedium
import ru.aries.hacaton.base.common_composable.TextButtonStyle
import ru.aries.hacaton.base.common_composable.TextFieldOutlinesApp
import ru.aries.hacaton.base.common_composable.TextTitleLarge
import ru.aries.hacaton.base.extension.toDateString
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.BackPressHandler
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberImageRaw
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.base.util.rememberState
import ru.aries.hacaton.models.api.City
import ru.aries.hacaton.screens.module_registration.RegStepsModel

class StepThirdScreen : Screen {

    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<RegStepsModel>()
        val location by model.location.collectAsState()
        val userData by model.userData.collectAsState()
        val locationBirth by model.locationBirth.collectAsState()
        val locationResidence by model.locationResidence.collectAsState()

        BackPressHandler(onBackPressed = model::goBackStack)
        StepThirdScr(
            onClickBack = model::goBackStack,
            onClickNext = model::updateMyProfileStepThird,
            locations = location,
            onSearchLocation = model::onSearchLocation,
            onClickToAuth = model::goToAuth,
            birthdateMillis = userData.getBirthdateInMillis(),
            locationResidence = locationResidence,
            locationBirth = locationBirth,
        )
    }
}

@Composable
private fun StepThirdScr(
    onClickBack: () -> Unit,
    onClickNext: (
        birthdate: Long,
        residenceLocation: City?,
        birthLocation: City?
    ) -> Unit,
    onClickToAuth: () -> Unit,
    birthdateMillis: Long?,
    locationResidence: City?,
    locationBirth: City?,
    onSearchLocation: (String?) -> Unit,
    locations: List<City>,
) {

    val birthdateEnter = rememberState(birthdateMillis) { birthdateMillis }
    val locationResidenceEnter = rememberState(locationResidence) { locationResidence }
    val locationBirthEnter = rememberState(locationBirth) { locationBirth }

    val checkData: Boolean = remember(birthdateEnter.value) { birthdateEnter.value != null }
    val nextStep = remember(
        checkData,
        locationResidenceEnter.value,
        locationBirthEnter.value) {
        {
            val date = birthdateEnter.value
            if (checkData && date != null) onClickNext.invoke(
                date,
                locationResidenceEnter.value,
                locationBirthEnter.value)
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
            text = TextApp.formatStepFrom(3, 4)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            ColumnContentThird(
                birthdateEnter = birthdateEnter,
                locationResidenceEnter = locationResidenceEnter,
                locationBirthEnter = locationBirthEnter,
                locations = locations,
                onSearchLocation = onSearchLocation,
                onClickToAuth = onClickToAuth,
            )
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

@Composable
private fun ColumnScope.ColumnContentThird(
    birthdateEnter: MutableState<Long?>,
    locationResidenceEnter: MutableState<City?>,
    locationBirthEnter: MutableState<City?>,
    locations: List<City>,
    onSearchLocation: (String?) -> Unit,
    onClickToAuth: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var focusBirthDay by rememberState { false }
    val isErrorBirthDay = remember(birthdateEnter.value) { birthdateEnter.value == null }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(DimApp.screenPadding)
    ) {
        TextTitleLarge(text = TextApp.textCreateAnAccount)
        TextBodyLarge(text = TextApp.textEnterYourDetails)
        BoxSpacer()
        TextBodyMedium(text = TextApp.textBirthDayWye)
        BoxSpacer(0.5f)
        Box(
            modifier = Modifier
                .onFocusChanged { focusBirthDay = it.hasFocus },
        ) {
            TextFieldOutlinesApp(
                modifier = Modifier.fillMaxWidth(),
                value = TextFieldValue(birthdateEnter.value?.toDateString() ?: ""),
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                isError = isErrorBirthDay,
                supportingText = {
                    if (isErrorBirthDay) {
                        Text(text = TextApp.textObligatoryField)
                    }
                },
                placeholder = {
                    Text(text = TextApp.textNotSpecified)
                },
                trailingIcon = {
                    IconButton(onClick = { focusBirthDay = true }) {
                        IconApp(painter = rememberImageRaw(id = R.raw.ic_drop))
                    }
                },
            )
        }
        BoxSpacer()
        TextBodyMedium(text = TextApp.textCityOfBirth)
        BoxSpacer(0.5f)
        DropMenuCities(
            items = locations,
            modifier = Modifier.fillMaxWidth(),
            enterNameLocation = onSearchLocation,
            onClickClear = { locationBirthEnter.value = null },
            checkItem = { locationBirthEnter.value = it },
            locationChooser = locationBirthEnter.value?.name,
        )
        BoxSpacer()
        TextBodyMedium(text = TextApp.textCityOfResidence)
        BoxSpacer(0.5f)
        DropMenuCities(
            items = locations,
            modifier = Modifier.fillMaxWidth(),
            enterNameLocation = onSearchLocation,
            onClickClear = { locationResidenceEnter.value = null },
            checkItem = { locationResidenceEnter.value = it },
            locationChooser = locationResidenceEnter.value?.name,
        )
        BoxSpacer()
        TextBodyMedium(text = TextApp.textForAMorePreciseSearch)
        BoxSpacer(2f)
        HyperlinkText(
            fullText = TextApp.formatAlreadyHaveAnAccount(TextApp.textToComeIn),
            hyperLinks = listOf(TextApp.textToComeIn)
        ) { item, index ->
            onClickToAuth.invoke()
        }
    }

    if (focusBirthDay) {
        DialogDataPickerddmmyyy(
            initTime = birthdateEnter.value,
            onDismissDialog = {
                focusBirthDay = false
                focusManager.clearFocus()
            },
            onDataMillisSet = { birthdateEnter.value = it },
        )
    }
}


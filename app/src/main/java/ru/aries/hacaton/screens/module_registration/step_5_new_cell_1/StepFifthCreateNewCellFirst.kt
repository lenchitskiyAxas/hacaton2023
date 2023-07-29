package ru.aries.hacaton.screens.module_registration.step_5_new_cell_1

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.aries.hacaton.R
import ru.aries.hacaton.base.common_composable.BoxFillWeight
import ru.aries.hacaton.base.common_composable.BoxSpacer
import ru.aries.hacaton.base.common_composable.ButtonAccentApp
import ru.aries.hacaton.base.common_composable.DialogBottomSheet
import ru.aries.hacaton.base.common_composable.DialogDataPickerddmmyyy
import ru.aries.hacaton.base.common_composable.DialogGender
import ru.aries.hacaton.base.common_composable.IconApp
import ru.aries.hacaton.base.common_composable.PanelBottom
import ru.aries.hacaton.base.common_composable.PanelNavBackTop
import ru.aries.hacaton.base.common_composable.TextBodyLarge
import ru.aries.hacaton.base.common_composable.TextBodyMedium
import ru.aries.hacaton.base.common_composable.TextButtonStyle
import ru.aries.hacaton.base.common_composable.TextFieldOutlinesApp
import ru.aries.hacaton.base.common_composable.TextFieldOutlinesAppStr
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
import ru.aries.hacaton.models.api.CreatingFamilyMember
import ru.aries.hacaton.models.api.Gender
import ru.aries.hacaton.models.api.RoleFamily
import ru.aries.hacaton.screens.module_registration.RegStepsModel

class StepFifthCreateNewCellFirst : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<RegStepsModel>()
        val userData by model.userData.collectAsState()
        val firstFamilyMember by model.firstFamilyMember.collectAsState()
        BackPressHandler(onBackPressed = model::goBackStack)
        StepFifthCreateNewCellFirstScr(
            onClickBack = model::goBackStack,
            yourGender = userData.convertInEnumGender(),
            firstFamilyMember = firstFamilyMember,
            updateFirstFamilyMember = model::updateFirstFamilyMember,
            onClickAdd = model::addSatelliteInCell,
        )
    }
}

@Composable
private fun StepFifthCreateNewCellFirstScr(
    onClickBack: () -> Unit,
    yourGender: Gender,
    firstFamilyMember: CreatingFamilyMember,
    updateFirstFamilyMember: (CreatingFamilyMember)->Unit,
    onClickAdd: () -> Unit,
) {

    val checkData: Boolean = remember(
        firstFamilyMember,
    ) {
        firstFamilyMember.isFullForeSend()
    }

    val nextStep = remember(checkData) {
        {
            if (checkData) {
                onClickAdd.invoke()
            }
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
                .padding(horizontal = DimApp.screenPadding)) {
                TextTitleLarge(text = TextApp.textCreateACell)
                TextBodyLarge(text = yourGender.getEnterDataYourSatellite())
            }
            BoxSpacer()
            ColumnContentNewCell(
                firstName = firstFamilyMember.first_name,
                lastName = firstFamilyMember.last_name,
                patronymic = firstFamilyMember.patronymic,
                birthdate = firstFamilyMember.birthdate,
                maidenName = firstFamilyMember.maiden_name,
                gender = firstFamilyMember.convertInEnumGender(),
                roleFamily = firstFamilyMember.convertInEnumRoleFamily(),
                isSetGender = false,
                setFirstName = {
                    updateFirstFamilyMember.invoke(firstFamilyMember.copy(first_name = it))
                },
                setLastName = {
                    updateFirstFamilyMember.invoke(firstFamilyMember.copy(last_name = it))
                },
                setPatronymic = {
                    updateFirstFamilyMember.invoke(firstFamilyMember.copy(patronymic = it))
                },
                setBirthdate = {
                    updateFirstFamilyMember.invoke(firstFamilyMember.copy(birthdate = it))
                },
                setMaidenName = {
                    updateFirstFamilyMember.invoke(firstFamilyMember.copy(maiden_name = it))
                },
                setGender = {
                    updateFirstFamilyMember.invoke(firstFamilyMember.copy(gender = it.numbGender))
                },
            )

            BoxFillWeight()
            PanelBottom {
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding),
                    enabled = checkData,
                    onClick = nextStep,
                    text = TextApp.titleAdd
                )
            }
        }
    }
}

@Composable
fun ColumnContentNewCell(
    firstName: String?,
    setFirstName: (String) -> Unit,
    lastName: String?,
    setLastName: (String) -> Unit,
    patronymic: String?,
    setPatronymic: (String?) -> Unit,
    birthdate: Long?,
    setBirthdate: (Long) -> Unit,
    maidenName: String?,
    setMaidenName: (String?) -> Unit,
    isSetGender: Boolean = true,
    isMaidenName: Boolean = true,
    gender: Gender?,
    roleFamily:  RoleFamily?,
    setGender: (Gender) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var focusBirthDay by rememberState { false }
    var focusGender by rememberState { false }

    val isErrorName = remember(firstName) { firstName.isNullOrEmpty() }
    val isErrorLastName = remember(lastName) { lastName.isNullOrEmpty() }
    val isErrorBirthDay = remember(birthdate) { birthdate == null }
    val isErrorGender = remember(gender) { gender == null }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DimApp.screenPadding)
    ) {
        TextBodyMedium(text = TextApp.textName)
        BoxSpacer(0.5f)
        TextFieldOutlinesAppStr(
            modifier = Modifier.fillMaxWidth(),
            value = firstName ?: "",
            onValueChange = { setFirstName(it) },
            singleLine = true,
            isError = isErrorName,
            supportingText = {
                if (isErrorName) {
                    Text(text = TextApp.textObligatoryField)
                }
            },
            trailingIcon = {
                if (isErrorName) {
                    IconApp(painter = rememberImageRaw(id = R.raw.ic_error))
                }
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
        )
        BoxSpacer()
        TextBodyMedium(text = TextApp.textSurname)
        BoxSpacer(0.5f)
        TextFieldOutlinesAppStr(
            modifier = Modifier.fillMaxWidth(),
            value = lastName ?: "",
            onValueChange = { setLastName(it) },
            singleLine = true,
            isError = isErrorLastName,
            supportingText = {
                if (isErrorLastName) {
                    Text(text = TextApp.textObligatoryField)
                }
            },
            trailingIcon = {
                if (isErrorLastName) {
                    IconApp(painter = rememberImageRaw(id = R.raw.ic_error))
                }
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
        )
        BoxSpacer()
        TextBodyMedium(text = TextApp.textPatronymic)
        BoxSpacer(0.5f)
        TextFieldOutlinesAppStr(
            modifier = Modifier.fillMaxWidth(),
            value = patronymic ?: "",
            onValueChange = { setPatronymic(it) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
        )
        BoxSpacer()
        TextBodyMedium(text = TextApp.textBirthDayWye)
        BoxSpacer(0.5f)
        Box(
            modifier = Modifier
                .onFocusChanged { focusBirthDay = it.hasFocus },
        ) {
            TextFieldOutlinesApp(
                modifier = Modifier.fillMaxWidth(),
                value = TextFieldValue(birthdate?.toDateString() ?: ""),
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
        if (isSetGender) {
            BoxSpacer()
            TextBodyMedium(text = TextApp.textGender)
            BoxSpacer(0.5f)
            Box(
                modifier = Modifier
                    .onFocusChanged { focusGender = it.hasFocus },
            ) {
                TextFieldOutlinesApp(
                    modifier = Modifier.fillMaxWidth(),
                    value = TextFieldValue(gender?.getGenderText() ?: ""),
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    isError = isErrorGender,
                    supportingText = {
                        if (isErrorGender) {
                            Text(text = TextApp.textObligatoryField)
                        }
                    },
                    placeholder = {
                        Text(text = TextApp.textNotSpecified)
                    },
                    trailingIcon = {
                        IconButton(onClick = { focusGender = true }) {
                            IconApp(painter = rememberImageRaw(id = R.raw.ic_drop))
                        }
                    },
                )
            }
        }
        AnimatedVisibility(
            visible = gender == Gender.WOMAN && isMaidenName
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                BoxSpacer()
                TextBodyMedium(text = TextApp.textMaidenName)
                BoxSpacer(0.5f)
                TextFieldOutlinesAppStr(
                    modifier = Modifier.fillMaxWidth(),
                    value = maidenName ?: "",
                    onValueChange = { setMaidenName(it) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (!focusManager.moveFocus(FocusDirection.Down)){
                                focusManager.clearFocus()
                            }
                        }
                    ),
                )
            }
        }
        BoxSpacer()
    }

    if (focusBirthDay) {
        DialogDataPickerddmmyyy(
            initTime = birthdate,
            onDismissDialog = {
                focusBirthDay = false
                if (!focusManager.moveFocus(FocusDirection.Down)){
                    focusManager.clearFocus()
                }
            },
            onDataMillisSet = { setBirthdate(it) },
        )
    }

    if (focusGender) {
        DialogBottomSheet(
            onDismiss = {
                if (!focusManager.moveFocus(FocusDirection.Down)){
                    focusManager.clearFocus()
                }
                focusGender = false
            }) { onDismiss ->
            DialogGender(
                genderEnter = gender,
                setGender = {
                    setGender.invoke(it)
                    onDismiss.invoke()
                },
                onDismiss = onDismiss
            )
        }
    }
}
package ru.aries.hacaton.screens.module_registration.step_2

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.aries.hacaton.R
import ru.aries.hacaton.base.common_composable.BoxFillWeight
import ru.aries.hacaton.base.common_composable.BoxImageLoad
import ru.aries.hacaton.base.common_composable.BoxSpacer
import ru.aries.hacaton.base.common_composable.ButtonAccentApp
import ru.aries.hacaton.base.common_composable.ButtonWeakApp
import ru.aries.hacaton.base.common_composable.DialogGender
import ru.aries.hacaton.base.common_composable.DialogBottomSheet
import ru.aries.hacaton.base.common_composable.DialogCropImage
import ru.aries.hacaton.base.common_composable.DialogGetImage
import ru.aries.hacaton.base.common_composable.HyperlinkText
import ru.aries.hacaton.base.common_composable.IconApp
import ru.aries.hacaton.base.common_composable.PanelBottom
import ru.aries.hacaton.base.common_composable.PanelNavBackTop
import ru.aries.hacaton.base.common_composable.TextBodyLarge
import ru.aries.hacaton.base.common_composable.TextBodyMedium
import ru.aries.hacaton.base.common_composable.TextButtonStyle
import ru.aries.hacaton.base.common_composable.TextFieldOutlinesApp
import ru.aries.hacaton.base.common_composable.TextTitleLarge
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.AxasTheme
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.BackPressHandler
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberImageRaw
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.base.util.rememberOpenIntentUrl
import ru.aries.hacaton.base.util.rememberState
import ru.aries.hacaton.models.api.Gender
import ru.aries.hacaton.screens.module_registration.RegStepsModel

class StepTwoScreen : Screen {

    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<RegStepsModel>()
        val userData by model.userData.collectAsState()
        var imageUriCamera by rememberState<Uri?> { null }
        var getImage by rememberState { false }
        BackPressHandler(onBackPressed = model::goBackStack)
        val openPolicy = rememberOpenIntentUrl()
        StepTwoScr(
            firstName = userData.first_name,
            lastName = userData.last_name,
            patronymic = userData.patronymic,
            maidenName = userData.maiden_name,
            gender = userData.convertInEnumGender(),
            imageUri = userData.avatar,
            onClickBack = model::goBackStack,
            onClickNext = model::updateMyProfileStepTwo,
            onClickToAuth = model::goToAuth,
            onClickNewImage = {
                getImage = true
            },
            onClickPrivacyPolicy = {
                openPolicy.invoke(TextApp.linkAxas)
            })

        imageUriCamera?.let {
            DialogCropImage(
                onDismiss = { imageUriCamera = null },
                uriImage = it,
                onImageCroppedUri = model::uploadPhoto,
            )
        }

        if (getImage) {
            DialogGetImage(
                onDismiss = { getImage = false },
                uploadPhoto = {
                    imageUriCamera = it
                    getImage = false
                }
            )
        }
    }
}

@Composable
private fun StepTwoScr(
    firstName: String?,
    lastName: String?,
    patronymic: String?,
    maidenName: String?,
    gender: Gender?,
    imageUri: Any?,
    onClickBack: () -> Unit,
    onClickToAuth: () -> Unit,
    onClickNext: (
        firstName: String,
        lastName: String,
        patronymic: String?,
        maidenName: String?,
        gender: Gender,
    ) -> Unit,
    onClickNewImage: () -> Unit,
    onClickPrivacyPolicy: () -> Unit,
) {

    val firstNameEnter = rememberState(firstName) { TextFieldValue(firstName ?: "") }
    val lastNameEnter = rememberState(lastName) { TextFieldValue(lastName ?: "") }
    val patronymicEnter = rememberState(patronymic) { TextFieldValue(patronymic ?: "") }
    val maidenNameEnter = rememberState(maidenName) { TextFieldValue(maidenName ?: "") }
    val genderEnter: MutableState<Gender?> = rememberState(gender) { gender }

    val checkData: Boolean = remember(
        firstNameEnter.value,
        lastNameEnter.value,
        genderEnter.value) {
        firstNameEnter.value.text.isNotEmpty() &&
                lastNameEnter.value.text.isNotEmpty() &&
                genderEnter.value != null
    }

    val nextStep = remember(checkData) {
        {
            val genderCheck = genderEnter.value
            if (checkData && genderCheck != null) onClickNext(
                firstNameEnter.value.text,
                lastNameEnter.value.text,
                patronymicEnter.value.text.ifEmpty { null },
                maidenNameEnter.value.text.ifEmpty { null },
                genderCheck,
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
        PanelNavBackTop(onClickBack = onClickBack)
        TextButtonStyle(
            modifier = Modifier.padding(horizontal = DimApp.screenPadding),
            text = TextApp.formatStepFrom(2, 4)
        )
        BoxSpacer(0.5f)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            ColumnContentTwo(
                firstNameEnter = firstNameEnter,
                lastNameEnter = lastNameEnter,
                patronymicEnter = patronymicEnter,
                maidenNameEnter = maidenNameEnter,
                genderEnter = genderEnter,
                imageUri = imageUri,
                onClickNextStep = nextStep,
                onClickPrivacyPolicy = onClickPrivacyPolicy,
                onClickToAuth = onClickToAuth,
                onClickNewImage = onClickNewImage,
                onClickSetGender = { genderEnter.value = it }
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
private fun ColumnScope.ColumnContentTwo(
    firstNameEnter: MutableState<TextFieldValue>,
    lastNameEnter: MutableState<TextFieldValue>,
    patronymicEnter: MutableState<TextFieldValue>,
    maidenNameEnter: MutableState<TextFieldValue>,
    genderEnter: MutableState<Gender?>,
    imageUri: Any?,
    onClickNewImage: () -> Unit,
    onClickNextStep: () -> Unit,
    onClickSetGender: (Gender) -> Unit,
    onClickPrivacyPolicy: () -> Unit,
    onClickToAuth: () -> Unit
) {

    var focusGender by rememberState { false }
    val focusManager = LocalFocusManager.current
    val isErrorName = remember(firstNameEnter.value) { firstNameEnter.value.text.isEmpty() }
    val isErrorLastName = remember(lastNameEnter.value) { lastNameEnter.value.text.isEmpty() }
    val isErrorGender = remember(genderEnter.value) { genderEnter.value == null }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(DimApp.screenPadding)
    ) {
        TextTitleLarge(text = TextApp.textCreateAnAccount)
        BoxSpacer()
        TextBodyLarge(text = TextApp.textEnterYourDetails)
        BoxSpacer(2f)
        TextBodyMedium(text = TextApp.textPhoto)
        BoxSpacer(0.5f)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BoxImageLoad(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(DimApp.imageAvatarSize),
                image = imageUri,
                drawableError = R.drawable.stab_avatar,
                drawablePlaceholder = R.drawable.stab_avatar
            )
            BoxSpacer()
            ButtonWeakApp(onClick = onClickNewImage, text = TextApp.titleChange)
        }
        BoxSpacer()
        TextBodyMedium(text = TextApp.textName)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = firstNameEnter.value,
            onValueChange = { firstNameEnter.value = it },
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
        BoxSpacer(2f)
        TextBodyMedium(text = TextApp.textSurname)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = lastNameEnter.value,
            onValueChange = { lastNameEnter.value = it },
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
        BoxSpacer(2f)
        TextBodyMedium(text = TextApp.textPatronymic)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = patronymicEnter.value,
            onValueChange = { patronymicEnter.value = it },
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
        TextBodyMedium(text = TextApp.textGender)
        BoxSpacer(0.5f)
        Box(
            modifier = Modifier
                .onFocusChanged { focusGender = it.hasFocus },
        ) {
            TextFieldOutlinesApp(
                modifier = Modifier.fillMaxWidth(),
                value = TextFieldValue(genderEnter.value?.getGenderText() ?: ""),
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
        BoxSpacer()
        AnimatedVisibility(
            visible = genderEnter.value == Gender.WOMAN
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                TextBodyMedium(text = TextApp.textMaidenName)
                BoxSpacer(0.5f)
                TextFieldOutlinesApp(
                    modifier = Modifier.fillMaxWidth(),
                    value = maidenNameEnter.value,
                    onValueChange = { maidenNameEnter.value = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onClickNextStep.invoke()
                        }
                    ),
                )
                BoxSpacer()
            }
        }
        BoxSpacer()
        HyperlinkText(
            fullText = TextApp.formatYouAgreeTo(TextApp.textPrivacyPolicy),
            hyperLinks = listOf(TextApp.textPrivacyPolicy)
        ) { item, index ->
            onClickPrivacyPolicy.invoke()
        }
        BoxSpacer(2f)
        HyperlinkText(
            fullText = TextApp.formatAlreadyHaveAnAccount(TextApp.textToComeIn),
            hyperLinks = listOf(TextApp.textToComeIn)
        ) { item, index ->
            onClickToAuth.invoke()
        }
    }

    if (focusGender) {
        DialogBottomSheet(
            onDismiss = {
                focusManager.clearFocus()
                focusGender = false
            }) { onDismiss ->
            DialogGender(
                genderEnter = null,
                setGender = {
                    onClickSetGender.invoke(it)
                    onDismiss.invoke()
                },
                onDismiss = onDismiss
            )
        }
    }
}


@Preview
@Composable
private fun StepTwoScrTest() {
    AxasTheme {
        StepTwoScr(
            imageUri = null,
            onClickBack = {},
            onClickToAuth = {},
            onClickNext = { _, _, _, _, _ -> },
            onClickNewImage = {},
            onClickPrivacyPolicy = {},
            firstName = "firstName",
            lastName = "lastName",
            patronymic = "patronymic",
            maidenName = "maidenName",
            gender = null,
        )
    }
}
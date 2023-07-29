package ru.aries.hacaton.screens.module_registration.step_1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.aries.hacaton.R
import ru.aries.hacaton.base.common_composable.BoxSpacer
import ru.aries.hacaton.base.common_composable.ButtonAccentApp
import ru.aries.hacaton.base.common_composable.HyperlinkText
import ru.aries.hacaton.base.common_composable.IconApp
import ru.aries.hacaton.base.common_composable.PanelBottom
import ru.aries.hacaton.base.common_composable.PanelNavBackTop
import ru.aries.hacaton.base.common_composable.TextBodyLarge
import ru.aries.hacaton.base.common_composable.TextBodyMedium
import ru.aries.hacaton.base.common_composable.TextButtonApp
import ru.aries.hacaton.base.common_composable.TextButtonStyle
import ru.aries.hacaton.base.common_composable.TextFieldOutlinesApp
import ru.aries.hacaton.base.common_composable.TextTitleLarge
import ru.aries.hacaton.base.extension.isTextMatchingRegex
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.AxasTheme
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.BackPressHandler
import ru.aries.hacaton.base.util.FieldValidators
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberImageRaw
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.base.util.rememberState
import ru.aries.hacaton.screens.module_registration.RegStepsModel

class StepOneScreen() : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<RegStepsModel>()
        BackPressHandler(onBackPressed = model::goBackStack)
        val codeValidation by model.codeValidation.collectAsState()
        val email by model.email.collectAsState()
        val password by model.password.collectAsState()

        codeValidation?.let { code ->
            ValidationCode(
                onClickBack = model::deleteCodeValidation,
                onClickNextStep = model::goToTwoStep,
                code = code,
                email = email,
            )
        } ?: run {
            StepOneScr(
                onClickBack = model::goBackStack,
                onClickCreateNewEmail = model::createNewEmail,
                password = password,
                email = email,
            )
        }
    }
}

@Composable
private fun ValidationCode(
    onClickBack: () -> Unit,
    onClickNextStep: (enterCode: String) -> Unit,
    code: String,
    email: String,
) {

    val enterCode = rememberState { TextFieldValue() }
    val buttonOn = remember(enterCode) { enterCode.value.text.isNotBlank() }
    val nextStep = remember(buttonOn) {
        { if (enterCode.value.text.isNotBlank()) onClickNextStep(enterCode.value.text) }
    }

    LaunchedEffect(key1 = enterCode.value, block = {
        if (code == enterCode.value.text) {
            onClickNextStep.invoke(enterCode.value.text)
        }
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.backgroundVariant)
            .systemBarsPadding()
            .imePadding()
    ) {
        PanelNavBackTop(onClickBack = onClickBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            ColumnValidationEmail(
                enterCode = enterCode,
                onClickNextStep = nextStep,
                email = email,
                onClickBack = onClickBack,
            )
            Box(modifier = Modifier.weight(1f))
            PanelBottom {
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding),
                    enabled = buttonOn,
                    onClick = nextStep,
                    text = TextApp.titleNext
                )
            }
        }
    }
}

@Composable
private fun StepOneScr(
    onClickBack: () -> Unit,
    onClickCreateNewEmail: (email: String, password: String) -> Unit,
    password: String,
    email: String,
) {

    val emailAddress = rememberState(email) { TextFieldValue(email) }
    val passwordEnter = rememberState(password) { TextFieldValue(password) }
    val passwordTwoEnter = rememberState(password) { TextFieldValue(password) }

    val emailAddressValidator: Boolean = remember(emailAddress.value) {
        emailAddress.value.text.isBlank() || FieldValidators.isValidEmail(emailAddress.value.text)
    }
    val passwordsSimilarValidator: Boolean = remember(passwordTwoEnter.value) {
        passwordEnter.value.text == passwordTwoEnter.value.text
    }
    val passwordsRegularValidator: Boolean = remember(passwordEnter.value) {
        isTextMatchingRegex(passwordEnter.value.text)
    }

    val checkData: Boolean =
        remember(emailAddress.value, passwordEnter.value, passwordTwoEnter.value) {
            emailAddress.value.text.isNotBlank()
                    && passwordEnter.value.text.isNotBlank()
                    && passwordTwoEnter.value.text.isNotBlank()
                    && emailAddressValidator
                    && passwordsSimilarValidator
                    && passwordsRegularValidator
        }

    val nextStep = remember(checkData) {
        { if (checkData) onClickCreateNewEmail(emailAddress.value.text, passwordEnter.value.text) }
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
            text = TextApp.formatStepFrom(1, 4)
        )
        BoxSpacer(0.5f)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ColumnContentOne(
                emailAddress = emailAddress,
                password = passwordEnter,
                emailAddressValidator = emailAddressValidator,
                passwordsSimilarValidator = passwordsSimilarValidator,
                passwordTwo = passwordTwoEnter,
                onClickNextStep = nextStep,
                onClickToComeIn = onClickBack,
                passwordsRegularValidator = passwordsRegularValidator
            )
            Box(modifier = Modifier.weight(1f))
            PanelBottom(
                modifier = Modifier.fillMaxWidth()
            ) {
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
private fun ColumnScope.ColumnContentOne(
    emailAddress: MutableState<TextFieldValue>,
    password: MutableState<TextFieldValue>,
    passwordTwo: MutableState<TextFieldValue>,
    emailAddressValidator: Boolean,
    passwordsSimilarValidator: Boolean,
    passwordsRegularValidator: Boolean,
    onClickNextStep: () -> Unit,
    onClickToComeIn: () -> Unit
) {

    val focusManager = LocalFocusManager.current
    var passwordVisible by rememberState { false }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(DimApp.screenPadding)
    ) {
        TextTitleLarge(text = TextApp.textCreateAnAccount)
        BoxSpacer()
        TextBodyLarge(text = TextApp.textRegisterWithEmail)
        BoxSpacer()
        BoxSpacer()
        TextBodyMedium(text = TextApp.textEmailAddress)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = emailAddress.value,
            onValueChange = { emailAddress.value = it },
            singleLine = true,
            isError = !emailAddressValidator,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            supportingText = {
                if (!emailAddressValidator) {
                    Text(text = TextApp.textEmailNoValide)
                }

            },
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
        )
        BoxSpacer()
        BoxSpacer()
        TextBodyMedium(text = TextApp.textPassword)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = password.value,
            onValueChange = { password.value = it },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Send
            ),
            isError = !passwordsSimilarValidator || !passwordsRegularValidator,
            supportingText = {
                if (!passwordsRegularValidator) {
                    Text(text = TextApp.textPasswordNoRegular)
                }
                else if (!passwordsSimilarValidator) {
                    Text(text = TextApp.textPasswordNoSimilar)
                }
            },
            trailingIcon = {
                val image = if (passwordVisible)
                    rememberImageRaw(id = R.raw.ic_visibility)
                else rememberImageRaw(id = R.raw.ic_visibility_off)
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    IconApp(painter = image)
                }
            },
            keyboardActions = KeyboardActions(
                onSend = {
                    focusManager.clearFocus()
                    onClickNextStep.invoke()
                }
            ),
        )
        BoxSpacer()
        BoxSpacer()
        TextBodyMedium(text = TextApp.textPasswordTwo)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = passwordTwo.value,
            onValueChange = { passwordTwo.value = it },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Send
            ),
            isError = !passwordsSimilarValidator,
            supportingText = {
                if (!passwordsSimilarValidator) {
                    Text(text = TextApp.textPasswordNoSimilar)
                }
            },
            trailingIcon = {
                val image = if (passwordVisible)
                    rememberImageRaw(id = R.raw.ic_visibility)
                else rememberImageRaw(id = R.raw.ic_visibility_off)
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    IconApp(painter = image)
                }
            },
            keyboardActions = KeyboardActions(
                onSend = {
                    focusManager.clearFocus()
                    onClickNextStep.invoke()
                }
            ),
        )
        BoxSpacer()
        BoxSpacer()
        HyperlinkText(
            fullText = TextApp.formatAlreadyHaveAnAccount(TextApp.textToComeIn),
            hyperLinks = listOf(TextApp.textToComeIn)
        ) { item, index ->
            onClickToComeIn.invoke()
        }
    }
}

@Composable
private fun ColumnScope.ColumnValidationEmail(
    enterCode: MutableState<TextFieldValue>,
    email: String,
    onClickNextStep: () -> Unit,
    onClickBack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(DimApp.screenPadding)
    ) {
        TextTitleLarge(text = TextApp.textWelcomeInFamilyVibe)
        BoxSpacer()
        HyperlinkText(
            fullText = TextApp.formatConfirmationCodeSentYourEmail(email),
            hyperLinks = listOf(email)
        ) { item, index -> }
        BoxSpacer()
        BoxSpacer()
        TextBodyMedium(text = TextApp.textMailConfirmationCode)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = enterCode.value,
            onValueChange = { enterCode.value = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    focusManager.clearFocus()
                    if (enterCode.value.text.isNotBlank()) {
                        onClickNextStep.invoke()
                    }
                }
            ),
        )
        BoxSpacer()
        TextButtonApp(
            onClick = onClickBack,
            contentPadding = PaddingValues(2.dp),
            text = TextApp.textChangeEmailAddress
        )
    }
}

@Preview
@Composable
private fun StepOneScrTest() {
    AxasTheme {
        StepOneScr(
            onClickBack = {},
            onClickCreateNewEmail = { s: String, s1: String -> },
            password = "",
            email = ""
        )
    }
}
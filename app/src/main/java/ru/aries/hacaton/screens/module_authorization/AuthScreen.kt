package ru.aries.hacaton.screens.module_authorization

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
import androidx.compose.runtime.Composable
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.aries.hacaton.R
import ru.aries.hacaton.base.common_composable.BoxSpacer
import ru.aries.hacaton.base.common_composable.ButtonAccentApp
import ru.aries.hacaton.base.common_composable.DialogBackPressExit
import ru.aries.hacaton.base.common_composable.HyperlinkText
import ru.aries.hacaton.base.common_composable.IconApp
import ru.aries.hacaton.base.common_composable.LogoRow
import ru.aries.hacaton.base.common_composable.PanelBottom
import ru.aries.hacaton.base.common_composable.TextBodyMedium
import ru.aries.hacaton.base.common_composable.TextFieldOutlinesApp
import ru.aries.hacaton.base.common_composable.TextTitleLarge
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.FieldValidators
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberImageRaw
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.base.util.rememberState

class AuthScreen : Screen {

    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<AuthScreenModel>()
        DialogBackPressExit()
        AuthorizationScr(
            onClickAuthorization = model::authorization,
            onClickRegistration = model::goToReg
        )
    }
}

@Composable
private fun AuthorizationScr(
    onClickAuthorization: (email: String, password: String) -> Unit,
    onClickRegistration: () -> Unit,
) {

    val focusManager = LocalFocusManager.current
    var emailAddress by rememberState { TextFieldValue(TextApp.mockEmail) }
    var password by rememberState { TextFieldValue(TextApp.mockPass) }
    var passwordVisible by rememberState { false }

    val emailAddressValidator: Boolean = remember(emailAddress) {
        emailAddress.text.isBlank() ||
                FieldValidators.isValidEmail(emailAddress.text)
    }

    val checkData: Boolean = remember(emailAddress, password) {
        emailAddress.text.isNotBlank() && password.text.isNotBlank() && emailAddressValidator
    }

    val sendData = remember(checkData) {
        {
            if (checkData) {
                onClickAuthorization(emailAddress.text, password.text)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.primary)
            .systemBarsPadding()
            .imePadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            LogoRow()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.5f)
                .background(
                    color = ThemeApp.colors.onPrimary,
                    shape = ThemeApp.shape.mediumTop
                )
                .padding(top = DimApp.screenPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding)) {
                TextTitleLarge(text = TextApp.textWelcome)
                BoxSpacer()
                BoxSpacer()
                TextBodyMedium(text = TextApp.textEmailAddress)
                BoxSpacer(0.5f)
                TextFieldOutlinesApp(
                    modifier = Modifier.fillMaxWidth(),
                    value = emailAddress,
                    onValueChange = { emailAddress = it },
                    singleLine = true,
                    isError = !emailAddressValidator,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
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
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Send
                    ),
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
                            sendData.invoke()
                        }
                    ),
                )
                BoxSpacer()
                BoxSpacer()
                HyperlinkText(
                    fullText = TextApp.formatNotRegisteredYet(TextApp.textRegistered),
                    hyperLinks = listOf(TextApp.textRegistered)
                ) { item, index ->
                    onClickRegistration.invoke()
                }
            }
            Box(modifier = Modifier.weight(1f))
            PanelBottom {
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding),
                    enabled = checkData,
                    onClick = sendData,
                    text = TextApp.titleNext
                )
            }
        }
    }
}


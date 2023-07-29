package ru.aries.hacaton.base.common_composable

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.aries.hacaton.base.theme.ArielTheme
import ru.aries.hacaton.base.theme.ThemeApp

@Composable
fun TextFieldApp(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = ThemeApp.typography.bodyLarge,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = textColorField(),
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier =modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle =textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation =visualTransformation,
        keyboardOptions =keyboardOptions,
        keyboardActions =keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource =interactionSource,
        shape =shape,
        colors =colors,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText
    )
}

@Composable
fun TextFieldAppStr(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = ThemeApp.typography.bodyLarge,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = textColorField(),
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier =modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle =textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation =visualTransformation,
        keyboardOptions =keyboardOptions,
        keyboardActions =keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource =interactionSource,
        shape =shape,
        colors =colors,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText
    )
}

@Composable
fun TextFieldOutlinesAppStr(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = ThemeApp.typography.bodyLarge,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = shapeBoard(),
    colors: TextFieldColors = textColorField(),
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier =modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle =textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation =visualTransformation,
        keyboardOptions =keyboardOptions,
        keyboardActions =keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource =interactionSource,
        shape =shape,
        colors =colors,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText
    )
}

@Composable
fun TextFieldOutlinesApp(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = ThemeApp.typography.bodyLarge,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = shapeBoard(),
    colors: TextFieldColors = textColorField(),
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier =modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle =textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation =visualTransformation,
        keyboardOptions =keyboardOptions,
        keyboardActions =keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource =interactionSource,
        shape =shape,
        colors =colors,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText
    )
}


@Composable
private fun shapeBoard() = ThemeApp.shape.smallAll

@Composable
private fun textColorField() = TextFieldDefaults.colors(

    focusedTextColor = ThemeApp.colors.textDark,
    unfocusedTextColor =ThemeApp.colors.textDark,
    disabledTextColor =  ThemeApp.colors.textLight,
    errorTextColor = ThemeApp.colors.textDark,

    focusedLabelColor = ThemeApp.colors.primary,
    unfocusedLabelColor = ThemeApp.colors.borderLight,
    disabledLabelColor = ThemeApp.colors.textLight,
    errorLabelColor = ThemeApp.colors.attentionContent,

    focusedPlaceholderColor = ThemeApp.colors.textLight,
    unfocusedPlaceholderColor = ThemeApp.colors.textLight,
    disabledPlaceholderColor = ThemeApp.colors.textLight,
    errorPlaceholderColor = ThemeApp.colors.attentionContent,

    focusedIndicatorColor = ThemeApp.colors.primary,
    unfocusedIndicatorColor = ThemeApp.colors.borderLight,
    disabledIndicatorColor = ThemeApp.colors.textLight,
    errorIndicatorColor = ThemeApp.colors.attentionContent,

    focusedPrefixColor = ThemeApp.colors.textDark,
    unfocusedPrefixColor =ThemeApp.colors.textDark,
    disabledPrefixColor =ThemeApp.colors.textLight,
    errorPrefixColor = ThemeApp.colors.textDark,

    focusedSuffixColor =ThemeApp.colors.textDark,
    unfocusedSuffixColor =ThemeApp.colors.textDark,
    disabledSuffixColor =ThemeApp.colors.textLight,
    errorSuffixColor = ThemeApp.colors.textDark,

    cursorColor = ThemeApp.colors.textDark,
    errorCursorColor = ThemeApp.colors.textDark,

    selectionColors = TextSelectionColors(
        handleColor =ThemeApp.colors.primary,
        backgroundColor =ThemeApp.colors.primary.copy(alpha = 0.4f)
    ),

    focusedLeadingIconColor = ThemeApp.colors.primary,
    unfocusedLeadingIconColor = ThemeApp.colors.primary,
    disabledLeadingIconColor = ThemeApp.colors.textLight,
    errorLeadingIconColor = ThemeApp.colors.textLight,

    focusedTrailingIconColor = ThemeApp.colors.textLight,
    unfocusedTrailingIconColor = ThemeApp.colors.textLight,
    disabledTrailingIconColor = ThemeApp.colors.textLight,
    errorTrailingIconColor = ThemeApp.colors.attentionContent,



    focusedSupportingTextColor = ThemeApp.colors.textLight,
    unfocusedSupportingTextColor = ThemeApp.colors.textLight,
    disabledSupportingTextColor = ThemeApp.colors.textLight,
    errorSupportingTextColor = ThemeApp.colors.attentionContent,

    errorContainerColor = ThemeApp.colors.background.copy(.0f),
    focusedContainerColor = ThemeApp.colors.background.copy(.0f),
    unfocusedContainerColor = ThemeApp.colors.background.copy(.0f),
    disabledContainerColor =ThemeApp.colors.background.copy(.0f),
)

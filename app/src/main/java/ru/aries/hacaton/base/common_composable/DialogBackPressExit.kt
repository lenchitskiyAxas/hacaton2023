package ru.aries.hacaton.base.common_composable

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import ru.aries.hacaton.R
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.util.BackPressHandler
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ArielTheme
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.rememberImageRaw


@Composable
fun DialogBackPressExit() {
    val exitCheck = remember { mutableStateOf(false) }
    val activity = (LocalContext.current as? Activity)
    BackPressHandler { exitCheck.value = true }
    val onDismiss = { exitCheck.value = false }
    if (exitCheck.value) {
        Dialog(onDismissRequest = onDismiss) {
            ContentDialogExit(
                onDismiss = onDismiss,
                onClickExit = { activity?.finish() }
            )
        }
    }
}

@Composable
private fun ContentDialogExit(
    onDismiss: () -> Unit,
    onClickExit: () -> Unit,
) {
    Column(modifier = Modifier
        .clip(ThemeApp.shape.mediumAll)
        .background(ThemeApp.colors.backgroundVariant)
        .padding(horizontal = DimApp.screenPadding)) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextTitleSmall(text = TextApp.titleExit)
            IconButtonApp(
                modifier = Modifier.offset(x = DimApp.screenPadding),
                onClick = onDismiss) {
                IconApp(painter = rememberImageRaw(R.raw.ic_close))
            }
        }

        TextBodyLarge(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = TextApp.textExitApp)
        BoxSpacer()
        BoxSpacer()
        Row(modifier = Modifier.fillMaxWidth()) {
            ButtonAccentTextApp(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = onDismiss,
                text = TextApp.titleCancel)
            BoxSpacer()
            ButtonAccentApp(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = onClickExit, text = TextApp.titleNext)
        }
        BoxSpacer()
    }
}

@Preview
@Composable
private fun TestContentDialogExit() {
    ArielTheme {
        ContentDialogExit(
            onDismiss = {},
            onClickExit = {}
        )
    }
}


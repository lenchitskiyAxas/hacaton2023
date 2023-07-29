package ru.aries.hacaton.base.common_composable

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.Dialog
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp


@Composable
fun DialogDeletePhoto(
    namePhoto: String,
    onDismiss: () -> Unit,
    onYes: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(ThemeApp.shape.mediumAll)
                .background(ThemeApp.colors.backgroundVariant)
                .padding(DimApp.screenPadding)
        ) {
            TextTitleSmall(text = TextApp.textTitleDeletePhoto)
            BoxSpacer()
            TextBodyMedium(text = TextApp.formatDelete(namePhoto))
            BoxSpacer()
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BoxSpacer()
                TextButtonApp(
                    modifier = Modifier,
                    onClick = onDismiss,
                    text = TextApp.titleCancel
                )
                TextButtonApp(
                    modifier = Modifier,
                    onClick = onYes,
                    text = TextApp.textDelete
                )
            }


        }
    }
}
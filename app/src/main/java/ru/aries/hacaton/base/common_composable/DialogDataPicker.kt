package ru.aries.hacaton.base.common_composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.aries.hacaton.base.extension.toDateString
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.util.picker.DataPickerddmmyyy
import ru.aries.hacaton.base.util.rememberState

@Composable
fun DialogDataPickerddmmyyy(
    onDismissDialog: () -> Unit,
    onDataMillisSet: (Long) -> Unit,
    initTime: Long? = null
) {
    DialogBottomSheet(
        onDismiss = onDismissDialog
    ) { onDismiss ->
        var bethTime by rememberState { initTime ?: System.currentTimeMillis() }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()
                .padding(horizontal = DimApp.screenPadding)
        ) {
            BoxSpacer()
            TextBodyLarge(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = TextApp.textBirthDay
            )
            DataPickerddmmyyy(
                modifier = Modifier.padding(horizontal = DimApp.screenPadding),
                onDataChose = { bethTime = it }
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                ButtonAccentTextApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = onDismiss,
                    text = TextApp.titleCancel
                )
                BoxSpacer()
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = {
                        onDataMillisSet.invoke(bethTime)
                        onDismiss.invoke()
                    },
                    text = TextApp.titleNext
                )
            }
            BoxSpacer()
        }
    }

}
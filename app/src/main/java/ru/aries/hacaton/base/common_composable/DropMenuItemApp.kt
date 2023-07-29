package ru.aries.hacaton.base.common_composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.aries.hacaton.R
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.rememberImageRaw

@Composable
fun DropMenuInAlbumContains(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onAddToFavorite: () -> Unit,
    onRename: () -> Unit,
    onDownload: () -> Unit,
    onDelete: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        content.invoke(this)
        DropdownMenu(
            modifier = Modifier
                .background(color = ThemeApp.colors.backgroundVariant),
            expanded = expanded,
            onDismissRequest = onDismiss,

            ) {
            DropdownMenuItem(
                text = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconApp(
                            modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                            painter = rememberImageRaw(R.raw.ic_star)
                        )
                        TextButtonStyle(text = TextApp.textAddToFavorite)
                    }
                },
                onClick = {
                    onAddToFavorite.invoke()
                    onDismiss.invoke()
                }
            )
            DropdownMenuItem(
                text = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconApp(
                            modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                            painter = rememberImageRaw(R.raw.ic_edit)
                        )
                        TextButtonStyle(text = TextApp.textRename)
                    }
                },
                onClick = {
                    onRename.invoke()
                    onDismiss.invoke()
                }
            )
            DropdownMenuItem(
                text = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconApp(
                            modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                            painter = rememberImageRaw(R.raw.ic_download)
                        )
                        TextButtonStyle(text = TextApp.textDownload)
                    }
                },
                onClick = {
                    onDownload.invoke()
                    onDismiss.invoke()
                }
            )
            DropdownMenuItem(
                text = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconApp(
                            modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                            painter = rememberImageRaw(R.raw.ic_delete)
                        )
                        TextButtonStyle(text = TextApp.textDelete)
                    }
                },
                onClick = {
                    onDelete.invoke()
                    onDismiss.invoke()
                }
            )
        }
    }
}

@Composable
fun DropMenuInMedia(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onAddToFavorite: () -> Unit,
    onGoToAlbum: () -> Unit,
    onRename: () -> Unit,
    onDownload: () -> Unit,
    onDelete: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        content.invoke(this)
        DropdownMenu(
            modifier = Modifier
                .background(color = ThemeApp.colors.backgroundVariant),
            expanded = expanded,
            onDismissRequest = onDismiss,

            ) {

            DropdownMenuItem(
                text = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconApp(
                            modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                            painter = rememberImageRaw(R.raw.ic_edit)
                        )
                        TextButtonStyle(text = TextApp.textRemake)
                    }
                },
                onClick = {
                    onRename.invoke()
                    onDismiss.invoke()
                }
            )
            DropdownMenuItem(
                text = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconApp(
                            modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                            painter = rememberImageRaw(R.raw.ic_star)
                        )
                        TextButtonStyle(text = TextApp.textAddToFavorite)
                    }
                },
                onClick = {
                    onAddToFavorite.invoke()
                    onDismiss.invoke()
                }
            )
            DropdownMenuItem(
                text = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconApp(
                            modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                            painter = rememberImageRaw(R.raw.ic_download)
                        )
                        TextButtonStyle(text = TextApp.textDownload)
                    }
                },
                onClick = {
                    onDownload.invoke()
                    onDismiss.invoke()
                }
            )
            DropdownMenuItem(
                text = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconApp(
                            modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                            painter = rememberImageRaw(R.raw.ic_album)
                        )
                        TextButtonStyle(text = TextApp.textGoToAlbum)
                    }
                },
                onClick = {
                    onGoToAlbum.invoke()
                    onDismiss.invoke()
                }
            )
            DropdownMenuItem(
                text = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconApp(
                            modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                            painter = rememberImageRaw(R.raw.ic_delete)
                        )
                        TextButtonStyle(text = TextApp.textDelete)
                    }
                },
                onClick = {
                    onDelete.invoke()
                    onDismiss.invoke()
                }
            )
        }
    }
}


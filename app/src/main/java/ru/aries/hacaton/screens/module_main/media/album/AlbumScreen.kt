package ru.aries.hacaton.screens.module_main.media.album

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import kotlinx.coroutines.delay
import ru.aries.hacaton.R
import ru.aries.hacaton.base.common_composable.BoxFillWeight
import ru.aries.hacaton.base.common_composable.BoxSpacer
import ru.aries.hacaton.base.common_composable.ButtonAccentApp
import ru.aries.hacaton.base.common_composable.CheckerApp
import ru.aries.hacaton.base.common_composable.ColorButtonApp
import ru.aries.hacaton.base.common_composable.ContentButtonAddNew
import ru.aries.hacaton.base.common_composable.DialogGetImageList
import ru.aries.hacaton.base.common_composable.DialogRenameAlbum
import ru.aries.hacaton.base.common_composable.DropMenuInAlbumContains
import ru.aries.hacaton.base.common_composable.FloatingActionButtonApp
import ru.aries.hacaton.base.common_composable.IconApp
import ru.aries.hacaton.base.common_composable.IconButtonApp
import ru.aries.hacaton.base.common_composable.ImageItem
import ru.aries.hacaton.base.common_composable.PanelBottom
import ru.aries.hacaton.base.common_composable.PanelNavBackTop
import ru.aries.hacaton.base.common_composable.ProgressIndicatorApp
import ru.aries.hacaton.base.common_composable.TextBodyLarge
import ru.aries.hacaton.base.common_composable.TextBodyMedium
import ru.aries.hacaton.base.common_composable.TextButtonApp
import ru.aries.hacaton.base.common_composable.TextCaption
import ru.aries.hacaton.base.common_composable.TextFieldOutlinesApp
import ru.aries.hacaton.base.common_composable.TextTitleSmall
import ru.aries.hacaton.base.common_composable.colorsButtonAccentTextApp
import ru.aries.hacaton.base.extension.downloadFromUrlExt
import ru.aries.hacaton.base.extension.minLinesHeight
import ru.aries.hacaton.base.extension.toDateMillisToUnixString
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.PermissionsModule
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberImageRaw
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.base.util.rememberState
import ru.aries.hacaton.models.api.GettingMedia
import ru.aries.hacaton.screens.module_main.media.MediaModel

class AlbumScreen(
    private val idAlbum: Int
) : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<MediaModel>()

        LifecycleEffect(onStarted = {
            model.getMediaByAlbum(idAlbum)
            model.getAlbum(idAlbum)
        })

        val listMedia by model.listMedia.collectAsState()
        val album by model.currentAlbum.collectAsState()

        val context = LocalContext.current
        var isViewRenameDialog by rememberState { false }
        var isViewInfo by rememberState { false }
        var getImage by rememberState { false }
        var getImageNew by rememberState { false }

        var linkDownload by remember { mutableStateOf<String?>(null) }
        val rememberDownloadManager =
            PermissionsModule.launchPermissionCameraAndGallery { isPermission ->
                if (isPermission) {
                    linkDownload?.let {
                        downloadFromUrlExt(context, it)
                    }
                }
            }

        when (isViewInfo) {
            true  -> AlbumInfo(
                onClickBack = { isViewInfo = false },
                onCreate = model::changeAlbum,
                nameAlbum = album?.name ?: "",
                nameOwner = album?.owner?.getFullName() ?: "",
                date = album?.custom_date?.toDateMillisToUnixString() ?: "",
                description = album?.description ?: "",
            )

            false -> AlbumContains(
                onClickBack = model::goBackStack,
                onClickInfo = { isViewInfo = true },
                nameAlbum = album?.name ?: "",
                listMedia = listMedia,
                onClickAddPhoto = { getImage = true },
                onAddToFavorite = model::setAlbumIsFavorite,
                onRename = { isViewRenameDialog = true },
                onDownload = {},//todo()
                onDelete = model::deleteAlbum,
                onSelectedPhotosIds = {
                },
                isMainAlbum = idAlbum < 0,
                onClickAddPhotoTwo = { getImageNew = true },
                onClickMedia = {
                    model.goToViewScreen(it.id)
                }
            )
        }
        if (isViewRenameDialog) {
            DialogRenameAlbum(
                onDismiss = { isViewRenameDialog = false },
                text = album?.name ?: "",
                onChangeText = model::renameAlbum
            )
        }
        if (getImage) {
            DialogGetImageList(
                onDismiss = { getImage = false },
                getPhoto = {
                    model.uploadPhoto(it)
                    getImage = false
                }
            )
        }
        if (getImageNew) {
            DialogGetImageList(
                onDismiss = { getImageNew = false },
                getPhoto = {
                    if (it.isNotEmpty()) {
                        model.setImagesForUpload(it)
                        model.goToMetaScreen(album?.id ?: -1)
                    }
                    getImageNew = false
                }
            )
        }

    }
}

@Composable
fun AlbumContains(
    onClickBack: () -> Unit,
    onClickInfo: () -> Unit,
    onAddToFavorite: () -> Unit,
    onRename: () -> Unit,
    onDownload: () -> Unit,
    onDelete: () -> Unit,
    onClickMedia: (GettingMedia) -> Unit,
    onClickAddPhoto: () -> Unit,
    onClickAddPhotoTwo: () -> Unit,
    nameAlbum: String,
    listMedia: List<GettingMedia>,
    greedColumn: Int = 3,
    onSelectedPhotosIds: (List<Int>) -> Unit,
    isMainAlbum: Boolean
) {
    var expandedMero by rememberState { false }
    val selectedPhotos = remember { mutableStateListOf<Int>() }
    var isSelectedMode by rememberState { false }
    var isViewLoad by remember { mutableStateOf(true) }
    LaunchedEffect(key1 = listMedia) {
        isViewLoad = if (listMedia.isNotEmpty())
            false
        else {
            delay(3000L)
            false
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (isSelectedMode)
                TopAddingPhoto(
                    onClickBack = { isSelectedMode = false },
                    onClickSelect = {
                        isSelectedMode = false
                        onSelectedPhotosIds.invoke(selectedPhotos.toList())
                    },
                    text = if (selectedPhotos.size == 0) TextApp.textAddPhoto
                    else TextApp.textSelectedN(selectedPhotos.size)
                )
            else
                TopPanel(
                    onClickBack = onClickBack,
                    onClickInfo = onClickInfo,
                    onClickMero = { expandedMero = !expandedMero },
                    nameAlbum = nameAlbum,
                    isViewMero = expandedMero,
                    onAddToFavorite = onAddToFavorite,
                    onRename = onRename,
                    onDownload = onDownload,
                    onDelete = onDelete,
                    isMainAlbum = isMainAlbum
                )

            if (isViewLoad) {
                BoxFillWeight()
                ProgressIndicatorApp(
                    modifier = Modifier
                        .size(DimApp.iconSizeTouchStandard)
                        .align(Alignment.CenterHorizontally)
                )
                BoxFillWeight()
            }
            if (listMedia.isEmpty() && !isViewLoad)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextBodyLarge(
                            text = TextApp.textNoPhotoInAlbum,
                            textAlign = TextAlign.Center
                        )
                        BoxSpacer()
                        TextButtonApp(
                            onClick = onClickAddPhoto,
                            text = TextApp.textAddOnePhoto,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = DimApp.screenPadding),
                            colors = ColorButtonApp(
                                containerColor = ThemeApp.colors.backgroundVariant,
                                contentColor = ThemeApp.colors.primary,
                                disabledContainerColor = ThemeApp.colors.container,
                                disabledContentColor = ThemeApp.colors.textLight,
                            ),
                            contentStart = {
                                IconApp(
                                    modifier = Modifier.padding(end = DimApp.screenPadding * .3f),
                                    painter = rememberImageRaw(id = R.raw.ic_add)
                                )
                            })
                    }
                }

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                columns = GridCells.Fixed(greedColumn),
                horizontalArrangement = Arrangement.spacedBy(
                    0.dp,
                    Alignment.CenterHorizontally
                )
            ) {

                itemsIndexed(
                    items = listMedia,
                    key = { _, it -> it.id },
                    contentType = { _, it -> it.is_video }
                ) { index, item ->
                    val padding = when (index % greedColumn) {
                        0    -> PaddingValues(start = DimApp.screenPadding)
                        2    -> PaddingValues(end = DimApp.screenPadding)
                        else -> PaddingValues(horizontal = DimApp.screenPadding * .5f)
                    }
                    when (item.is_video) {
                        true  -> {}
                        false -> {
                            ImageItem(
                                padding = padding,
                                onPhotoSelected = {
                                    if (isSelectedMode) {
                                        if (selectedPhotos.contains(item.id)) {
                                            selectedPhotos.remove(item.id)
                                        }
                                        else {
                                            selectedPhotos.add(item.id)
                                        }
                                    }
                                    else {
                                        onClickMedia.invoke(item)
                                    }
                                },
                                isSelected = selectedPhotos.contains(item.id),
                                image = item.url,
                                onLongPhotoSelected = { isSelectedMode = true },
                                isSelectedMode = isSelectedMode,
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButtonApp(
            modifier = Modifier
                .systemBarsPadding()
                .align(Alignment.BottomEnd)
                .padding(DimApp.screenPadding),
            onClick = onClickAddPhotoTwo) {
            IconApp(
                modifier = Modifier.rotate(45f),
                painter = rememberImageRaw(id = R.raw.ic_close)
            )
        }
    }
}

@Composable
private fun TopPanel(
    onClickBack: () -> Unit,
    onClickInfo: () -> Unit,
    onClickMero: () -> Unit,
    onAddToFavorite: () -> Unit,
    onRename: () -> Unit,
    onDownload: () -> Unit,
    onDelete: () -> Unit,
    isViewMero: Boolean,
    nameAlbum: String,
    isMainAlbum: Boolean = false
) {
    PanelNavBackTop(
        modifier = Modifier,
        onClickBack = onClickBack,
        text = nameAlbum,
    ) {
        BoxFillWeight()
        if (!isMainAlbum) {
            IconButtonApp(
                modifier = Modifier,
                onClick = onClickInfo
            ) {
                IconApp(painter = rememberImageRaw(R.raw.ic_info))
            }
            DropMenuInAlbumContains(
                expanded = isViewMero,
                onDismiss = onClickMero,
                onAddToFavorite = onAddToFavorite,
                onRename = onRename,
                onDownload = onDownload,
                onDelete = onDelete,
                content = {
                    IconButtonApp(
                        modifier = Modifier,
                        onClick = onClickMero
                    ) {
                        IconApp(painter = rememberImageRaw(R.raw.ic_mero_vert))
                    }
                })
        }
    }
}

@Composable
private fun TopAddingPhoto(
    onClickBack: () -> Unit,
    onClickSelect: () -> Unit,
    text: String,
) {
    PanelNavBackTop(
        modifier = Modifier,
        onClickBack = onClickBack,
        text = text,
        painter = rememberImageRaw(R.raw.ic_close)
    ) {
        IconButtonApp(
            modifier = Modifier,
            onClick = onClickSelect
        ) {
            IconApp(painter = rememberImageRaw(R.raw.ic_check), tint = ThemeApp.colors.primary)
        }
    }
}

@Composable
private fun TopInfo(
    onClickBack: () -> Unit,
    nameAlbum: String,
) {
    PanelNavBackTop(
        modifier = Modifier,
        onClickBack = onClickBack,
        text = nameAlbum,
        painter = rememberImageRaw(R.raw.ic_close)
    )
}

@Composable
fun AlbumInfo(
    onClickBack: () -> Unit,
    onCreate: (
        desc: String,
        isPrivate: Boolean
    ) -> Unit,
    nameAlbum: String,
    nameOwner: String,
    date: String,
    description: String,
) {
    var isVisibleEditText by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    var descriptionEnter by rememberState { TextFieldValue(description) }
    val countChar by rememberState(descriptionEnter.text) { descriptionEnter.text.length }
    var isPrivateEnter by rememberState { false }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        TopInfo(onClickBack = onClickBack, nameAlbum = nameAlbum)
        BoxSpacer()
        AnimatedVisibility(
            visible = !isVisibleEditText,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(
                    durationMillis = 200,
                    easing = LinearEasing
                )
            ),
            exit = slideOutHorizontally(
                animationSpec = tween(
                    durationMillis = 200,
                    easing = LinearEasing
                )
            ) + fadeOut(),
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextBodyMedium(
                        text = TextApp.textDescription,
                        modifier = Modifier
                    )
                    IconButtonApp(
                        modifier = Modifier,
                        onClick = {
                            isVisibleEditText = true
                        }
                    ) {
                        IconApp(painter = rememberImageRaw(R.raw.ic_edit))
                    }
                }
                TextTitleSmall(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding),
                    text = descriptionEnter.text
                )
            }
        }
        AnimatedVisibility(
            visible = isVisibleEditText,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(
                    durationMillis = 200,
                    easing = LinearEasing
                )
            ),
            exit = slideOutHorizontally(
                animationSpec = tween(
                    durationMillis = 200,
                    easing = LinearEasing
                )
            ) + fadeOut(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DimApp.screenPadding),
            ) {
                TextFieldOutlinesApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .minLinesHeight(8, ThemeApp.typography.bodyLarge),
                    value = descriptionEnter,
                    onValueChange = {
                        descriptionEnter = it
                    },
                    isError = countChar > 150,
                    placeholder = { Text(text = TextApp.textDescription) },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            isVisibleEditText = false
                        }
                    ),
                )
                TextCaption(text = "${countChar}/150", modifier = Modifier.align(Alignment.End))
            }
        }
        TextBodyMedium(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = DimApp.screenPadding,
                    end = DimApp.screenPadding,
                    top = DimApp.screenPadding
                ), text = TextApp.textOwner
        )
        TextTitleSmall(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding), text = nameOwner
        )
        TextBodyMedium(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = DimApp.screenPadding,
                    end = DimApp.screenPadding,
                    top = DimApp.screenPadding
                ), text = TextApp.textDate
        )
        TextTitleSmall(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding), text = date
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CheckerApp(
                checked = isPrivateEnter,
                onCheckedChange = {
                    isPrivateEnter = !isPrivateEnter
                })
            TextBodyLarge(text = TextApp.textPrivateAlbum)
        }
        BoxFillWeight()
        PanelBottom(modifier = Modifier.align(Alignment.End)) {
            TextButtonApp(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = DimApp.screenPadding),
                colors = colorsButtonAccentTextApp().copy(contentColor = ThemeApp.colors.textDark),
                onClick = onClickBack,
                text = TextApp.titleCancel
            )
            ButtonAccentApp(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = DimApp.screenPadding),
                enabled = countChar <= 150,
                onClick = {
                    onCreate(descriptionEnter.text, isPrivateEnter)
                    onClickBack.invoke()
                },
                text = TextApp.titleCreate
            )
        }
    }
}


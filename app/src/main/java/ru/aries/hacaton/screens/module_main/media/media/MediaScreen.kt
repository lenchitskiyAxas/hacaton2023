package ru.aries.hacaton.screens.module_main.media.media

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.aries.hacaton.R
import ru.aries.hacaton.base.common_composable.BoxImageLoad
import ru.aries.hacaton.base.common_composable.BoxSpacer
import ru.aries.hacaton.base.common_composable.ContentButtonAddNew
import ru.aries.hacaton.base.common_composable.DialogBottomSheet
import ru.aries.hacaton.base.common_composable.DialogChoseAlbum
import ru.aries.hacaton.base.common_composable.DialogGetImageList
import ru.aries.hacaton.base.common_composable.FillLineHorizontal
import ru.aries.hacaton.base.common_composable.FloatingActionButtonApp
import ru.aries.hacaton.base.common_composable.IconApp
import ru.aries.hacaton.base.common_composable.PanelNavBackTop
import ru.aries.hacaton.base.common_composable.TextBodyLarge
import ru.aries.hacaton.base.common_composable.TextButtonApp
import ru.aries.hacaton.base.common_composable.TextCaption
import ru.aries.hacaton.base.common_composable.TextTitleSmall
import ru.aries.hacaton.base.common_composable.colorsButtonAccentTextApp
import ru.aries.hacaton.base.extension.clickableRipple
import ru.aries.hacaton.base.extension.toDateString
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberImageRaw
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.base.util.rememberState
import ru.aries.hacaton.models.api.GettingAlbum
import ru.aries.hacaton.models.api.GettingMedia
import ru.aries.hacaton.screens.module_main.media.MediaModel
import ru.aries.hacaton.screens.module_main.media.MenuMediaRibbon

class MediaScreen : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<MediaModel>()

        val menuMediaRibbon by model.menuMediaRibbon.collectAsState()
        val listMedia by model.listMedia.collectAsState()
        val listAlbum by model.listAlbum.collectAsState()
        var getImage by rememberState { false }

        var focusDetails by rememberState { false }
        val focusManager = LocalFocusManager.current

        LifecycleEffect(onStarted = {
            model.getMyMedia()
            model.getMyFamily()
        })

        Box(modifier = Modifier.fillMaxSize()) {
            MediaScr(
                onClickBack = model::goBackStack,
                menuMediaRibbon = menuMediaRibbon,
                onClickChooseMenu = model::chooseMenu,
                listMedia = listMedia,
                listAlbum = listAlbum,
                onClickShowAll = model::goToAllAlbum,
                onClickMedia = {
                    model.goToViewScreen(it.id)
                    //todo
                },
                onClickAlbum = { model.goToAlbum(it.id) },
            )

            FloatingActionButtonApp(
                modifier = Modifier
                    .systemBarsPadding()
                    .align(Alignment.BottomEnd)
                    .padding(DimApp.screenPadding),
                onClick = {
                    focusDetails = true
                }) {
                IconApp(
                    modifier = Modifier.rotate(45f),
                    painter = rememberImageRaw(id = R.raw.ic_close)
                )
            }
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
        if (focusDetails) {
            DialogBottomSheet(
                onDismiss = {
                    focusManager.clearFocus()
                    focusDetails = false
                },
                backgroundContent = ThemeApp.colors.background
            ) {
                DialogChoseAlbum(
                    onClickAlbum = model::goToAlbum,
                    onClickAddAlbum = model::goToCreateNewAlbum,
                    listAlbum = listAlbum,
                )
            }
        }
    }
}

@Composable
private fun MediaScr(
    onClickBack: () -> Unit,
    onClickShowAll: () -> Unit,
    onClickMedia: (GettingMedia) -> Unit,
    onClickAlbum: (GettingAlbum) -> Unit,
    listAlbum: List<GettingAlbum>,
    listMedia: List<GettingMedia>,
    onClickChooseMenu: (MenuMediaRibbon) -> Unit,
    menuMediaRibbon: MenuMediaRibbon,
    greedColumn: Int = 3,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
            .systemBarsPadding()
    ) {
        TopPanel(
            onClickBack = onClickBack,
            menuMediaRibbon = menuMediaRibbon,
            onClickChooseMenu = onClickChooseMenu
        )

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            columns = GridCells.Fixed(greedColumn),
            horizontalArrangement = Arrangement.spacedBy(
                0.dp,
                Alignment.CenterHorizontally
            ),
            content = {
                item(span = { GridItemSpan(greedColumn) }) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = DimApp.screenPadding)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        TextBodyLarge(text = TextApp.textAlbums)
                        TextButtonApp(onClick = onClickShowAll, text = TextApp.holderShowAllS)
                    }
                }

                item(span = { GridItemSpan(greedColumn) }) {
                    LazyRow(content = {
                        items(
                            items = listAlbum,
                            key = { it.id }) { item ->
                            Column(modifier = Modifier
                                .padding(
                                    start = DimApp.screenPadding,
                                    bottom = DimApp.screenPadding
                                )
                                .size(
                                    width = DimApp.sizeWidthCardAlbum,
                                    height = DimApp.sizeHeightCardAlbum
                                )
                                .shadow(
                                    elevation = DimApp.shadowElevation,
                                    shape = ThemeApp.shape.mediumAll
                                )
                                .background(ThemeApp.colors.backgroundVariant)
                                .clickableRipple { onClickAlbum.invoke(item) }) {

                                BoxImageLoad(
                                    modifier = Modifier
                                        .padding(DimApp.screenPadding * .5f)
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .clip(ThemeApp.shape.smallAll),
                                    drawableError = R.drawable.stub_photo,
//                                    drawablePlaceholder = R.drawable.stub_photo,
                                    contentScale = ContentScale.FillWidth,
                                    image = item.cover,
                                )

                                TextTitleSmall(
                                    modifier = Modifier
                                        .padding(horizontal = DimApp.screenPadding * .5f),
                                    text = item.description ?: "",
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis,
                                )

                                TextCaption(
                                    modifier = Modifier
                                        .padding(DimApp.screenPadding * .5f),
                                    text = item.created?.toDateString() ?: ""
                                )
                            }
                        }
                    })
                }

                item(span = { GridItemSpan(greedColumn) }) {
                    Column() {
                        BoxSpacer(.5f)
                        TextBodyLarge(
                            modifier = Modifier.padding(horizontal = DimApp.screenPadding),
                            text = TextApp.textAllFiles
                        )
                    }
                }

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
                            Column() {
                                BoxSpacer(.5f)
                                BoxImageLoad(
                                    modifier = Modifier
                                        .padding(padding)
                                        .aspectRatio(1f)
                                        .clip(ThemeApp.shape.smallAll)
                                        .clickableRipple { onClickMedia.invoke(item) },
                                    drawableError = R.drawable.stub_photo,
                                    drawablePlaceholder = R.drawable.stub_photo,
                                    image = item.url,
                                )
                            }

                        }
                    }
                }
            })
    }
}

@Composable
private fun TopPanel(
    onClickBack: () -> Unit,
    menuMediaRibbon: MenuMediaRibbon,
    onClickChooseMenu: (MenuMediaRibbon) -> Unit,
) {
    val menuList by rememberState { MenuMediaRibbon.values() }

    var offsetTargetDot by rememberState { 0.dp }
    val offsetDot by animateDpAsState(targetValue = offsetTargetDot)
    val des = LocalDensity.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(DimApp.shadowElevation)
            .background(ThemeApp.colors.backgroundVariant)
    ) {
        PanelNavBackTop(
            modifier = Modifier,
            onClickBack = onClickBack,
            text = TextApp.titleMedia
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            menuList.forEachIndexed { index, item ->
                TextButtonApp(
                    contentPadding = PaddingValues(DimApp.screenPadding * .5f),
                    modifier = Modifier.onGloballyPositioned {
                        if (menuMediaRibbon == item) {
                            offsetTargetDot =
                                with(des) { it.positionInWindow().x.toDp() + (it.size.width * .5f).toDp() }
                        }
                    },
                    colors = colorsButtonAccentTextApp().copy(
                        contentColor = if (menuMediaRibbon == item) {
                            ThemeApp.colors.primary
                        }
                        else {
                            ThemeApp.colors.textDark
                        }
                    ),
                    onClick = {
                        onClickChooseMenu.invoke(item)
                    },
                    text = item.getTextMenu()
                )
            }


        }
        Box(
            modifier = Modifier
                .offset(x = offsetDot - (DimApp.menuItemsWidth * .5f))
                .width(DimApp.menuItemsWidth)
                .height(DimApp.menuItemsHeight)
                .clip(ThemeApp.shape.smallTop)
                .background(ThemeApp.colors.primary)
        )
        FillLineHorizontal(modifier = Modifier.fillMaxWidth())

    }

}



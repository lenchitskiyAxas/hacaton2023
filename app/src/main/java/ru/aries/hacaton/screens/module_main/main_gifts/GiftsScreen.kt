package ru.aries.hacaton.screens.module_main.main_gifts

import androidx.annotation.RawRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.aries.hacaton.R
import ru.aries.hacaton.base.common_composable.BoxFillWeight
import ru.aries.hacaton.base.common_composable.BoxImageLoad
import ru.aries.hacaton.base.common_composable.BoxImageRowRes
import ru.aries.hacaton.base.common_composable.BoxSpacer
import ru.aries.hacaton.base.common_composable.DialogBottomSheet
import ru.aries.hacaton.base.common_composable.FloatingActionButtonApp
import ru.aries.hacaton.base.common_composable.IconApp
import ru.aries.hacaton.base.common_composable.IconButtonApp
import ru.aries.hacaton.base.common_composable.TextBodyLarge
import ru.aries.hacaton.base.common_composable.TextButtonStyle
import ru.aries.hacaton.base.common_composable.TextTitleMedium
import ru.aries.hacaton.base.extension.clickableRipple
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberImageRaw
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.base.util.rememberState
import ru.aries.hacaton.models.api.GettingWishlist

class GiftsScreen : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<GiftsModel>()
        val listWishes by model.listWishlists.collectAsState()
        var dialogChooseTypeAdd by rememberState { false }

        GiftsScr(
            listWishes = listWishes,
            onSearch = {},
            onClickAddFloat = {
                dialogChooseTypeAdd = true
            },
            onClickMoreWishlist = {
            },
            onClickWishlist = model::goInWishlist
        )

        if (dialogChooseTypeAdd) {
            DialogBottomSheet(
                onDismiss = {
                    dialogChooseTypeAdd = false
                },
                backgroundContent = ThemeApp.colors.backgroundVariant
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    TextBodyLarge(text = TextApp.textAddSomething)

                    BoxSpacer()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ButtonFlow(
                            id = R.raw.ic_gift,
                            body = TextApp.textWish
                        ) {
                            model.goToNewWish()
                            it.invoke()
                        }
                        BoxSpacer()

                        ButtonFlow(
                            id = R.raw.ic_create_new_folder,
                            body = TextApp.titleWishList
                        ) {
                            model.goToNewWishList()
                            it.invoke()
                        }
                    }
                    BoxSpacer(3f)
                }
            }
        }
    }
}


@Composable
private fun ButtonFlow(
    @RawRes id: Int,
    body: String,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(DimApp.sizeButtonForeGifts)
            .clip(ThemeApp.shape.smallAll)
            .background(ThemeApp.colors.background)
            .clickableRipple { onClick.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            BoxImageRowRes(
                modifier = Modifier.size(DimApp.iconSizeStandard),
                image = id,
                colorFilter = ColorFilter.tint(ThemeApp.colors.primary)

            )
            BoxSpacer(.5f)
            TextButtonStyle(text = body, color = ThemeApp.colors.primary)

        }
    }
}

@Composable
private fun GiftsScr(
    listWishes: List<GettingWishlist>,
    onSearch: (String) -> Unit,
    onClickAddFloat: () -> Unit,
    onClickWishlist: (GettingWishlist?) -> Unit,
    onClickMoreWishlist: (GettingWishlist?) -> Unit,
) {

    val greedColumn = 2
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
            .systemBarsPadding()
    ) {
        PanelTopGifts(
            onSearch = onSearch,
        )


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(DimApp.screenPadding),
                columns = GridCells.Fixed(greedColumn),
                verticalArrangement = Arrangement.spacedBy(DimApp.screenPadding),
                horizontalArrangement = Arrangement.spacedBy(
                    DimApp.screenPadding,
                    Alignment.CenterHorizontally
                ),
                content = {
                    item(span = { GridItemSpan(greedColumn) }) {
                        TextBodyLarge(text = TextApp.textWishlist)
                    }

                    item {
                        PreviewWishlist(
                            image = listWishes.firstOrNull()?.cover,
                            title = TextApp.textAllWishes,
                            onClickMore = null,
                            isPrivate = true,
                            onClickWishlist = { onClickWishlist.invoke(null) }

                        )
                    }

                    itemsIndexed(
                        items = listWishes,
                        key = { _, it -> it.id },
                    ) { _, item ->
                        PreviewWishlist(
                            image = item.cover,
                            title = item.title ?: "",
                            isPrivate = item.is_secret ?: false,
                            onClickMore = { onClickMoreWishlist.invoke(item) },
                            onClickWishlist = { onClickWishlist.invoke(item) }

                        )
                    }
                })


            FloatingActionButtonApp(
                modifier = Modifier
                    .systemBarsPadding()
                    .align(Alignment.BottomEnd)
                    .padding(DimApp.screenPadding),
                onClick = onClickAddFloat
            ) {
                IconApp(
                    modifier = Modifier.rotate(45f),
                    painter = rememberImageRaw(id = R.raw.ic_close)
                )
            }
        }
    }
}

@Composable
private fun PreviewWishlist(
    image: String?,
    title: String,
    isPrivate: Boolean,
    onClickMore: (() -> Unit)?,
    onClickWishlist: () -> Unit,
) {
    Box(
        modifier = Modifier
            .shadow(
                elevation = DimApp.shadowElevation,
                shape = ThemeApp.shape.smallAll
            )
            .background(ThemeApp.colors.backgroundVariant)
            .clickableRipple { onClickWishlist.invoke() }
    ) {
        Column {
            BoxImageLoad(
                modifier = Modifier
                    .padding(DimApp.screenPadding * 0.4f)
                    .aspectRatio(1f)
                    .clip(ThemeApp.shape.smallAll),
                drawableError = R.drawable.stub_photo_v2,
                drawablePlaceholder = R.drawable.stub_photo_v2,
                image = image,
            )

            Row(
                modifier = Modifier.height(DimApp.iconSizeStandard),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButtonStyle(
                    modifier = Modifier
                        .padding(start = DimApp.screenPadding * 0.5f),
                    maxLines = 1,
                    text = title
                )
                BoxFillWeight()

                onClickMore?.let {
                    BoxImageRowRes(
                        modifier = Modifier
                            .padding(end = DimApp.screenPadding * 0.5f)
                            .size(DimApp.iconSizeStandard)
                            .clip(CircleShape)
                            .clickableRipple { onClickMore.invoke() },
                        image = R.raw.ic_mero_vert,
                        colorFilter = ColorFilter.tint(ThemeApp.colors.textDark)
                    )

                }
            }
            BoxSpacer(.5f)
        }
        if (isPrivate) {
            BoxImageRowRes(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(DimApp.screenPadding)
                    .size(DimApp.iconSizeBig)
                    .clip(CircleShape)
                    .background(ThemeApp.colors.backgroundVariant)
                    .padding(DimApp.screenPadding * .2f),
                image = R.raw.ic_lock_fill,
            )
        }
    }
}

@Composable
private fun PanelTopGifts(
    onSearch: (String) -> Unit,
) {
    var isSearch by rememberState { false }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(DimApp.heightTopNavigationPanel)
            .shadow(elevation = DimApp.shadowElevation)
            .background(ThemeApp.colors.backgroundVariant),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        TextTitleMedium(
            modifier = Modifier
                .padding(start = DimApp.screenPadding)
                .fillMaxWidth()
                .weight(1f),
            textAlign = TextAlign.Start,
            text = TextApp.textGift
        )
        IconButtonApp(
            modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
            onClick = {
                isSearch = !isSearch
            }
        ) {
            IconApp(painter = rememberImageRaw(R.raw.ic_search))
        }
    }

}
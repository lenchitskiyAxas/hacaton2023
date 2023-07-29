package ru.aries.hacaton.base.common_composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.theme.ThemeApp
import kotlin.math.absoluteValue
import kotlin.math.sign

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerImageWithOutDownload(
    images: List<Any?>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isPagerIndicatorString: Boolean = false,
    isIndicatorOff: Boolean = false,
    isEnable: Boolean = false,
    imageHeight: Dp? = null,
    imageWith: Dp? = null,
    initialPage:Int = 0,
    isPagerIndicatorAlignment: Alignment = Alignment.BottomCenter,
    activeColorIndicator: Color = ThemeApp.colors.primary,
    inactiveColorIndicator: Color = ThemeApp.colors.background,
    paddingIndicator: Dp = DimApp.indicatorPadding,
    paddingContent: Dp = DimApp.screenPadding / 2,
    contentScale: ContentScale = ContentScale.FillWidth,
    onClick: (index: Int) -> Unit = {},
    itPageIndex: (Int) -> Unit = {},
) {
    var index by remember { mutableStateOf(0) }
    val size = (LocalConfiguration.current.screenWidthDp * 0.7).dp
    PagerApp(
        items = images,
        modifier = modifier,
        contentPadding = contentPadding,
        isIndicatorOff = isIndicatorOff,
        pagerState = rememberPagerState(initialPage = initialPage),
        isPagerIndicatorString = isPagerIndicatorString,
        isPagerIndicatorAlignment = isPagerIndicatorAlignment,
        activeColorIndicator = activeColorIndicator,
        inactiveColorIndicator = inactiveColorIndicator,
        itPageIndex = {
            index = it
            itPageIndex.invoke(it)
        }
    ) { image ->
        BoxImageLoad(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = paddingIndicator)
                .padding(horizontal = paddingContent)
                .then(if (imageHeight != null) Modifier.height(imageHeight) else Modifier)
                .then(if (imageWith != null) Modifier.height(imageWith) else Modifier)
                .clipToBounds()
                .clickable(
                    onClick = { onClick(index) },
                    role = Role.Button,
                    enabled = isEnable,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false)
                ),
            image = image,
            contentScale = contentScale,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> PagerApp(
    items: List<T>,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    indicatorPadding: PaddingValues = PaddingValues(DimApp.screenPadding),
    isPagerIndicatorString: Boolean = false,
    isIndicatorOff: Boolean = false,
    isPagerIndicatorAlignment: Alignment = Alignment.BottomEnd,
    activeColorIndicator: Color = ThemeApp.colors.primary,
    inactiveColorIndicator: Color = ThemeApp.colors.onPrimary,
    itPage: (T) -> Unit = {},
    itPageIndex: (Int) -> Unit = {},
    content: @Composable (T) -> Unit,
) {
    LaunchedEffect(key1 = pagerState.currentPage, block = {
        val item = items.getOrNull(pagerState.currentPage)
        itPageIndex(pagerState.currentPage)
        if (item != null) itPage(item)
    })

    if (items.isNotEmpty()) Box(modifier = modifier) {
        HorizontalPager(
            modifier = Modifier.align(Alignment.Center),
            contentPadding = contentPadding,
            pageCount = items.size,
            state = pagerState
        ) { page ->
            content(items[page])
        }

        if (!isPagerIndicatorString && !isIndicatorOff) HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(isPagerIndicatorAlignment)
                .padding(indicatorPadding),
            activeColor = activeColorIndicator,
            inactiveColor = inactiveColorIndicator,
            pageCount = items.size,
        )

        if (isPagerIndicatorString && !isIndicatorOff) Box(
            modifier = Modifier
                .wrapContentSize()
                .align(isPagerIndicatorAlignment)
                .padding(DimApp.screenPadding)
                .background(color = ThemeApp.colors.primary.copy(.3f), shape = CircleShape)
                .padding(DimApp.starsPadding), contentAlignment = Alignment.Center
        ) {
            TextCaption(text = "${pagerState.currentPage + 1} / ${items.size}")
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    pageCount: Int,
    pageIndexMapping: (Int) -> Int = { it },
    activeColor: Color = Color.Black.copy(0.4f),
    inactiveColor: Color = activeColor.copy(1.0f),
    indicatorWidth: Dp = 8.dp,
    indicatorHeight: Dp = indicatorWidth,
    spacing: Dp = indicatorWidth,
    indicatorShape: Shape = CircleShape,
) {

    val indicatorWidthPx = LocalDensity.current.run { indicatorWidth.roundToPx() }
    val spacingPx = LocalDensity.current.run { spacing.roundToPx() }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val indicatorModifier = Modifier
                .size(width = indicatorWidth, height = indicatorHeight)
                .background(color = inactiveColor, shape = indicatorShape)

            repeat(pageCount) {
                Box(indicatorModifier)
            }
        }

        Box(
            Modifier
                .offset {
                    val position = pageIndexMapping(pagerState.currentPage)
                    val offset = pagerState.currentPageOffsetFraction
                    val next = pageIndexMapping(pagerState.currentPage + offset.sign.toInt())
                    val scrollPosition = ((next - position) * offset.absoluteValue + position)
                        .coerceIn(
                            0f,
                            (pageCount - 1)
                                .coerceAtLeast(0)
                                .toFloat()
                        )

                    IntOffset(
                        x = ((spacingPx + indicatorWidthPx) * scrollPosition).toInt(),
                        y = 0
                    )
                }
                .size(width = indicatorWidth, height = indicatorHeight)
                .then(
                    if (pageCount > 0) Modifier.background(
                        color = activeColor,
                        shape = indicatorShape,
                    )
                    else Modifier
                )
        )
    }
}
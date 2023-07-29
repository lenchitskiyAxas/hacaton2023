package ru.aries.hacaton.base.common_composable

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Dimension
import coil.size.Size
import ru.aries.hacaton.R
import ru.aries.hacaton.base.theme.ThemeApp

/**
BoxImageLoad(
modifier = Modifier
.fillMaxWidth()
.background(Color.Black),
contentScale = contentScale,
image = R.raw.ic_navigation_bar,
isSVG = true
)
 * */

@Composable
fun BoxImageLoad(
    modifier: Modifier = Modifier,
    image: Any?,
    alignment: Alignment = Alignment.Center,
    sizeToIntrinsics: Boolean = true,
    contentScale: ContentScale = ContentScale.FillBounds,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    drawableError: Int? = R.drawable.image_hide,
    drawablePlaceholder: Int? = null,
    colorLoader: Color = ThemeApp.colors.primary,
    strokeWidthLoader: Dp = ProgressIndicatorDefaults.CircularStrokeWidth,
    modifierOnImage: Modifier = Modifier,
    content: @Composable BoxScope.(error: Boolean) -> Unit = {},
) {

    val isWebSVG = remember(image) {
        val isWebSVG = image.toString()
        isWebSVG.isNotEmpty() && isWebSVG.length > 3 && isWebSVG.takeLast(3).equals("svg", true)
    }

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    var sizeLoader by remember {
        mutableStateOf(0.dp)
    }

    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    val painter = rememberAsyncImagePainter(onLoading = {
        isLoading = true
        isError = false
    }, onError = {
        isLoading = false
        isError = true
    }, onSuccess = {
        isLoading = false
        isError = false
    }, model = ImageRequest.Builder(LocalContext.current).apply {
        size(
            if (isWebSVG) {
                Size(
                    with(density) { configuration.screenWidthDp.dp.roundToPx() },
                    Dimension.Undefined
                )
            } else {
                Size.ORIGINAL
            }
        )
        drawableError?.let { error(it) }
        drawablePlaceholder?.let { placeholder(it) }
        decoderFactory(SvgDecoder.Factory())
        data(image)
        crossfade(true)
    }.build())
    Box(modifier = modifier
        .onGloballyPositioned {
            sizeLoader = with(density) {
                (it.size.height / 2)
                    .toDp()
                    .coerceIn(
                        25.dp,
                        40.dp
                    )
            }
        }
        .paint(
            painter = painter ,
            alignment = alignment,
            sizeToIntrinsics = sizeToIntrinsics,
            contentScale = contentScale ,
            colorFilter = colorFilter,
            alpha = alpha
        )
        .then(modifierOnImage)) {
        content.invoke(this, isError)
        if (isLoading) CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(sizeLoader),
            color = colorLoader,
            strokeWidth = strokeWidthLoader,
        )
    }
}


@Composable
fun BoxImageRowRes(
    modifier: Modifier = Modifier,
    @RawRes image: Int,
    alignment: Alignment = Alignment.Center,
    sizeToIntrinsics: Boolean = true,
    contentScale: ContentScale = ContentScale.FillBounds,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    drawableError: Int? = R.drawable.image_hide,
    drawablePlaceholder: Int? = null,
    colorLoader: Color = ThemeApp.colors.primary,
    strokeWidthLoader: Dp = ProgressIndicatorDefaults.CircularStrokeWidth,
    modifierOnImage: Modifier = Modifier,
    content: @Composable BoxScope.(error: Boolean) -> Unit = {},
) {

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    var sizeLoader by remember { mutableStateOf(25.dp) }

    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    val painter = rememberAsyncImagePainter(onLoading = {
        isLoading = true
        isError = false
    }, onError = {
        isLoading = false
        isError = true
    }, onSuccess = {
        isLoading = false
        isError = false
    }, model = ImageRequest.Builder(LocalContext.current).apply {
        data(image)
        size(Size(with(density) { configuration.screenWidthDp.dp.roundToPx() }, Dimension.Undefined))
        drawableError?.let { error(it) }
        drawablePlaceholder?.let { placeholder(it) }
        decoderFactory(SvgDecoder.Factory())
        crossfade(false)
    }.build())
    Box(modifier = modifier
        .onGloballyPositioned {
            sizeLoader = with(density) {
                (it.size.height / 2)
                    .toDp()
                    .coerceIn(
                        25.dp,
                        40.dp
                    )
            }
        }
        .paint(
            painter = painter,
            alignment = alignment,
            sizeToIntrinsics = sizeToIntrinsics,
            contentScale = contentScale,
            colorFilter = colorFilter,
            alpha = alpha
        )
        .then(modifierOnImage)) {
        content.invoke(this, isError)
        if (isLoading) CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(sizeLoader),
            color = colorLoader,
            strokeWidth = strokeWidthLoader,
        )
    }
}

@Composable
fun BoxImageLoadSizeWidth(
    modifier: Modifier = Modifier,
    image: Any?,
    alignment: Alignment = Alignment.Center,
    sizeToIntrinsics: Boolean = true,
    contentScale: ContentScale = ContentScale.FillBounds,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    drawableError: Int? = R.drawable.image_hide,
    drawablePlaceholder: Int? = null,
    sizeWidth: Float = 0.9f,
    colorLoader: Color = ThemeApp.colors.primary,
    strokeWidthLoader: Dp = ProgressIndicatorDefaults.CircularStrokeWidth,
    modifierOnImage: Modifier = Modifier,
    content: @Composable BoxScope.(error: Boolean) -> Unit = {},
) {

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    var sizeLoader by remember {
        mutableStateOf(0.dp)
    }

    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    val painter = rememberAsyncImagePainter(onLoading = {
        isLoading = true
        isError = false
    }, onError = {
        isLoading = false
        isError = true
    }, onSuccess = {
        isLoading = false
        isError = false
    }, model = ImageRequest.Builder(LocalContext.current).apply {
        size(Size(
            with(density) { (configuration.screenWidthDp.dp * sizeWidth).roundToPx() },
            Dimension.Undefined
        ))
        drawableError?.let { error(it) }
        drawablePlaceholder?.let { placeholder(it) }
        decoderFactory(SvgDecoder.Factory())
        data(image)
        crossfade(true)
    }.build())
    Box(modifier = modifier
        .onGloballyPositioned {
            sizeLoader = with(density) {
                (it.size.height / 2)
                    .toDp()
                    .coerceIn(
                        25.dp,
                        40.dp
                    )
            }
        }
        .paint(
            painter = painter ,
            alignment = alignment,
            sizeToIntrinsics = sizeToIntrinsics,
            contentScale = contentScale ,
            colorFilter = colorFilter,
            alpha = alpha
        )
        .then(modifierOnImage)) {
        content.invoke(this, isError)
        if (isLoading) CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(sizeLoader),
            color = colorLoader,
            strokeWidth = strokeWidthLoader,
        )
    }
}

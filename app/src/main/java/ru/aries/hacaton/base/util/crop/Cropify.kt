package ru.aries.hacaton.base.util.crop

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import java.lang.Float.max
import java.lang.Float.min
import kotlin.math.roundToInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.aries.hacaton.base.util.rememberState

@Composable
fun Cropify(
    uri: Uri,
    state: CropifyState,
    onRotate:Int = 0,
    onImageCropped: (ImageBitmap) -> Unit,
    onFailedToLoadImage: (Throwable) -> Unit,
    modifier: Modifier = Modifier,
    option: CropifyOption = CropifyOption(),
) {
    BoxWithConstraints(modifier = modifier) {
        val context = LocalContext.current
        var sampledImageBitmap by remember(uri) { mutableStateOf<SampledImageBitmap?>(null) }

        LaunchedEffect(uri) {
            try {
                sampledImageBitmap = loadSampledImageBitmap(context, uri, constraints.run { IntSize(maxWidth, maxHeight) })
                state.loadedUri = uri
                state.inSampleSize = requireNotNull(sampledImageBitmap).inSampleSize
            } catch (t: Throwable) {
                sampledImageBitmap = null
                onFailedToLoadImage(t)
            }
        }

        if (sampledImageBitmap != null) {
            Cropify(
                bitmap = requireNotNull(sampledImageBitmap).imageBitmap,
                state = state,
                onRotate = onRotate,
                onImageCropped = onImageCropped,
                option = option,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}

@Composable
fun Cropify(
    bitmap: ImageBitmap,
    state: CropifyState,
    onImageCropped: (ImageBitmap) -> Unit,
    onRotate:Int,
    modifier: Modifier = Modifier,
    option: CropifyOption = CropifyOption(),
) {
    var bitmapRemember by rememberState { bitmap }


    LaunchedEffect(key1 = onRotate, block = {
        if (onRotate >0) {
            bitmapRemember = rotateBitmap(bitmapRemember)
        }
    })



    val density = LocalDensity.current
    val tolerance = remember { density.run { 24.dp.toPx() } }
    var touchRegion = remember<TouchRegion?> { null }

    BoxWithConstraints(
        modifier = modifier
            .pointerInput(bitmapRemember, option.frameAspectRatio) {
                detectDragGestures(
                    onDragStart = { touchRegion = detectTouchRegion(it, state.frameRect, tolerance) },
                    onDragEnd = { touchRegion = null }
                ) { change, dragAmount ->
                    touchRegion?.let {
                        when (it) {
                            is TouchRegion.Vertex -> state.scaleFrameRect(it, option.frameAspectRatio, dragAmount, tolerance * 2)
                            TouchRegion.Inside -> state.translateFrameRect(dragAmount)
                        }
                        change.consume()
                    }
                }
            }
    ) {
        val context = LocalContext.current
        LaunchedEffect(state.shouldCrop) {
            if (state.shouldCrop) {
                val loadedUri = state.loadedUri
                if (loadedUri != null) {
                    cropSampledImage(context, bitmapRemember, loadedUri, state.frameRect, state.imageRect, state.inSampleSize)
                } else {
                    cropImage(bitmapRemember, state.frameRect, state.imageRect)
                }
                val cropped = cropImage(bitmapRemember, state.frameRect, state.imageRect)
                state.shouldCrop = false
                onImageCropped(cropped)
            }
        }
        LaunchedEffect(bitmapRemember, option.frameAspectRatio, constraints) {
            val canvasSize = Size(constraints.maxWidth.toFloat(), constraints.maxHeight.toFloat())
            state.imageRect = calculateImagePosition(bitmapRemember, canvasSize)
            state.frameRect = calculateFrameRect(state.imageRect, canvasSize, option.frameAspectRatio)
        }
        ImageCanvas(
            bitmap = bitmapRemember,
            offset = state.imageRect.topLeft,
            size = state.imageRect.size,
            option = option,
            modifier = Modifier.matchParentSize(),
        )
        ImageOverlay(
            offset = state.frameRect.topLeft,
            size = state.frameRect.size,
            tolerance = tolerance,
            option = option,
            modifier = Modifier.matchParentSize()
        )
    }
}

private suspend fun cropSampledImage(
    context: Context,
    bitmap: ImageBitmap,
    uri: Uri,
    frameRect: Rect,
    imageRect: Rect,
    inSampleSize: Int
): ImageBitmap {
    return withContext(Dispatchers.IO) {
        if (inSampleSize > 1) {
            val fullImage = loadImageBitmap(context, uri)
            if (fullImage != null) cropImage(fullImage, frameRect, imageRect)
            else cropImage(bitmap, frameRect, imageRect)
        } else {
            cropImage(bitmap, frameRect, imageRect)
        }
    }
}

private suspend fun cropImage(
    bitmap: ImageBitmap,
    frameRect: Rect,
    imageRect: Rect,
): ImageBitmap {
    return withContext(Dispatchers.IO) {
        val scale = bitmap.width / imageRect.width
        Bitmap.createBitmap(
            bitmap.asAndroidBitmap(),
            ((frameRect.left - imageRect.left) * scale).roundToInt(),
            ((frameRect.top - imageRect.top) * scale).roundToInt(),
            (frameRect.width * scale).roundToInt(),
            (frameRect.height * scale).roundToInt(),
        ).asImageBitmap()
    }
}

internal fun calculateImagePosition(bitmap: ImageBitmap, canvasSize: Size): Rect {
    val imageSize = calculateImageSize(bitmap, canvasSize)
    return Rect(
        Offset((canvasSize.width - imageSize.width) / 2, (canvasSize.height - imageSize.height) / 2),
        imageSize
    )
}

internal fun calculateImageSize(bitmap: ImageBitmap, canvasSize: Size): Size {
    val newSize = Size(canvasSize.width, canvasSize.width * bitmap.height / bitmap.width.toFloat())
    return if (newSize.height > canvasSize.height)
        (canvasSize.height / newSize.height).let { Size(newSize.width * it, newSize.height * it) }
    else newSize
}

internal fun calculateFrameRect(
    imageRect: Rect,
    canvasSize: Size,
    frameAspectRatio: AspectRatio?,
): Rect {
    val shortSide = min(imageRect.width, imageRect.height)
    return if (frameAspectRatio == null) {
        Rect(center = imageRect.center, radius = shortSide * 0.8f / 2)
    } else {
        val scale = shortSide / max(imageRect.width, imageRect.width * frameAspectRatio.value)
        val size = Size(imageRect.width * scale * 0.8f, imageRect.width * scale * frameAspectRatio.value * 0.8f)
        Rect(Offset((canvasSize.width - size.width) / 2, (canvasSize.height - size.height) / 2), size)
    }
}

internal fun detectTouchRegion(tapPosition: Offset, frameRect: Rect, tolerance: Float): TouchRegion? {
    return when {
        Rect(frameRect.topLeft, tolerance).contains(tapPosition) -> TouchRegion.Vertex.TOP_LEFT
        Rect(frameRect.topRight, tolerance).contains(tapPosition) -> TouchRegion.Vertex.TOP_RIGHT
        Rect(frameRect.bottomLeft, tolerance).contains(tapPosition) -> TouchRegion.Vertex.BOTTOM_LEFT
        Rect(frameRect.bottomRight, tolerance).contains(tapPosition) -> TouchRegion.Vertex.BOTTOM_RIGHT
        Rect(frameRect.center, frameRect.width / 2 - tolerance).contains(tapPosition) -> TouchRegion.Inside
        else -> null
    }
}

fun rotateBitmap(bitmap: ImageBitmap , degrees: Float = 90f): ImageBitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)

    return Bitmap.createBitmap(
        bitmap.asAndroidBitmap(), 0, 0, bitmap.width, bitmap.height, matrix, true
    ).asImageBitmap()
}
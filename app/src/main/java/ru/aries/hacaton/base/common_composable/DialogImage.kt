package ru.aries.hacaton.base.common_composable

import android.net.Uri
import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.BackPressHandler
import ru.aries.hacaton.base.util.PermissionsModule
import ru.aries.hacaton.base.util.logD
import ru.aries.hacaton.data.gDMessage


@Composable
fun DialogGetImage(
    onDismiss: () -> Unit,
    uploadPhoto: (Uri) -> Unit,
) {
    var isViewDialog by remember { mutableStateOf(false) }
    val permission = PermissionsModule.launchPermissionCameraAndGallery { permition ->
        if (permition) {
            isViewDialog = true
        }
        else {
            isViewDialog = false
            onDismiss.invoke()
            gDMessage(TextApp.textMissingPermission)
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        permission.invoke()
    })


    if (isViewDialog) {
        Dialog(onDismissRequest = onDismiss) {
            val gallerySecondStep =
                PermissionsModule.galleryLauncher { uri ->
                    uploadPhoto(uri)
                    onDismiss()
                }
            val cameraLauncherStep =
                PermissionsModule.cameraLauncher { uri ->
                    uploadPhoto(uri)
                    onDismiss()
                }
            Column(
                modifier = Modifier
                    .clip(ThemeApp.shape.mediumAll)
                    .background(ThemeApp.colors.backgroundVariant)
                    .padding(DimApp.screenPadding)
            ) {

                ButtonAccentTextApp(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = cameraLauncherStep::invoke,
                    text = TextApp.textOpenTheCamera
                )
                BoxSpacer()
                ButtonAccentTextApp(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = gallerySecondStep::invoke,
                    text = TextApp.textOpenGallery
                )
                BoxSpacer()
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = onDismiss,
                    text = TextApp.titleCancel
                )
                BoxSpacer()
            }
        }
    }
}

@Composable
fun DialogGetImageList(
    onDismiss: () -> Unit,
    getPhoto: (List<Uri>) -> Unit,
) {
    var isViewDialog by remember { mutableStateOf(false) }
    val permission = PermissionsModule.launchPermissionCameraAndGallery { permition ->
        if (permition) {
            isViewDialog = true
        }
        else {
            isViewDialog = false
            onDismiss.invoke()
            gDMessage(TextApp.textMissingPermission)
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        permission.invoke()
    })


    if (isViewDialog) {
        Dialog(onDismissRequest = onDismiss) {
            val gallerySecondStep =
                PermissionsModule.getListImage { uris ->
                    getPhoto(uris)
                    onDismiss()
                }
            val cameraLauncherStep =
                PermissionsModule.cameraLauncher { uri ->
                    getPhoto(listOf(uri))
                    onDismiss()
                }
            Column(
                modifier = Modifier
                    .clip(ThemeApp.shape.mediumAll)
                    .background(ThemeApp.colors.backgroundVariant)
                    .padding(DimApp.screenPadding)
            ) {

                ButtonAccentTextApp(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = cameraLauncherStep::invoke,
                    text = TextApp.textOpenTheCamera
                )
                BoxSpacer()
                ButtonAccentTextApp(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = gallerySecondStep::invoke,
                    text = TextApp.textOpenGallery
                )
                BoxSpacer()
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = onDismiss,
                    text = TextApp.titleCancel
                )
                BoxSpacer()
            }
        }
    }
}

@Composable
fun ZoomImageDialog(
    image: String,
    dismiss: () -> Unit
) {

    BackPressHandler(onBackPressed = dismiss)
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context).data(image).crossfade(true).build()
    )
    val brush = Brush.radialGradient(
        colorStops = arrayOf(
            Pair(0.5F, ThemeApp.colors.onBackground),
            Pair(0.6F, ThemeApp.colors.background.copy(0.0F))
        )
    )

    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                awaitEachGesture {
                    MotionEvent.ACTION_UP
                    do {
                        val event = awaitPointerEvent()
                        if (event.calculateCentroidSize() > 0) {
                            scale *= event.calculateZoom()
                            val offset = event.calculatePan()
                            offsetX += offset.x
                            offsetY += offset.y
                        }
                    } while (event.changes.any { it.pressed })

                    if (scale < 0.2 || scale > 12) {
                        scale = 1f
                        offsetX = 0f
                        offsetY = 0f
                    }
                }
            }
            .fillMaxSize()
            .background(ThemeApp.colors.background)
            .systemBarsPadding()
    ) {

        Box(
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                )
                .align(Alignment.Center)
                .wrapContentSize()
                .paint(
                    painter = painter,
                    contentScale = ContentScale.FillWidth
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(DimApp.iconSizeTouchStandard)
                .background(brush = brush)
                .clip(CircleShape)
                .clickable(
                    onClick = dismiss,
                    role = Role.Button,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple()
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(DimApp.iconSizeStandard)
                    .paint(
                        painter = rememberVectorPainter(image = Icons.Filled.Close),
                        contentScale = ContentScale.FillBounds,
                        colorFilter = ColorFilter.tint(ThemeApp.colors.background)
                    )
            )
        }
    }
}



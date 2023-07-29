package ru.aries.hacaton.screens.module_main.media.zoom_image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.aries.hacaton.R
import ru.aries.hacaton.base.common_composable.BoxFillWeight
import ru.aries.hacaton.base.common_composable.BoxSpacer
import ru.aries.hacaton.base.common_composable.DialogDeletePhoto
import ru.aries.hacaton.base.common_composable.DropMenuInMedia
import ru.aries.hacaton.base.common_composable.IconApp
import ru.aries.hacaton.base.common_composable.IconButtonApp
import ru.aries.hacaton.base.common_composable.PagerImageWithOutDownload
import ru.aries.hacaton.base.common_composable.PanelNavBackTop
import ru.aries.hacaton.base.common_composable.ProgressIndicatorApp
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberImageRaw
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.base.util.rememberState
import ru.aries.hacaton.screens.module_main.media.MediaModel

class ImageViewScreen(
    private val idAlbum: Int?,
    private val mediaId: Int
) : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<MediaModel>()
        var isViewDelete by rememberState { false }
        val media by model.listMedia.collectAsState()
        var mediaNameDelete by rememberState { "" }
        var mediaIdDelete by rememberState { -1 }

        LifecycleEffect(onStarted = {
            if (idAlbum != null)
                model.getMediaByAlbum(idAlbum)
            else
                model.getMyMedia()
        })

        ImageView(
            onClickBack = model::goBackStack,
            onEdit = { currentIndex ->
                val mMedia = media.getOrNull(currentIndex)
                mMedia?.id?.let { model.goToEditScreen(it) }
            },
            onAddToFavorite = { currentIndex ->
                val mMedia = media.getOrNull(currentIndex)
                mMedia?.let { model.addToFavoriteMedia(it) }
            },
            onDownload = { currentIndex ->
                val mMedia = media.getOrNull(currentIndex)//todo()
            },
            onDelete = { currentIndex ->
                val mMedia = media.getOrNull(currentIndex)

                mMedia?.id?.let { mediaIdDelete = it }
                mMedia?.name?.let { mediaNameDelete = it }
                isViewDelete = true
            },
            imageList = media.map { it.url },
            currentIndexContent = media.indexOfFirst { it.id == mediaId },
            onGoToAlbum = { currentIndex ->
                val mMedia = media.getOrNull(currentIndex)
                model.goToAlbum(mMedia?.album_id ?: -1)
            },
        )
        if (isViewDelete)
            DialogDeletePhoto(
                namePhoto = mediaNameDelete,
                onDismiss = { isViewDelete = false },
                onYes = {
                    model.deleteMedia(mediaIdDelete)
                })
    }
}

@Composable
fun ImageView(
    onClickBack: () -> Unit,
    onAddToFavorite: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    onDownload: (Int) -> Unit,
    onGoToAlbum: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    imageList: List<Any?>,
    currentIndexContent: Int
) {
    var expandedMero by rememberState { false }
    var currentIndex by rememberState(currentIndexContent) { currentIndexContent }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.Black)
    ) {
        TopImageView(
            modifier = Modifier,
            onClickBack = onClickBack,
            onClickMero = { expandedMero = !expandedMero },
            nameAlbum = "${currentIndex + 1} из ${imageList.size}",
            isViewMero = expandedMero,
            onAddToFavorite = { onAddToFavorite(currentIndex) },
            onRename = { onEdit(currentIndex) },
            onDownload = { onDownload(currentIndex) },
            onDelete = { onDelete(currentIndex) },
            onGoToAlbum = { onGoToAlbum(currentIndex) },

            )
        BoxSpacer()
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (currentIndex == -1)
                ProgressIndicatorApp(
                    modifier = Modifier
                        .size(DimApp.iconSizeTouchStandard)
                )
            else
                PagerImageWithOutDownload(
                    modifier = Modifier
                        .fillMaxWidth(),
                    images = imageList,
                    isIndicatorOff = true,
                    paddingContent = 0.dp,
                    contentScale = ContentScale.FillBounds,
                    itPageIndex = {
                        currentIndex = it
                    },
                    initialPage = currentIndexContent,
                    onClick = {

                    }
                )
        }
        BoxSpacer()
    }
}

@Composable
private fun TopImageView(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit,
    onClickMero: () -> Unit,
    onAddToFavorite: () -> Unit,
    onRename: () -> Unit,
    onGoToAlbum: () -> Unit,
    onDownload: () -> Unit,
    onDelete: () -> Unit,
    isViewMero: Boolean,
    nameAlbum: String,
) {
    PanelNavBackTop(
        modifier = modifier,
        onClickBack = onClickBack,
        text = nameAlbum,
        container = Color.Black
    ) {
        BoxFillWeight()
        DropMenuInMedia(
            expanded = isViewMero,
            onDismiss = onClickMero,
            onAddToFavorite = onAddToFavorite,
            onGoToAlbum = onGoToAlbum,
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

@Composable
fun SwiperImage() {

}

package ru.aries.hacaton.screens.module_main.media

import android.net.Uri
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.aries.hacaton.base.BaseModel
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.data.GlobalDada
import ru.aries.hacaton.data.data_store.DataStorePrefs
import ru.aries.hacaton.data.gDSetListImage
import ru.aries.hacaton.data.gDSetLoader
import ru.aries.hacaton.models.api.GettingAlbum
import ru.aries.hacaton.models.api.GettingFamily
import ru.aries.hacaton.models.api.GettingMedia
import ru.aries.hacaton.models.local.DataForPostingMedia
import ru.aries.hacaton.screens.module_main.create_new_album.CreateNewAlbumScreen
import ru.aries.hacaton.screens.module_main.edit_media.EditMediaScreen
import ru.aries.hacaton.screens.module_main.media.album.AlbumScreen
import ru.aries.hacaton.screens.module_main.media.all_albums.AlbumAllScreen
import ru.aries.hacaton.screens.module_main.media.meta.MetaScreen
import ru.aries.hacaton.screens.module_main.media.zoom_image.ImageViewScreen
import ru.aries.hacaton.use_case.UseCaseAlbums
import ru.aries.hacaton.use_case.UseCaseFamily
import ru.aries.hacaton.use_case.UseCaseMedia

class MediaModel(
    private val apiMedia: UseCaseMedia,
    private val apiAlbum: UseCaseAlbums,
    private val apiFamily: UseCaseFamily,
    private val dataStore: DataStorePrefs
) : BaseModel() {

    private val _menuMediaRibbon = MutableStateFlow(MenuMediaRibbon.MY_MEDIA)
    val menuMediaRibbon = _menuMediaRibbon.asStateFlow()

    private val _listMedia = MutableStateFlow(listOf<GettingMedia>())
    val listMedia = _listMedia.asStateFlow()

    private val _listAlbum = MutableStateFlow(listOf<GettingAlbum>())
    val listAlbum = _listAlbum.asStateFlow()

    private val _currentAlbum = MutableStateFlow<GettingAlbum?>(null)
    val currentAlbum = _currentAlbum.asStateFlow()

    private val _listFamily = MutableStateFlow(listOf<GettingFamily>())
    val listFamily = _listFamily.asStateFlow()

    private val _imagesUpload = MutableStateFlow<List<DataForPostingMedia>?>(listOf())
    val imagesUpload = _imagesUpload.asStateFlow()


    private val gettingAlbumDefault = MutableStateFlow(
        GettingAlbum(
            name = TextApp.textMyAlbom,
            is_favorite = null,
            id = -1,
            created = 0,
            cover = null,
            owner = dataStore.UserData().get(),
            description = "Мой Альбом",
            location = null,
            custom_date = null,
            is_private = null,
        )
    )


    fun getMyFamily() = coroutineScope.launch {
        apiFamily.getMyFamily(
            flowStart = {},
            flowSuccess = {
                _listFamily.value = it
            },
            flowError = {},
            flowMessage = ::message
        )
    }


    fun chooseMenu(menu: MenuMediaRibbon) {
        _menuMediaRibbon.value = menu
        filterMedia()
        //TODO(Добавить действие на фильтр)
    }

    fun goToAlbum(idAlbum: Int) {
        navigator.push(AlbumScreen(idAlbum))
    }

    fun goToAllAlbum() {
        navigator.push(AlbumAllScreen())
    }

    fun goToViewScreen(mediaId: Int, idAlbum: Int? = null) {
        navigator.push(ImageViewScreen(idAlbum = idAlbum, mediaId = mediaId))
    }


    private fun filterMedia() {
        when (_menuMediaRibbon.value) {
            MenuMediaRibbon.MY_MEDIA  -> getMyMedia()
            MenuMediaRibbon.ALL       -> getAllMedia()
            MenuMediaRibbon.FAMILY    -> getFamilyMedia()
            MenuMediaRibbon.FAVORITES -> getFavoritesMedia()
        }
    }

    fun getMediaByAlbum(id: Int) = coroutineScope.launch {
        if (id < 0) {
            getMyMedia()
        }
        else {
            _listMedia.value = apiMedia.getMedias(
                albumIds = listOf(id),
            ).data?.ifEmpty { null } ?: listOf()
        }
    }

    fun getAlbum(id: Int) = coroutineScope.launch {
        if (id > -1)
            _currentAlbum.value =
                apiAlbum.getAlbumId(albumId = id).data ?: gettingAlbumDefault.value
        else _currentAlbum.value = gettingAlbumDefault.value
    }

    fun setAlbumIsFavorite() = coroutineScope.launch {
        _currentAlbum.value?.id?.let { id ->
            apiAlbum.putAlbumInFavorite(
                albumId = id,
                favorite = true,
                flowStart = {},
                flowSuccess = {},
                flowError = {},
                flowMessage = { message(it) }
            )
        }
    }

    fun downloadAlbum() = coroutineScope.launch {
        _currentAlbum.value?.let { apiAlbum.getAlbumDownload(it.id) }
    }

    fun addToFavoriteMedia(media: GettingMedia) = coroutineScope.launch {
        apiMedia.editMedia(
            idMedia = media.id,
            is_favorite = true,
            flowStart = {},
            flowSuccess = {},
            flowError = {},
            flowMessage = {})
    }

    fun goToEditScreen(id: Int) {
        navigator.push(EditMediaScreen(id))
    }

    fun goToMetaScreen(albumId:Int) {
        navigator.push(MetaScreen(albumId))
    }

    fun setImagesForUpload(images: List<Uri>) {
        val listMedia = mutableListOf<DataForPostingMedia>()
        images.forEach {
            listMedia.add(
                DataForPostingMedia(
                    uri = it,
                    name = null,
                    description = null,
                    happened = null,
                    is_favorite = null,
                    album_id = null,
                    address = null,
                    lat = null,
                    lon = null
                )
            )
        }
        gDSetListImage(listMedia)
    }

    fun getListImageForUpload() {
        _imagesUpload.value = GlobalDada.value?.listImageForUpload
    }

    fun uploadPhoto(image: List<Uri>) = coroutineScope.launch {

        val listMedia = mutableListOf<DataForPostingMedia>()
        image.forEach { image ->
            listMedia.add(
                DataForPostingMedia(
                    uri = image,
                    album_id = _currentAlbum.value?.id,
                    name = null,//todo()
                    is_favorite = true,//todo()
                    address = null,//todo()
                    lat = null,//todo()
                    lon = null,//todo()
                    happened = null,//todo()
                    description = null,//todo()
                )
            )
        }
        apiMedia.postNewMedia(
            mediaList = listMedia,
            flowStart = { gDSetLoader(true) },
            flowSuccess = {
                gDSetLoader(false)
                _listMedia.value = it
            },
            flowError = { gDSetLoader(false) },
            flowMessage = ::message,
        )
    }

    fun deleteMedia(id: Int) = coroutineScope.launch {
        apiMedia.deleteMedia(
            mediaId = id,
            flowStart = { gDSetLoader(true) },
            flowSuccess = {
                gDSetLoader(false)
                navigator.pop()
            },
            flowError = { gDSetLoader(false) },
            flowMessage = ::message
        )
    }

    fun deleteAlbum() = coroutineScope.launch {
        _currentAlbum.value?.id?.let { apiAlbum.deleteAlbum(it) }
        navigator.pop()
    }

    fun renameAlbum(name: String?) = coroutineScope.launch {
        _currentAlbum.value?.let { album ->
            apiAlbum.putAlbums(
                albumId = album.id,
                name = name,
                description = album.description,
                location = album.location,
                custom_date = album.custom_date,
                is_private = album.is_private ?: false,
                flowStart = {},
                flowSuccess = {},
                flowError = {},
                flowMessage = {})
        }
    }

    fun changeAlbum(description: String?, isPrivate: Boolean?) = coroutineScope.launch {
        _currentAlbum.value?.let { album ->
            apiAlbum.putAlbums(
                albumId = album.id,
                name = album.name,
                description = description,
                location = album.location,
                custom_date = album.custom_date,
                is_private = isPrivate ?: false,
                flowStart = {},
                flowSuccess = {},
                flowError = {},
                flowMessage = {})
        }
    }

    private fun addDefaultMyAlbum() = coroutineScope.launch {
        _listMedia.value = apiMedia.getMedias(
            isPersonal = true
        ).data?.ifEmpty { null } ?: listOf()
        gettingAlbumDefault.value =
            gettingAlbumDefault.value.copy(
                cover = _listMedia.value.firstOrNull()?.url ?: ""
            )//todo() оптимизировать

        _listAlbum.value = apiAlbum.getAlbums(
            isPersonal = true
        ).data?.ifEmpty { null } ?: listOf()

        val mut = mutableListOf(gettingAlbumDefault.value)
        mut += _listAlbum.value
        _listAlbum.value = mut
    }

    fun getMyMedia() = coroutineScope.launch {
        addDefaultMyAlbum()
    }

    private fun getAllMedia() = coroutineScope.launch {
        addDefaultMyAlbum()
    }

    fun getAllAlbum() = coroutineScope.launch {
        addDefaultMyAlbum()
    }

    private fun getFamilyMedia() = coroutineScope.launch {

        if (listFamily.value.isEmpty()) {
            _listMedia.value = listOf()
            _listAlbum.value = listOf()
        }
        else {
            _listMedia.value = apiMedia.getMedias(
                familyIds = listFamily.value.map { it.id }
            ).data ?: listOf()
            _listAlbum.value = apiAlbum.getAlbums(
                familyIds = listFamily.value.map { it.id }
            ).data ?: listOf()
        }
    }

    private fun getFavoritesMedia() = coroutineScope.launch {
        _listMedia.value = apiMedia.getMedias(
            isFavorite = true
        ).data ?: listOf()
        _listAlbum.value = apiAlbum.getAlbums(
            isFavorite = true
        ).data ?: listOf()
    }

    fun goToCreateNewAlbum() {
        navigator.push(CreateNewAlbumScreen())
    }
}


enum class MenuMediaRibbon {
    MY_MEDIA,
    ALL,
    FAMILY,
    FAVORITES;

    fun getTextMenu() = when (this) {
        MY_MEDIA  -> TextApp.titleMyMedia
        ALL       -> TextApp.titleAll
        FAMILY    -> TextApp.titleFamily
        FAVORITES -> TextApp.titleFavorites
    }
}





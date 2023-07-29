package ru.aries.hacaton.screens.module_main.profile

import android.net.Uri
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.aries.hacaton.base.BaseModel
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.data.gDSetLoader
import ru.aries.hacaton.data.gDSetScreenMain
import ru.aries.hacaton.models.api.City
import ru.aries.hacaton.models.api.GettingFamily
import ru.aries.hacaton.models.api.GettingMedia
import ru.aries.hacaton.models.api.GettingPost
import ru.aries.hacaton.models.api.GettingWish
import ru.aries.hacaton.models.local.DataForPostingMedia
import ru.aries.hacaton.screens.module_main.core_main.HomeMainScreen
import ru.aries.hacaton.screens.module_main.core_main.ScreensHome
import ru.aries.hacaton.screens.module_main.media.zoom_image.ImageViewScreen
import ru.aries.hacaton.screens.module_main.media.media.MediaScreen
import ru.aries.hacaton.screens.module_main.profile_redaction.ProfileRedactionScreen
import ru.aries.hacaton.use_case.UseCaseFamily
import ru.aries.hacaton.use_case.UseCaseLocations
import ru.aries.hacaton.use_case.UseCaseMedia
import ru.aries.hacaton.use_case.UseCasePosts
import ru.aries.hacaton.use_case.UseCaseUser
import ru.aries.hacaton.use_case.UseCaseWishesAndList

class ProfileModel(
    private val apiUser: UseCaseUser,
    private val apiFamily: UseCaseFamily,
    private val apiLocation: UseCaseLocations,
    private val apiMedia: UseCaseMedia,
    private val apiPosts: UseCasePosts,
    private val apiWishesAndList: UseCaseWishesAndList
) : BaseModel() {

    private val _userData = MutableStateFlow(apiUser.getUserLocalData())
    val userData = _userData.asStateFlow()

    private val _cityUser = MutableStateFlow<City?>(null)
    val cityUser = _cityUser.asStateFlow()

    private val _cityUserOrigin = MutableStateFlow<City?>(null)
    val cityUserOrigin = _cityUserOrigin.asStateFlow()

    private val _listFamily = MutableStateFlow(listOf<GettingFamily>())
    val listFamily = _listFamily.asStateFlow()

    private val _listMedia = MutableStateFlow(listOf<GettingMedia>())
    val listMedia = _listMedia.asStateFlow()

    private val _listWishes = MutableStateFlow(listOf<GettingWish>())
    val listWishes = _listWishes.asStateFlow()

    private val _listPosts = MutableStateFlow(listOf<GettingPost>())
    val listPosts = _listPosts.asStateFlow()

    private val _menuRibbon = MutableStateFlow(MenuRibbon.MEDIA)
    val menuRibbon = _menuRibbon.asStateFlow()

    private val _chooserFamily = MutableStateFlow<GettingFamily?>(null)
    val chooserFamily = _chooserFamily.asStateFlow()


    init {
        getMe()
        getMyFamily()
        initChooseFamilyId()
        getMedia()
        getPosts()
        getWishesAndList()
    }

    fun getMedia() = coroutineScope.launch {
        _listMedia.value = apiMedia.getMedias().data?.ifEmpty { null } ?: listOf()
    }

    fun getPosts() = coroutineScope.launch {
        _listPosts.value = apiPosts.getAllPosts().data?.ifEmpty { null } ?: listOf()
    }

    fun getWishesAndList() = coroutineScope.launch {
        _listWishes.value = apiWishesAndList.getWishes(page = 1).data?.ifEmpty { null } ?: listOf()
    }

    fun getMe() = coroutineScope.launch {
        apiUser.getMe(
            flowStart = {},
            flowSuccess = {
                _userData.value = it
            },
            flowError = {},
            flowMessage = ::message
        )
        getLocalization()
    }

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

    fun changeFamily(family: GettingFamily) = coroutineScope.launch {
        apiUser.setChooseFamilyId(family.id)
        _chooserFamily.value = family
    }

    private fun initChooseFamilyId() = coroutineScope.launch {
        apiUser.getChooseFamilyId()?.let { loadFamily(it) } ?: run {
            _listFamily.value.firstOrNull()?.let {
                apiUser.setChooseFamilyId(it.id)
                _chooserFamily.value = it
            }
        }
    }

    fun chooseMenu(menu: MenuRibbon) {
        _menuRibbon.value = menu
    }

    fun loadFamily(id: Int) = coroutineScope.launch {
        apiFamily.getFamilyId(
            id = id,
            flowStart = {},
            flowSuccess = {
                _chooserFamily.value = it
            },
            flowError = {},
            flowMessage = ::message
        )
    }

    private fun getLocalization() = coroutineScope.launch {
        _cityUser.value = _userData.value.location_id?.let { apiLocation.getDdCity(it) }
        _cityUserOrigin.value = _userData.value.birth_location_id?.let { apiLocation.getDdCity(it) }
    }

    fun goToRedaction() {
        getNavigationLevel(NavLevel.MAIN)?.push(ProfileRedactionScreen())
    }

    fun goToWishes() {
        gDSetScreenMain(ScreensHome.GIFTS_SCREEN)
        getNavigationLevel(NavLevel.MAIN)?.replaceAll(HomeMainScreen())
    }


    fun goToAllMedia() {
        getNavigationLevel(NavLevel.MAIN)?.push(MediaScreen())
    }


    fun uploadPhoto(image: List<Uri>) = coroutineScope.launch {

        val listMedia = mutableListOf<DataForPostingMedia>()
        image.forEach { image ->
            listMedia.add(
                DataForPostingMedia(
                    uri = image,
                    album_id = null,//загружает в общий альбом
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

    fun goToViewScreen(mediaId: Int, idAlbum: Int? = null) {
        navigator.push(ImageViewScreen(idAlbum = idAlbum, mediaId = mediaId))
    }
}


enum class MenuRibbon {
    MEDIA,
    WISHLIST,
    AFFAIRS,
    AWARDS;

    fun getTextMenu() = when (this) {
        MEDIA    -> TextApp.titleMedia
        WISHLIST -> TextApp.titleWishList
        AFFAIRS  -> TextApp.titleAffairs
        AWARDS   -> TextApp.titleAwards
    }
}

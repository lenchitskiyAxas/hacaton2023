package ru.aries.hacaton.di

import org.koin.dsl.module
import ru.aries.hacaton.data.api_client.ApiAlbums
import ru.aries.hacaton.data.api_client.ApiFamily
import ru.aries.hacaton.data.api_client.ApiInterests
import ru.aries.hacaton.data.api_client.ApiLocation
import ru.aries.hacaton.data.api_client.ApiMedia
import ru.aries.hacaton.data.api_client.ApiPosts
import ru.aries.hacaton.data.api_client.ApiRequestsFamily
import ru.aries.hacaton.data.api_client.ApiSignIn
import ru.aries.hacaton.data.api_client.ApiUser
import ru.aries.hacaton.data.api_client.ApiWishesAndList
import ru.aries.hacaton.data.api_client.Client
import ru.aries.hacaton.data.dao.DatabaseCities
import ru.aries.hacaton.data.data_store.DataStorePrefs
import ru.aries.hacaton.screens.module_authorization.AuthScreenModel
import ru.aries.hacaton.screens.module_main.create_new_album.CreateNewAlbumModel
import ru.aries.hacaton.screens.module_main.edit_media.EditMediaModel
import ru.aries.hacaton.screens.module_main.main_gifts.GiftsModel
import ru.aries.hacaton.screens.module_main.main_ribbon.RibbonModel
import ru.aries.hacaton.screens.module_main.media.MediaModel
import ru.aries.hacaton.screens.module_main.new_wish.NewWishModel
import ru.aries.hacaton.screens.module_main.new_wishlist.NewWishListModel
import ru.aries.hacaton.screens.module_main.profile.ProfileModel
import ru.aries.hacaton.screens.module_main.profile_redaction.ProfileRedactionModel
import ru.aries.hacaton.screens.module_registration.RegStepsModel
import ru.aries.hacaton.screens.splash.SplashScreenModel
import ru.aries.hacaton.use_case.UseCaseAlbums
import ru.aries.hacaton.use_case.UseCaseFamily
import ru.aries.hacaton.use_case.UseCaseInterests
import ru.aries.hacaton.use_case.UseCaseLocations
import ru.aries.hacaton.use_case.UseCaseMedia
import ru.aries.hacaton.use_case.UseCaseMembershipFamily
import ru.aries.hacaton.use_case.UseCasePosts
import ru.aries.hacaton.use_case.UseCaseSignIn
import ru.aries.hacaton.use_case.UseCaseUser
import ru.aries.hacaton.use_case.UseCaseWishesAndList


val setMainModule = module {
    single { DataStorePrefs(get()) }
    single { Client(get()) }
    single { DatabaseCities.build(get()) }
}

val setApiRoute = module {
    factory { ApiSignIn(get()) }
    factory { ApiUser(get()) }
    factory { ApiLocation(get()) }
    factory { ApiMedia(get()) }
    factory { ApiInterests(get()) }
    factory { ApiRequestsFamily(get()) }
    factory { ApiFamily(get()) }
    factory { ApiAlbums(get()) }
    factory { ApiPosts(get()) }
    factory { ApiWishesAndList(get()) }
}

val setUseCase = module {
    factory { UseCaseSignIn(get(), get()) }
    factory { UseCaseUser(get(), get()) }
    factory { UseCaseInterests(get()) }
    factory { UseCaseMembershipFamily(get()) }
    factory { UseCaseFamily(get()) }
    factory { UseCaseAlbums(get()) }
    factory { UseCaseMedia(get(), get()) }
    factory { UseCasePosts(get()) }
    factory { UseCaseWishesAndList(get()) }
    factory { UseCaseLocations(get(), get<DatabaseCities>().cityDb, get()) }
}

val setModels = module {
    single { RegStepsModel(get(), get(), get(), get(), get(), get()) }
    single { ProfileRedactionModel(get(), get(), get(), get()) }

    factory { AuthScreenModel(get()) }
    factory { SplashScreenModel(get(), get()) }
    factory { RibbonModel(get()) }
    factory { MediaModel(get(), get(), get(), get()) }
    factory { EditMediaModel(get()) }
    factory { CreateNewAlbumModel(get()) }
    factory { GiftsModel(get()) }
    factory { NewWishModel(get()) }
    factory { NewWishListModel(get()) }
    factory { ProfileModel(get(), get(), get(), get(), get(), get()) }
}
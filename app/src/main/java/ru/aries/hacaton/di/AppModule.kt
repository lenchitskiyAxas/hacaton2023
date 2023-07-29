package ru.aries.hacaton.di

import org.koin.dsl.module
import ru.aries.hacaton.data.api_client.ApiSignIn
import ru.aries.hacaton.data.api_client.Client
import ru.aries.hacaton.data.dao.DatabaseCities
import ru.aries.hacaton.data.data_store.DataStorePrefs
import ru.aries.hacaton.screens.module_authorization.AuthModel
import ru.aries.hacaton.screens.module_main.core_main.HomeMainModel
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
import ru.aries.hacaton.screens.splash.SplashModel
import ru.aries.hacaton.use_case.UseCaseSignIn


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

    factory { AuthModel(get()) }
    factory { SplashModel() }
    factory { RibbonModel(get()) }
    factory { MediaModel(get(), get(), get(), get()) }
    factory { EditMediaModel(get()) }
    factory { CreateNewAlbumModel(get()) }
    factory { HomeMainModel(get()) }
    factory { GiftsModel(get()) }
    factory { NewWishModel(get()) }
    factory { NewWishListModel(get()) }
    factory { ProfileModel(get(), get(), get(), get(), get(), get()) }
}
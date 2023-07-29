package ru.aries.hacaton.screens.module_main.new_wish

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.aries.hacaton.base.BaseModel
import ru.aries.hacaton.data.gDSetScreenMain
import ru.aries.hacaton.models.api.GettingWishlist
import ru.aries.hacaton.screens.module_main.core_main.HomeMainScreen
import ru.aries.hacaton.screens.module_main.core_main.ScreensHome
import ru.aries.hacaton.use_case.UseCaseWishesAndList

class NewWishModel(
    private val apiWishesAndList: UseCaseWishesAndList
) : BaseModel() {

    private val _idWishlist = MutableStateFlow<Int?>(null)
    val idWishlist = _idWishlist.asStateFlow()

    private val _listWishlists = MutableStateFlow(listOf<GettingWishlist>())
    val listWishlists = _listWishlists.asStateFlow()

    init {
        getWishesAndList()
    }


    private fun getWishesAndList() = coroutineScope.launch {
        _listWishlists.value = apiWishesAndList.getWishlist().data ?: listOf()
    }


    fun goToBackOnMain(){
        gDSetScreenMain(ScreensHome.GIFTS_SCREEN)
        getNavigationLevel(NavLevel.MAIN)?.replaceAll(HomeMainScreen())
    }

    fun addNewWishlist(idWishlist: Int){
        _idWishlist.value = idWishlist
    }

}
package ru.aries.hacaton.screens.module_main.main_gifts

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.aries.hacaton.base.BaseModel
import ru.aries.hacaton.models.api.GettingWishlist
import ru.aries.hacaton.screens.module_main.new_wish.NewWish
import ru.aries.hacaton.screens.module_main.new_wishlist.NewWishList
import ru.aries.hacaton.use_case.UseCaseWishesAndList

class GiftsModel(
    private val apiWishesAndList: UseCaseWishesAndList
) : BaseModel() {


    private val _listWishlists = MutableStateFlow(listOf<GettingWishlist>())
    val listWishlists = _listWishlists.asStateFlow()

    init {
        getWishesAndList()
    }


    private fun getWishesAndList() = coroutineScope.launch {
        _listWishlists.value = apiWishesAndList.getWishlist().data ?: listOf()
    }

    fun goInWishlist(wish :GettingWishlist?){
        if (wish == null){
            goToMyWishList()
            return
        }
        if (wish.wishes.isNullOrEmpty()){
            goToNewWish(wish.id)
            return
        }
        goToWishList(wish.id)
    }

    fun goToNewWish(idWishList: Int? = null) {
        getNavigationLevel(NavLevel.MAIN)?.push(NewWish(idWishList))
    }

    fun goToNewWishList() {
        getNavigationLevel(NavLevel.MAIN)?.push(NewWishList())
    }

    fun goToWishList(idWishList: Int) {

    }

    fun goToMyWishList() {

    }

}
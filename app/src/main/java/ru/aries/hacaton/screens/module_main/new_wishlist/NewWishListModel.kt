package ru.aries.hacaton.screens.module_main.new_wishlist

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.launch
import ru.aries.hacaton.base.BaseModel
import ru.aries.hacaton.data.gDSetLoader
import ru.aries.hacaton.models.api.CreatingWish
import ru.aries.hacaton.screens.module_main.new_wish.NewWish
import ru.aries.hacaton.use_case.UseCaseWishesAndList

class NewWishListModel(
    private val apiWishesAndList: UseCaseWishesAndList
) : BaseModel() {


    fun createNewWishList(
        title: String,
        isPrivate: Boolean
    ) = coroutineScope.launch {


        apiWishesAndList.postWishlist(
            title = title,
            isSecret = isPrivate,
            flowStart = { gDSetLoader(true) },
            flowSuccess = {
                gDSetLoader(false)
                getNavigationLevel(NavLevel.MAIN)?.push(NewWish(it.id))
            },
            flowError = {
                gDSetLoader(false)
            },
            flowMessage = ::message

        )

    }



}
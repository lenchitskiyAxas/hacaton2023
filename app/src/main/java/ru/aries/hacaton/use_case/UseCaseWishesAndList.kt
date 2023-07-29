package ru.aries.hacaton.use_case

import io.ktor.http.isSuccess
import ru.aries.hacaton.base.util.logE
import ru.aries.hacaton.data.api_client.ApiWishesAndList
import ru.aries.hacaton.models.api.CreatingWish
import ru.aries.hacaton.models.api.CreatingWishlist
import ru.aries.hacaton.models.api.GettingWish
import ru.aries.hacaton.models.api.GettingWishlist
import ru.aries.hacaton.models.api.RudApi

class UseCaseWishesAndList(
    private val apiWishesAndList: ApiWishesAndList
) {
    suspend fun getWishes(
        wishlistId: Int? = null,
        title: String? = null,
        page: Int? = null,
    ): RudApi<List<GettingWish>> = apiWishesAndList.getWishes(
        page = page,
        wishlistId = wishlistId,
        title = title
    )

    suspend fun postWish(
        bodyWish: CreatingWish,
        flowStart: () -> Unit = {},
        flowSuccess: (GettingWish) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val call = apiWishesAndList.postWishlist(bodyWish)

        if (!call.status.isSuccess()) {
            flowError.invoke()
            flowMessage.invoke(call.response.getDescriptionRudApi())
            logE("postWish", call)
            return
        }

        call.response.data?.let { newData ->
            flowSuccess.invoke(newData)
        } ?: run {
            flowError.invoke()
            flowMessage.invoke(call.response.getDescriptionRudApi())
            logE("postWish", call)
        }
    }

    suspend fun getWish(
        wishId: Int,
        flowStart: () -> Unit = {},
        flowSuccess: (GettingWish) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ){
        flowStart.invoke()

        val response = apiWishesAndList.getWish(wishId)

        if (!response.isSuccess) {
            flowError.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("getWish", response)
            return
        }

        response.data?.let { newData ->
            flowSuccess.invoke(newData)
        } ?: run {
            flowError.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("getWish", response)
        }
    }

    suspend fun putWish(
        wishId: Int,
        updatingWish: CreatingWish,
        flowStart: () -> Unit = {},
        flowSuccess: (GettingWish) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ){
        flowStart.invoke()

        val response = apiWishesAndList.putWish(wishId,updatingWish)

        if (!response.isSuccess) {
            flowError.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("putWish", response)
            return
        }

        response.data?.let { newData ->
            flowSuccess.invoke(newData)
        } ?: run {
            flowError.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("putWish", response)
        }
    }
    suspend fun postWishlist(
        title: String?= null,
        isSecret: Boolean?= null,
        authorIds: List<Int>? = null,
        flowStart: () -> Unit = {},
        flowSuccess: (GettingWishlist) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ){
        flowStart.invoke()

        val call = apiWishesAndList.postWishlist(CreatingWishlist(
            title = title,
            is_secret = isSecret,
            author_ids = authorIds

        ))

        if (!call.status.isSuccess()) {
            flowError.invoke()
            flowMessage.invoke(call.response.getDescriptionRudApi())
            logE("putWish", call)
            return
        }

        call.response.data?.let { newData ->
            flowSuccess.invoke(newData)
        } ?: run {
            flowError.invoke()
            flowMessage.invoke(call.response.getDescriptionRudApi())
            logE("putWish", call)
        }
    }

    suspend fun getWishlist(
        title: String? = null,
        page: Int? = null,
    ): RudApi<List<GettingWishlist>> = apiWishesAndList.getWishlists(
        page = page,
        title = title
    )

}
package ru.aries.hacaton.data.api_client

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.isSuccess
import ru.aries.hacaton.base.util.logE
import ru.aries.hacaton.models.api.CreatingWish
import ru.aries.hacaton.models.api.CreatingWishlist
import ru.aries.hacaton.models.api.GettingWish
import ru.aries.hacaton.models.api.GettingWishlist
import ru.aries.hacaton.models.api.RudApi
import ru.aries.hacaton.models.api.VerifyingEmailCode
import java.io.File

/**
 * Клиентское приложение / Желания + Вишлисты
 */
class ApiWishesAndList(private val client: Client) {

    /**Желания------------------------------------------------------------*/

    /**
     *  Получить Список Желаний
     *
     * @param [Uri] GET /api/users/me/wishes/
     * @param [Int] wishlist_id (query)
     * @param [String] title (query)
     * @param [Int] page (query)
     *
     * @return List [GettingWish]
     */
    suspend fun getWishes(
        wishlistId: Int?,
        title: String?,
        page: Int?,
    ): RudApi<List<GettingWish>> {
        return try {
            val queryParams = Parameters.build {
                wishlistId?.let { append("wishlist_id", wishlistId.toString()) }
                title?.let { append("title", title.toString()) }
                page?.let { append("page", page.toString()) }
            }
            val response = client.api.get(
                urlString = "/api/users/me/wishes/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )

            val data = response.body<RudApi<List<GettingWish>>>()
            data.isSuccess = response.status.isSuccess()
            data
        } catch (e: Exception) {
            logE("getWishes", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }


    /**
     *  Добавить Желание
     *
     * @param [Uri] POST /api/users/me/wishes/
     * @param [CreatingWish] {
     *
     *  "title": [String],
     *
     *  "description": [String],
     *
     *  "price": [Int],
     *
     *  "link": [String],
     *
     *   "wishlist_id": [Int]
     *
     * }
     * @return [GettingWish]
     */
    suspend fun postWishlist(
        bodyWish: CreatingWish,
    ) = client.api.postRequest<CreatingWish, GettingWish>(
        urlString = "/api/users/me/wishes/",
        body = bodyWish
    )

    /**
     *  Получить Желание
     *
     * @param [Uri] GET /api/users/wishes/{wish_id}/
     * @param [Int] wish_id (path)
     * @return [GettingWish]
     */
    suspend fun getWish(
        wishId: Int
    ): RudApi<GettingWish> {
        return try {
            val response = client.api.get(urlString = "/api/users/wishes/${wishId}/")
            response.body<RudApi<GettingWish>>().copy(
                isSuccess = response.status.isSuccess()
            )
        } catch (e: Exception) {
            logE("getAlbumId", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     *  Изменить Желание
     *
     * @param [Uri] PUT /api/users/me/wishes/{wish_id}/
     * @param [Int] wish_id (path)
     * @param [CreatingWish] {
     *
     *  "title": [String],
     *
     *  "description": [String],
     *
     *  "price": [Int],
     *
     *  "link": [String],
     *
     *   "wishlist_id": [Int]
     *
     * }
     * @return [GettingWish]
     */
    suspend fun putWish(
        wishId: Int,
        updatingWish: CreatingWish,
    ): RudApi<GettingWish> {
        return try {
            val response = client.api.putRequest(
                urlString = "/api/users/wishes/${wishId}/",
                body = updatingWish
            )
            response.body<RudApi<GettingWish>>().copy(isSuccess = response.status.isSuccess())
        } catch (e: Exception) {
            logE("putWish", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     *  Удалить Желание
     *
     * @param [Uri] DELETE /api/users/me/wishes/{wish_id}/
     * @param [Int] wish_id (path)

     * @return [Nothing]
     */
    suspend fun deleteWish(
        wishId: Int,
    ): RudApi<Any> {
        return try {
            val response = client.api.delete(
                urlString = "/api/users/wishes/${wishId}/",
            )
            response.body<RudApi<Any>>().copy(isSuccess = response.status.isSuccess())
        } catch (e: Exception) {
            logE("deleteAlbum", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     *  Изменить Обложку Желания
     *
     * @param [Uri] POST /api/users/me/wishes/{wish_id}/cover/
     * @param [Int] wish_id (path)
     * @param [File] new_cover (body)
     * @return [CreatingWish]
     */
    suspend fun postWishCover(
        wishId: Int,
        file: File,
    ) = client.api.postRequest<MultiPartFormDataContent, GettingWish>(
        urlString = "/api/users/me/wishes/${wishId}/cover/",
        body = MultiPartFormDataContent(formData {
            append("new_cover", file.readBytes(), Headers.build {
                append(HttpHeaders.ContentType, "image/*")
                append(HttpHeaders.ContentDisposition, "filename=${file.name}")
            })
        })
    )

    /**
     *  Забронировать Желание
     *
     * @param [Uri] POST /api/users/wishes/{wish_id}/fulfillments/
     * @param [Int] wish_id (path)
     * @return [CreatingWish]
     */
    suspend fun postWishFulfillment(
        wishId: Int,
    ): RudApi<GettingWish> {
        return try {
            val response = client.api.post(
                urlString = "/api/users/wishes/${wishId}/fulfillments/",
            )
            response.body<RudApi<GettingWish>>().copy(isSuccess = response.status.isSuccess())
        } catch (e: Exception) {
            logE("putWish", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }


    /**Вишлисты--------------------------------------------------------------*/

    /**
     *  Получить Список Желаний
     *
     * @param [Uri] GET /api/users/me/wishlists/
     * @param [String] title (query)
     * @param [Int] page (query)
     * @return List [GettingWishlist]
     */
    suspend fun getWishlists(
        title: String?,
        page: Int?,
    ): RudApi<List<GettingWishlist>> {
        return try {
            val queryParams = Parameters.build {
                title?.let { append("title", title.toString()) }
                page?.let { append("page", page.toString()) }
            }
            val response = client.api.get(
                urlString = "/api/users/me/wishlists/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
            response.body<RudApi<List<GettingWishlist>>>().copy(isSuccess = response.status.isSuccess())
        } catch (e: Exception) {
            logE("getWishlists", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     *  Добавить Вишлист
     *
     * @param [Uri] POST /api/users/me/wishlists/
     * @param [CreatingWishlist] {
     *
     *  "title": [String],
     *
     *  "is_secret": [Boolean],
     *
     *  "author_ids": List [Int],
     *
     * }
     * @return [GettingWishlist]
     */
    suspend fun postWishlist(
        bodyWishlist: CreatingWishlist,
    ) = client.api.postRequest<CreatingWishlist, GettingWishlist>(
        urlString = "/api/users/me/wishlists/",
        body = bodyWishlist
    )

    /**
     *  Получить Вишлист
     *
     * @param [Uri] GET /api/users/wishlists/{wishlist_id}/
     * @param [Int] wishlist_id (path)
     * @return [GettingWishlist]
     */
    suspend fun getWishlist(
        wishlistId: Int
    ): RudApi<GettingWishlist> {
        return try {
            val response = client.api.get(urlString = "/api/users/wishlists/${wishlistId}/")
            response.body<RudApi<GettingWishlist>>().copy(
                isSuccess = response.status.isSuccess()
            )
        } catch (e: Exception) {
            logE("getWishlist", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     *  Изменить Вишлист
     *
     * @param [Uri] PUT /api/users/me/wishlists/{wishlist_id}/
     * @param [Int] wishlist_id (path)
     * @param [CreatingWishlist] {
     *
     *  "title": [String],
     *
     *  "is_secret": [Boolean],
     *
     *  "author_ids": List [Int],
     *
     * }
     * @return [GettingWishlist]
     */
    suspend fun putWishlist(
        wishlistId: Int,
        bodyWishlist: CreatingWishlist,
    ): RudApi<GettingWishlist> {
        return try {
            val response = client.api.putRequest(
                urlString = "/api/users/me/wishlists/${wishlistId}/",
                body = bodyWishlist
            )
            response.body<RudApi<GettingWishlist>>().copy(isSuccess = response.status.isSuccess())
        } catch (e: Exception) {
            logE("putWishlist", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }


    /**
     *  Удалить Вишлист
     *
     * @param [Uri] DELETE /api/users/me/wishlists/{wishlist_id}/
     * @param [Int] wishlist_id (path)

     * @return [Nothing]
     */
    suspend fun deleteWishlist(
        wishlistId: Int,
    ): RudApi<Any> {
        return try {
            val response = client.api.delete(
                urlString = "/api/users/me/wishlists/${wishlistId}/",
            )
            response.body<RudApi<Any>>().copy(isSuccess = response.status.isSuccess())
        } catch (e: Exception) {
            logE("deleteAlbum", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }
}
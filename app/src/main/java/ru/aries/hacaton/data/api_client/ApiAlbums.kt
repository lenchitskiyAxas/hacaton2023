package ru.aries.hacaton.data.api_client

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.http.Parameters
import io.ktor.http.isSuccess
import ru.aries.hacaton.base.util.logE
import ru.aries.hacaton.models.api.CreatingAlbum
import ru.aries.hacaton.models.api.GettingAlbum
import ru.aries.hacaton.models.api.GettingMedia
import ru.aries.hacaton.models.api.IsFavoriteBody
import ru.aries.hacaton.models.api.RudApi

class ApiAlbums(
    private val client: Client
) {

    /**
     * GET /api/albums/ Получить Все Альбомы
     *
     * @since
     * name string (query)
     *
     * page integer (query)
     *
     * family_ids  array [integer] (query)
     *
     * is_personal boolean (query)
     *
     * is_private boolean (query)
     *
     * is_favorite boolean (query)
     *
     * @return
     * response [List] <[GettingAlbum]>
     *
     */
    suspend fun getAlbums(
        name: String? = null,
        page: Int? = null,
        familyIds: List<Int>? = null,
        isPersonal: Boolean? = null,
        isPrivate: Boolean? = null,
        isFavorite: Boolean? = null,
    ): RudApi<List<GettingAlbum>> {
        return try {
            val queryParams = Parameters.build {
                name?.let { append("name", name) }
                page?.let { append("page", page.toString()) }
                familyIds?.forEach { listIds -> append("family_ids", listIds.toString()) }
                isPersonal?.let { append("is_personal", isPersonal.toString()) }
                isPrivate?.let { append("is_private", isPrivate.toString()) }
                isFavorite?.let { append("is_favorite", isFavorite.toString()) }
            }
            val response = client.api.get(
                urlString = "/api/albums/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
            response.body<RudApi<List<GettingAlbum>>>()
                .copy(isSuccess = response.status.isSuccess())
        } catch (e: Exception) {
            logE("getAlbum", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     * POST /api/albums/  Создать Альбом
     *
     * @since
     * [CreatingAlbum] {
     * "name": "string",
     * "description": "string",
     * "location": "string",
     * "custom_date": 0,
     * "is_private": false
     * }
     * @return
     * response [GettingAlbum]
     */
    suspend fun postAlbums(
        bodyAlbum: CreatingAlbum,
    ) = client.api.postRequest<CreatingAlbum, GettingAlbum>(
        urlString = "/api/albums/",
        body = bodyAlbum
    )


    /**
     *  GET  /api/albums/{album_id}/ Получить Альбом По Идентификатору
     *
     * @since
     * album_id  integer (path)
     *
     * @return
     * response [GettingAlbum]
     */
    suspend fun getAlbumId(
        albumId: Int
    ): RudApi<GettingAlbum> {
        return try {
            val response = client.api.get(urlString = "/api/albums/${albumId}/")
            response.body<RudApi<GettingAlbum>>().copy(
                isSuccess = response.status.isSuccess()
            )
        } catch (e: Exception) {
            logE("getAlbumId", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     *  PUT  /api/albums/{album_id}/ Изменить Альбом
     *
     * @since
     * album_id  integer (path)
     * [CreatingAlbum] {
     * "name": "string",
     * "description": "string",
     * "location": "string",
     * "custom_date": 0,
     * "is_private": false
     * }
     * @return
     * response [GettingAlbum]
     *
     */
    suspend fun putAlbums(
        albumId: Int,
        updatingAlbum: CreatingAlbum,
    ): RudApi<GettingMedia> {
        return try {
            val response = client.api.putRequest(
                urlString = "/api/albums/${albumId}/",
                body = updatingAlbum
            )
            response.body<RudApi<GettingMedia>>().copy(isSuccess = response.status.isSuccess())
        } catch (e: Exception) {
            logE("putAlbums", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     * DELETE /api/albums/{album_id}/ Удалить Альбом
     *
     * @since
     * album_id  integer (path)
     *
     * @return
     * response [null]
     */
    suspend fun deleteAlbum(
        albumId: Int,
    ): RudApi<Any> {
        return try {
            val response = client.api.delete(
                urlString = "/api/albums/${albumId}/"
            )
            response.body<RudApi<Any>>().copy(isSuccess = response.status.isSuccess())
        } catch (e: Exception) {
            logE("deleteMe", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /** GET /api/albums/{album_id}/download/
    Получить Архив Альбома
     */
    suspend fun getAlbumDownload(
        albumId: Int
    ): RudApi<Any?> {
        return try {
            val response = client.api.get(urlString = "/api/albums/${albumId}/download/")
            response.body<RudApi<Any?>>().copy(
                isSuccess = response.status.isSuccess()
            )
        } catch (e: Exception) {
            logE("getAlbumDownload", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     *  PUT /api/albums/{album_id}/is-favorite/ Изменить Альбом
     *
     * @since
     * album_id  integer (path)
     *
     * [IsFavoriteBody] {
     * "is_favorite": true
     * }
     *
     * @return
     * response [null]
     *
     */
    suspend fun putAlbumInFavorite(
        albumId: Int,
        favorite: IsFavoriteBody,
    ): RudApi<GettingMedia> {
        return try {
            val response = client.api.putRequest(
                urlString = "/api/albums/${albumId}/is-favorite/",
                body = favorite
            )
            response.body<RudApi<GettingMedia>>().copy(isSuccess = response.status.isSuccess())
        } catch (e: Exception) {
            logE("putAlbumInFavorite", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }


}
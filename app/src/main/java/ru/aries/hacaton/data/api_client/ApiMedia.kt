package ru.aries.hacaton.data.api_client

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.isSuccess
import org.apache.tika.Tika
import ru.aries.hacaton.base.util.logE
import ru.aries.hacaton.models.api.CreatingAlbum
import ru.aries.hacaton.models.api.GettingAlbum
import ru.aries.hacaton.models.api.GettingMedia
import ru.aries.hacaton.models.api.RudApi
import ru.aries.hacaton.models.api.UpdatingMedia
import java.io.File

class ApiMedia(
    private val client: Client,
) {

    /**
     * GET [Client.BASE_URL] /api/users/me/medias/
     *
     * Получить Все Медиа
     *
     * @since
     *  name  [String]  (query)
     *
     *  extension [String] (query)
     *
     *  is_favorite [Boolean] (query)
     *
     *  page [Int] (query)
     *
     * @return
     * response [List]<[GettingMedia]>
     *
     */
    suspend fun getMedias(
        name: String? = null,
        extension: String? = null,
        albumIds: List<Int>? = null,
        isFavorite: Boolean? = null,
        withoutAlbum: Boolean? = null,
        familyIds: List<Int>? = null,
        isPersonal: Boolean? = null,
        isPrivate: Boolean? = null,
        page: Int? = null,
    ): RudApi<List<GettingMedia>> {
        return try {
            val queryParams = Parameters.build {
                name?.let { append("name", name) }
                extension?.let { append("extension", extension) }
                albumIds?.let { listIds -> appendAll("album_ids", listIds.map { it.toString() }) }
                isFavorite?.let { append("is_favorite", isFavorite.toString()) }
                withoutAlbum?.let { append("without_album", withoutAlbum.toString()) }
                familyIds?.let {listIds ->  appendAll("family_ids", listIds.map { it.toString() }) }
                isPersonal?.let { append("is_personal", isPersonal.toString()) }
                isPrivate?.let { append("is_private", isPrivate.toString()) }
                page?.let { append("page", page.toString()) }
            }
            val response = client.api.get(
                urlString = "/api/users/me/medias/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
            response.body<RudApi<List<GettingMedia>>>()
                .copy(isSuccess = response.status.isSuccess())
        } catch (e: Exception) {
            logE("getMedias", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }


    /**
     * GET [Client.BASE_URL]  /api/users/me/medias/{media_id}/
     *
     * Получить Медиа По Id
     *
     * @since
     *  media_id  [Int]  (query)
     *
     * @return
     * response [GettingMedia]
     *
     */
    suspend fun getMediaById(
        media_id:Int
    ): RudApi<GettingMedia> {
        return try {
            val response = client.api.get(
                urlString = "/api/users/me/medias/${media_id}/",
            )
            response.body<RudApi<GettingMedia>>()
                .copy(isSuccess = response.status.isSuccess())
        } catch (e: Exception) {
            logE("getMediaById", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     * POST  [Client.BASE_URL] /api/users/me/medias/
     *
     * Добавить медиа
     *
     * Request body  new_file string($binary) multipart/form-data
     *
     * response [Nothing]
     *
     */
    suspend fun postMyMedia(
        file: File,
    ) = client.api.postRequest<MultiPartFormDataContent, GettingMedia>(
        urlString = "/api/albums/",
        body = MultiPartFormDataContent(formData {
            append("new_file", file.readBytes(), Headers.build {
                append(HttpHeaders.ContentType, Tika().detect(file))
                append(HttpHeaders.ContentDisposition, "filename=${file.name}")
            })
        })
    )

    /**
     *  PUT [Client.BASE_URL] /api/users/me/medias/{media_id}/
     *
     *  Изменить Медиа
     *
     *  media_id integer (path)
     *
     *  Request body [UpdatingMedia] {
     *      "name": "string",
     *      "is_favorite": true,
     *      "album_id": 0
     * }
     *
     * response [Nothing]
     *
     */
    suspend fun putMyMedia(
        updatingMedia: UpdatingMedia,
        mediaId: Int,
    ): RudApi<GettingMedia> {
        return try {
            val response = client.api.putRequest(
                urlString = "/api/users/me/medias/${mediaId}/",
                body = updatingMedia
            )
            response.body<RudApi<GettingMedia>>().copy(isSuccess = response.status.isSuccess())
        } catch (e: Exception) {
            logE("putMyMedia", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     *  DELETE [Client.BASE_URL] /api/users/me/medias/{media_id}/
     *
     *  Удалить Медиа
     *
     *  media_id integer (path)
     *
     *  response [null]
     */

    suspend fun deleteMedia(
        mediaId: Int,
    ): RudApi<Any> {
        return try {
            val response = client.api.delete(
                urlString = "/api/users/me/medias/${mediaId}/"
            )
            response.body<RudApi<Any>>().copy(isSuccess = response.status.isSuccess())
        } catch (e: Exception) {
            logE("deleteMe", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }
}
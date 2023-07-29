package ru.aries.hacaton.use_case

import io.ktor.http.isSuccess
import ru.aries.hacaton.base.util.logE
import ru.aries.hacaton.data.api_client.ApiAlbums
import ru.aries.hacaton.models.api.CreatingAlbum
import ru.aries.hacaton.models.api.GettingAlbum
import ru.aries.hacaton.models.api.GettingMedia
import ru.aries.hacaton.models.api.IsFavoriteBody
import ru.aries.hacaton.models.api.RudApi

class UseCaseAlbums(
    private val apiAlbums: ApiAlbums
) {


    suspend fun getAlbums(
        name: String? = null,
        page: Int? = null,
        familyIds: List<Int>? = listOf(),
        isPersonal: Boolean? = null,
        isPrivate: Boolean? = null,
        isFavorite: Boolean? = null,
    ): RudApi<List<GettingAlbum>> = apiAlbums.getAlbums(
        name = name,
        page = page,
        familyIds = familyIds,
        isPersonal = isPersonal,
        isPrivate = isPrivate,
        isFavorite = isFavorite
    )

    suspend fun postAlbums(
        bodyAlbum: CreatingAlbum,
        flowStart: () -> Unit = {},
        flowSuccess: (GettingAlbum) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val call = apiAlbums.postAlbums(bodyAlbum)

        if (!call.status.isSuccess()) {
            flowError.invoke()
            flowMessage.invoke(call.response.getDescriptionRudApi())
            logE("postAlbums", call)
            return
        }

        call.response.data?.let { newData ->
            flowSuccess.invoke(newData)
        } ?: run {
            flowError.invoke()
            flowMessage.invoke(call.response.getDescriptionRudApi())
            logE("postAlbums", call)
        }
    }

    suspend fun getAlbumId(
        albumId: Int
    ): RudApi<GettingAlbum> = apiAlbums.getAlbumId(
        albumId = albumId
    )

    suspend fun putAlbumInFavorite(
        albumId: Int,
        favorite: Boolean? = null,
        flowStart: () -> Unit = {},
        flowSuccess: (GettingMedia) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val response = apiAlbums.putAlbumInFavorite(
            albumId = albumId,
            favorite = IsFavoriteBody(is_favorite = favorite)
        )

        if (!response.isSuccess) {
            flowError.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("putAlbumInFavorite", response)
            return
        }

        response.data?.let { newData ->
            flowSuccess.invoke(newData)
        } ?: run {
            flowError.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("putAlbumInFavorite", response)
        }
    }

    suspend fun putAlbums(
        albumId: Int,
        name: String?,
        description: String?,
        location: String?,
        custom_date: Long?,
        is_private: Boolean = false, //default: false
        flowStart: () -> Unit = {},
        flowSuccess: (GettingMedia) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val response = apiAlbums.putAlbums(
            albumId = albumId,
            updatingAlbum = CreatingAlbum(
                name = name,
                description = description,
                location = location,
                custom_date = custom_date,
                is_private = is_private
            )
        )
        if (!response.isSuccess) {
            flowError.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("putAlbums", response)
            return
        }
        response.data?.let { newData ->
            flowSuccess.invoke(newData)
        } ?: run {
            flowError.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("putAlbums", response)
        }
    }

    suspend fun getAlbumDownload(
        id: Int
    ): RudApi<Any?> = apiAlbums.getAlbumDownload(
        albumId = id
    )


    suspend fun deleteAlbum(
        albumId: Int
    ): RudApi<Any> {
        val response = apiAlbums.deleteAlbum(albumId)

        if (!response.isSuccess) {
            logE("postAlbums", response.message)
        }
        return response
    }
}
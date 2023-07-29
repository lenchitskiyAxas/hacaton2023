package ru.aries.hacaton.use_case

import androidx.core.net.toFile
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import io.ktor.http.isSuccess
import ru.aries.hacaton.data.api_client.ApiMedia
import ru.aries.hacaton.data.data_store.DataStorePrefs
import ru.aries.hacaton.models.api.GettingMedia
import ru.aries.hacaton.models.api.RudApi
import ru.aries.hacaton.models.api.UpdatingMedia
import ru.aries.hacaton.models.local.DataForPostingMedia

class UseCaseMedia(
    private val apiMedia: ApiMedia,
    private val dataStore: DataStorePrefs
) {


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
    ): RudApi<List<GettingMedia>> = apiMedia.getMedias(
        name = name,
        extension = extension,
        isFavorite = isFavorite,
        page = page,
        albumIds = albumIds,
        withoutAlbum = withoutAlbum,
        familyIds = familyIds,
        isPersonal = isPersonal,
        isPrivate = isPrivate
    )

    suspend fun getMediaById(
        id:Int
    ): RudApi<GettingMedia> = apiMedia.getMediaById(
       media_id = id
    )

    suspend fun postNewMedia(
        mediaList: List<DataForPostingMedia>,
        flowStart: () -> Unit = {},
        flowSuccess: (List<GettingMedia>) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val listMediaUris = mutableListOf<GettingMedia>()
        mediaList.forEach { media ->
            val compressedImageFile = Compressor.compress(dataStore.context, media.uri.toFile()) {
                default(width = 1024)
            }
            val data = apiMedia.postMyMedia(file = compressedImageFile)
            if (!data.status.isSuccess()) {
                flowError.invoke()
                flowMessage.invoke(data.response.getDescriptionRudApi())
                return
            }
            val idMedia = data.response.data?.id ?: run {
                flowError.invoke()
                flowMessage.invoke(data.response.getDescriptionRudApi())
            }
            if (idMedia is Int) {
                val dataPut = apiMedia.putMyMedia(
                    updatingMedia = UpdatingMedia(
                        name = media.name,
                        is_favorite = media.is_favorite,
                        album_id = media.album_id,
                        address = media.address,
                        lat = media.lat,
                        lon = media.lon,
                        description = media.description,
                        happened = media.happened,
                    ), mediaId = idMedia
                )
                dataPut.data?.let { newData ->
                    listMediaUris.add(newData)
                } ?: run {
                    flowError.invoke()
                    flowMessage.invoke(data.response.getDescriptionRudApi())
                }
            }
        }

        flowSuccess.invoke(listMediaUris)

    }

    suspend fun editMedia(
        idMedia: Int,
        name: String? = null,
        is_favorite: Boolean? = null,
        album_id: Int? = null,
        address: String? = null,
        lat: Long? = null,
        lon: Long? = null,
        description: String? = null,
        happened: Int? = null,    //date - Int
        flowStart: () -> Unit = {},
        flowSuccess: (GettingMedia) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val data = apiMedia.putMyMedia(
            updatingMedia = UpdatingMedia(
                name = name,
                is_favorite = is_favorite,
                album_id = album_id,
                address = address,
                lat = lat,
                lon = lon,
                description = description,
                happened = happened,
            ), mediaId = idMedia
        )
        if (!data.isSuccess) {
            flowError.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            return
        }
        data.data?.let { newData ->
            flowSuccess.invoke(newData)
        } ?: run {
            flowError.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
        }
    }


    suspend fun deleteMedia(
        mediaId: Int,
        flowStart: () -> Unit = {},
        flowSuccess: () -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val data = apiMedia.deleteMedia(mediaId)
        if (!data.isSuccess) {
            flowError.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            return
        }
        flowSuccess.invoke()
    }
}
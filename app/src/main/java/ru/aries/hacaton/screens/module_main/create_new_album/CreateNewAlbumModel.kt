package ru.aries.hacaton.screens.module_main.create_new_album

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.launch
import ru.aries.hacaton.base.BaseModel
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.data.gDSetLoader
import ru.aries.hacaton.models.api.CreatingAlbum
import ru.aries.hacaton.screens.module_main.media.album.AlbumScreen
import ru.aries.hacaton.use_case.UseCaseAlbums

class CreateNewAlbumModel(
    private val apiAlbum: UseCaseAlbums,
) : BaseModel() {


    fun createAlbum(
        titleEnter: String,
        descriptionEnter: String,
        customDateEnter: Long?,
        addressEnter: String?,
        isPrivateEnter: Boolean,
    ) = coroutineScope.launch {
        apiAlbum.postAlbums(
            bodyAlbum = CreatingAlbum(
                name = titleEnter,
                description = descriptionEnter,
                location = addressEnter,
                custom_date = customDateEnter?.div(1000),
                is_private = isPrivateEnter
            ),
            flowStart = { gDSetLoader(true) },
            flowSuccess = {
                gDSetLoader(false)
                message(TextApp.textAlbumCreated)
                navigator.push(AlbumScreen(it.id))
            },
            flowError = {
                gDSetLoader(false)
                message(TextApp.errorCreateAlbum)
                navigator.pop()
            },
            flowMessage = ::message
        )
    }
}
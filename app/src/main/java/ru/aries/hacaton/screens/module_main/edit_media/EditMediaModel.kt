package ru.aries.hacaton.screens.module_main.edit_media

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.aries.hacaton.base.BaseModel
import ru.aries.hacaton.models.api.GettingMedia
import ru.aries.hacaton.use_case.UseCaseMedia


class EditMediaModel(
    private val apiMedia: UseCaseMedia,
) : BaseModel() {

    private val _media = MutableStateFlow<GettingMedia?>(null)
    val media = _media.asStateFlow()


    fun getMediaById(id: Int) = coroutineScope.launch {
        _media.value = apiMedia.getMediaById(id = id).data
    }

    fun editMedia(
        descriptionEnter: String,
        customDateEnter: Long?,
        addressEnter: String?,
    ) = coroutineScope.launch {

        _media.value?.id?.let { idMedia ->//todo() переделать
            apiMedia.editMedia(
                idMedia = idMedia,
                address = addressEnter,
                description = descriptionEnter,
                happened = customDateEnter?.toInt(),
                flowStart = {},
                flowSuccess = {},
                flowError = {},
                flowMessage = {},
            )
        }
    }
}
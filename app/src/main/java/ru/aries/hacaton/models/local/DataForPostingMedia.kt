package ru.aries.hacaton.models.local

import android.net.Uri

data class DataForPostingMedia(
    val uri: Uri,
    val name: String?,
    val description: String?,
    val happened: Int?,    //date - Int
    val is_favorite: Boolean?,
    val album_id: Int?,
    val address: String?,
    val lat: Long?,
    val lon: Long?
)

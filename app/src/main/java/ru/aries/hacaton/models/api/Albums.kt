package ru.aries.hacaton.models.api


data class CreatingAlbum(
    val name: String?,
    val description: String?,
    val location: String?,
    val custom_date: Long?,
    val is_private: Boolean = false, //default: false
)

data class GettingAlbum(
    val name: String?,
    val description: String?,
    val location: String?,
    val cover: String?, // TODO() :String
    val custom_date: Long?,
    val is_private: Boolean?,
    val id: Int,
    val created: Long,
    val owner: GettingUser,
    val is_favorite: Boolean?,
)
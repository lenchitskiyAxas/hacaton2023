package ru.aries.hacaton.models.api



data class GettingPost(
    val attachments: List<GettingAttachment>,
    val created: Int?,
    val id: Int,
    val is_vote: Boolean?,
    val polling: Any?,
    val published: Int?,
    val text: String?,
    val user: GettingUser,
    val votes_count: Int
){


    fun getUrlAttachments() =    this.attachments.map { it.url }



}



data class GettingAttachment(
    val name: String,
    val is_favorite: Boolean,
    val lat: Int?,
    val lon: Int?,
    val id: Int,
    val extension: String?,
    val url: String,
    val first_frame: String?,
    val duration: String?,
    val created: Int,
    val happened:Int?,
    val address:String?,
    val size: Int,
    val owner: GettingUser?,
    val is_video: Boolean = false,
)
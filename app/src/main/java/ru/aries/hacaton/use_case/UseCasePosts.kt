package ru.aries.hacaton.use_case

import ru.aries.hacaton.data.api_client.ApiPosts
import ru.aries.hacaton.models.api.GettingPost
import ru.aries.hacaton.models.api.RudApi

class UseCasePosts(
    private val apiPosts: ApiPosts
) {
    suspend fun getAllPosts(
        user_ids: List<Int>? = null,
        family_ids: List<Int>? = null,
        published_from: Int? = null,
        published_to: Int? = null,
        with_polling: Boolean? = null,
        with_text: Boolean? = null,
        with_photo: Boolean? = null,
        with_video: Boolean? = null,
        page: Int? = null,
    ):RudApi<List<GettingPost>> = apiPosts.getPosts(
        userIds = user_ids,
        familyIds = family_ids,
        publishedFrom = published_from,
        publishedTo = published_to,
        withPolling = with_polling,
        withText = with_text,
        withPhoto = with_photo,
        withVideo = with_video,
        page = page
    )
}
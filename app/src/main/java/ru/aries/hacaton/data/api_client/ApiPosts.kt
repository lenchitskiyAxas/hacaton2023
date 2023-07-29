package ru.aries.hacaton.data.api_client

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Parameters
import io.ktor.http.isSuccess
import ru.aries.hacaton.base.util.logE
import ru.aries.hacaton.models.api.GettingPost
import ru.aries.hacaton.models.api.RudApi

class ApiPosts(private val client: Client) {
    /**
     * Клиентское приложение / Посты
     */

    /**GET /api/posts/
    Получить Все Посты [List [GettingPost]]
     */
    suspend fun getPosts(
        userIds: List<Int>?,
        familyIds: List<Int>?,
        publishedFrom: Int?,
        publishedTo: Int?,
        withPolling: Boolean?,
        withText: Boolean?,
        withPhoto: Boolean?,
        withVideo: Boolean?,
        page: Int?,
    ): RudApi<List<GettingPost>> {
        return try {
            val queryParams = Parameters.build {
                userIds?.forEach { id -> append("user_ids", id.toString()) }
                familyIds?.forEach { id -> append("family_ids", id.toString()) }
                publishedFrom?.let { append("published_from", publishedFrom.toString()) }
                publishedTo?.let { append("published_to", publishedTo.toString()) }
                withPolling?.let { append("with_polling", withPolling.toString()) }
                withText?.let { append("with_text", withText.toString()) }
                withPhoto?.let { append("with_photo", withPhoto.toString()) }
                withVideo?.let { append("with_video", withVideo.toString()) }
                page?.let { append("page", page.toString()) }
            }
            val response = client.api.get(
                urlString = "/api/posts/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
            val data = response.body<RudApi<List<GettingPost>>>()
            data.isSuccess = response.status.isSuccess()
            data
        } catch (e: Exception) {
            logE("getPosts", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }
}
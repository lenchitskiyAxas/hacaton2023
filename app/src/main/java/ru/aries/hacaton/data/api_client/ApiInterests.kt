package ru.aries.hacaton.data.api_client

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Parameters
import io.ktor.http.isSuccess
import ru.aries.hacaton.base.util.logE
import ru.aries.hacaton.models.api.GettingInterest
import ru.aries.hacaton.models.api.RudApi

class ApiInterests(
    private val client: Client,
) {

    /**Получить Все Интересы [List [GettingInterest]] */
    suspend fun getInterests(
        page: Int? = null,
        name: String? = null,
    ): RudApi<List<GettingInterest>> {
        return try {
            val queryParams = Parameters.build {
                name?.let { append("name", name) }
                page?.let { append("page", page.toString()) }
            }
            val response = client.api.get(
                urlString = "/api/interests/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
            val data = response.body<RudApi<List<GettingInterest>>>()
            data.isSuccess = response.status.isSuccess()
            data
        } catch (e: Exception) {
            logE("getInterests", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }
}
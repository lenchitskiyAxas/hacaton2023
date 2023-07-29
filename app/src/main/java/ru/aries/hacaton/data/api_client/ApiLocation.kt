package ru.aries.hacaton.data.api_client

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Parameters
import io.ktor.http.isSuccess
import ru.aries.hacaton.base.util.logE
import ru.aries.hacaton.models.api.GettingLocation
import ru.aries.hacaton.models.api.RudApi

class ApiLocation(
    private val client: Client,
) {

    /**Получить Все Местоположения [List [GettingLocation]] */
    suspend fun getLocations(
        page: Int? = null,
        name: String? = null,
    ): RudApi<List<GettingLocation>> {
        return try {
            val queryParams = Parameters.build {
                name?.let { append("name", name) }
                page?.let { append("page", page.toString()) }
            }
            val response = client.api.get(
                urlString = "/api/locations/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
            val data = response.body<RudApi<List<GettingLocation>>>()
            data.isSuccess = response.status.isSuccess()
            data
        } catch (e: Exception) {
            logE("getLocations", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }
}
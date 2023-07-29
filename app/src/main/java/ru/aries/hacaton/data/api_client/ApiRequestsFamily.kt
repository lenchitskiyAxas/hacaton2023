package ru.aries.hacaton.data.api_client

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.isSuccess
import org.apache.tika.Tika
import ru.aries.hacaton.base.util.logE
import ru.aries.hacaton.models.api.BodyAny
import ru.aries.hacaton.models.api.CreatingAlbum
import ru.aries.hacaton.models.api.GettingAlbum
import ru.aries.hacaton.models.api.GettingFamilyRequest
import ru.aries.hacaton.models.api.GettingMedia
import ru.aries.hacaton.models.api.RudApi
import ru.aries.hacaton.models.api.UpdatingFamilyRequest

class ApiRequestsFamily(
    private val client: Client,
)  {
    /**
     * Клиентское приложение / Запросы на членство
     */

    /**
     * GET
     * /api/families/{family_id}/requests/
     *
     * Получить Все Запросы На Членство
     *
     * "List" [GettingFamilyRequest]
     *
     * family_id integer (path)
     *
     * first_name  string (query)
     *
     * last_name string (query)
     *
     * patronymic string (query)
     *
     * page integer (query)
    */

    suspend fun getFamiliesRequests(
        familyId: Int,
        page: Int = 1,
        firstName: String? = null,
        lastName: String? = null,
        patronymic: String? = null,
    ): RudApi<List<GettingFamilyRequest>> {
        return try {
            val queryParams = Parameters.build {
                firstName?.let { append("first_name", firstName) }
                lastName?.let { append("last_name", lastName) }
                patronymic?.let { append("patronymic", patronymic) }
                append("page", page.toString())
            }
            val response = client.api.get(
                urlString = "/api/families/${familyId}/requests/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
            val data = response.body<RudApi<List<GettingFamilyRequest>>>()
            data.isSuccess = response.status.isSuccess()
            data
        } catch (e: Exception) {
            logE("getFamiliesRequests", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }


    /**
     * POST
     * /api/families/{family_id}/requests/
     *
     * Отправить Запрос На Членство
     *
     * [GettingFamilyRequest]
     *
     * family_id integer (path)
     *
     * BodyAny{} // пустое тело
    */

    suspend fun postFamiliesRequests(
        familyId: Int,
    )= client.api.postRequest<BodyAny, GettingFamilyRequest>(
        urlString = "/api/families/${familyId}/requests/",
        body = BodyAny()
    )

    /**
     * GET
     * /api/families/requests/{request_id}/
     *
     * Получить Запрос На Членство По Идентификатору
     *
     * [GettingFamilyRequest]
     *
     * request_id integer (path)
    */

    suspend fun getFamiliesRequest(
        requestId: Int,
    ): RudApi<GettingFamilyRequest> {
        return try {
            val response = client.api.get(urlString ="/api/families/requests/${requestId}/")
            val data = response.body<RudApi<GettingFamilyRequest>>()
            data.isSuccess = response.status.isSuccess()
            data
        } catch (e: Exception) {
            logE("getFamiliesRequest", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     * PUT
     * /api/families/requests/{request_id}/
     * Изменить Запрос На Членство В Семье
     *
     * [GettingFamilyRequest]
     *
     * request_id integer (path)
     * body send [UpdatingFamilyRequest]
    */

    suspend fun putFamiliesRequest(
        requestId: Int,
        isApproved: Boolean
    )= client.api.getRequest<UpdatingFamilyRequest, GettingFamilyRequest>(
        urlString = "/api/families/requests/${requestId}/",
        body = UpdatingFamilyRequest(is_approved = isApproved)
    )

    /**
     * DELETE
     * /api/families/requests/{request_id}/
     * Удалить Запрос На Членство В Семье
     *
     * [Nothing]
     *
     * request_id integer (path)
     *
     */


    suspend fun deleteFamiliesRequest(
        requestId: Int,
    ): RudApi<Any> {
        return try {
            val response = client.api.delete(urlString ="/api/families/requests/${requestId}/")
            val data = response.body<RudApi<Any>>()
            data.isSuccess = response.status.isSuccess()
            data
        } catch (e: Exception) {
            logE("deleteFamiliesRequest", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

}
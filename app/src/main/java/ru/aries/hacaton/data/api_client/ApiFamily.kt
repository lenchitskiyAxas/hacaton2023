package ru.aries.hacaton.data.api_client

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.http.Parameters
import io.ktor.http.isSuccess
import ru.aries.hacaton.base.util.logE
import ru.aries.hacaton.models.api.CreatingAlbum
import ru.aries.hacaton.models.api.CreatingFamilyMember
import ru.aries.hacaton.models.api.GettingAlbum
import ru.aries.hacaton.models.api.GettingFamily
import ru.aries.hacaton.models.api.GettingFamilyMember
import ru.aries.hacaton.models.api.RudApi
import ru.aries.hacaton.models.api.UpdatingFamily

class ApiFamily(
    private val client: Client,
) {
    /**
     *  Клиентское приложение / Семьи
     */

    /**
     *  GET
     *  /api/users/me/families/
     *
     *  Получить Все Семьи
     *
     * name string (query)
     *
     * page integer (query)
     *
     * List [GettingFamily]
     */
    suspend fun getFamilies(
        page: Int = 1,
        name: String? = null,
    ): RudApi<List<GettingFamily>> {
        return try {
            val queryParams = Parameters.build {
                name?.let { append("name", name) }
                append("page", page.toString())
            }
            val response = client.api.get(
                urlString = "/api/users/me/families/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
            val data = response.body<RudApi<List<GettingFamily>>>()
            data.isSuccess = response.status.isSuccess()
            data
        } catch (e: Exception) {
            logE("getFamilies", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }


    /**
     *  GET
     *  /api/families/{family_id}/
     *
     *  Получить Семья По Идентификатору
     *
     *  family_id integer (path)
     *
     *  [GettingFamily]
     */

    suspend fun getFamilyId(
        familyId: Int
    ): RudApi<GettingFamily> {
        return try {
            val response = client.api.get(urlString = "/api/families/${familyId}/")
            val data = response.body<RudApi<GettingFamily>>()
            data.isSuccess = response.status.isSuccess()
            data
        } catch (e: Exception) {
            logE("getFamilyId", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     *  PUT
     *  /api/families/{family_id}/
     *
     *  Изменить Семья
     *
     *  family_id integer (path)
     *
     *  [UpdatingFamily] (body)
     *
     *  [GettingFamily] (response)
     */

    suspend fun putFamilyNameFromId(
        familyId: Int,
        newNameFamily: String
    ): RudApi<GettingFamily> {
        return try {
            val response = client.api.putRequest(
                urlString = "/api/families/${familyId}/",
                body = UpdatingFamily(newNameFamily)
            )
            val data = response.body<RudApi<GettingFamily>>()
            data.isSuccess = response.status.isSuccess()
            data
        } catch (e: Exception) {
            logE("putFamilyNameFromId", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     *  DELETE
     *  /api/families/{family_id}/
     *
     *  Удалить Семью
     *
     *  family_id integer (path)
     *
     *  [null] (response)
     */

    suspend fun deleteFamilyFromId(
        familyId: Int,
    ): RudApi<Any> {
        return try {
            val response = client.api.delete(
                urlString = "/api/families/${familyId}/"
            )
            val data = response.body<RudApi<Any>>()
            data.isSuccess = response.status.isSuccess()
            data
        } catch (e: Exception) {
            logE("putFamilyNameFromId", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     *  POST
     *  /api/families/
     *
     *  Создать Семью
     *
     *  [UpdatingFamily] (body)
     *
     *  [GettingFamily] (response)
     */
    suspend fun postCreateFamily(
        nameFamily: String?
    )= client.api.postRequest<UpdatingFamily, GettingFamily>(
        urlString = "/api/families/",
        body = UpdatingFamily(nameFamily)
    )

    /**
     *  Клиентское приложение / Члены семьи
     */

    /**
     *  GET
     *  /api/families/{family_id}/members/
     *
     *  Получить Всех Членов Семьи
     *
     *  family_id integer (path)
     *
     *  first_name string (query)
     *
     *  last_name string (query)
     *
     *  patronymic string (query)
     *
     *  role integer (query)
     *
     *  List [GettingFamilyMember]
     */

    suspend fun getFamilyMembers(
        familyId: Int,
        firstName: String?,
        lastName: String?,
        patronymic: String?,
        role: Int?,
        page: Int = 1,
    ): RudApi<List<GettingFamilyMember>> {
        return try {
            val queryParams = Parameters.build {
                firstName?.let { append("first_name", firstName) }
                lastName?.let { append("last_name", lastName) }
                patronymic?.let { append("patronymic", patronymic) }
                role?.let { append("role", role.toString()) }
                append("page", page.toString())
            }
            val response = client.api.get(
                urlString = "/api/families/${familyId}/members/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
            val data = response.body<RudApi<List<GettingFamilyMember>>>()
            data.isSuccess = response.status.isSuccess()
            data
        } catch (e: Exception) {
            logE("getFamilyMembers", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     *  POST
     *  /api/families/{family_id}/members/
     *
     *  Создать Члена Семьи
     *
     *  family_id integer (path)
     *
     *  [CreatingFamilyMember] (body)
     *
     *  [GettingFamilyMember] (response)
     *
     */

    suspend fun postCreateFamilyMember(
        familyId: Int,
        familyMember: CreatingFamilyMember,
    )= client.api.postRequest<CreatingFamilyMember, GettingFamilyMember>(
        urlString = "/api/families/${familyId}/members/",
        body = familyMember
    )

    /**
     *  GET
     *  /api/families/members/{member_id}/
     *
     *  Получить Члена Семьи По Идентификатору
     *
     *  member_id integer (path)
     *
     *  [GettingFamilyMember] (response)
     */

    suspend fun getFamilyMemberFromId(
        memberId: Int
    ): RudApi<GettingFamilyMember> {
        return try {
            val response = client.api.get(urlString = "/api/families/members/${memberId}/")
            val data = response.body<RudApi<GettingFamilyMember>>()
            data.isSuccess = response.status.isSuccess()
            data
        } catch (e: Exception) {
            logE("getFamilyMemberFromId", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     *  PUT
     *  /api/families/members/{member_id}/
     *
     *  Изменить Члена Семьи
     *
     *  member_id integer (path)
     *
     *  [GettingFamilyMember] (response)
     */


    suspend fun putUpdateFamilyMember(
        memberId: Int,
        familyMember: CreatingFamilyMember,
    ): RudApi<GettingFamilyMember> {
        return try {
            val response = client.api.putRequest(
                urlString = "/api/families/members/${memberId}/",
                body = familyMember
            )
            val data = response.body<RudApi<GettingFamilyMember>>()
            data.isSuccess = response.status.isSuccess()
            data
        } catch (e: Exception) {
            logE("putUpdateFamilyMember", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     *  DELETE
     *  /api/families/members/{member_id}/
     *
     *  Удалить Члена Семьи
     *
     *  member_id integer (path)
     *
     *  [Any] (response)
     */

    suspend fun deleteUpdateFamilyMember(
        memberId: Int,
    ): RudApi<Any> {
        return try {
            val response = client.api.delete(
                urlString = "/api/families/members/${memberId}/",
            )
            val data = response.body<RudApi<Any>>()
            data.isSuccess = response.status.isSuccess()
            data
        } catch (e: Exception) {
            logE("deleteUpdateFamilyMember", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }
}
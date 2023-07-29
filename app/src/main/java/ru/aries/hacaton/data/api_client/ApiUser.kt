package ru.aries.hacaton.data.api_client


import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import ru.aries.hacaton.base.util.logE
import ru.aries.hacaton.models.api.GettingUser
import ru.aries.hacaton.models.api.RudApi
import ru.aries.hacaton.models.api.SignUpEmail
import ru.aries.hacaton.models.api.TokenWithUser
import ru.aries.hacaton.models.api.UpdatingUser
import ru.aries.hacaton.models.api.VerifyingEmailCode
import ru.aries.hacaton.models.api.VerifyingTelCode
import java.io.File

class ApiUser(
    private val client: Client,
) {

    /**Получить Свой Профиль
     * RudApi [GettingUser]
     * */
    suspend fun getMe(
    ): RudApi<GettingUser> {
        return try {
            val response = client.api.get(urlString = "/api/users/me/")
            val data = response.body<RudApi<GettingUser>>()
            data.isSuccess = response.status.isSuccess()
            data
        } catch (e: Exception) {
            logE("getMe", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**Изменить Текущего Пользователя
     * RudApi [GettingUser]
     * */
    suspend fun putMe(
        user: UpdatingUser
    ): RudApi<GettingUser> {
        return try {
            val response = client.api.putRequest(
                urlString = "/api/users/me/",
                body = user
            )
            val data = response.body<RudApi<GettingUser>>()
            data.isSuccess = response.status.isSuccess()
            data
        } catch (e: Exception) {
            logE("putMe", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**Удалить Текущего Пользователя
     * RudApi [String]
     * */
    suspend fun deleteMe(
    ): RudApi<String> {
        return try {
            val response = client.api.delete(urlString = "/api/users/me/")
            val data = response.body<RudApi<String>>()
            data.isSuccess = response.status.isSuccess()
            data
        } catch (e: Exception) {
            logE("deleteMe", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**Изменить Аватар
     * RudApi [GettingUser]
     * */
    suspend fun postMyAvatar(
        file: File,
    ) = client.api.postRequest<MultiPartFormDataContent, GettingUser>(
        urlString = "/api/users/me/avatar/",
        body = MultiPartFormDataContent(formData {
            append("new_avatar", file.readBytes(), Headers.build {
                append(HttpHeaders.ContentType, "image/*")
                append(HttpHeaders.ContentDisposition, "filename=${file.name}")
            })
        })
    )

    /**Изменить Email
     * RudApi [String]
     * */
    suspend fun postMyEmail(
        email: String,
        code: String,
    ) = client.api.postRequest<VerifyingEmailCode, String>(
        urlString = "/api/users/me/email/",
        body = VerifyingEmailCode(
            email = email,
            code = code
        )
    )

    /**Изменить Номер Телефона
     * RudApi [String]
     * */
    suspend fun postMyTel(
        tel: String,
        code: String,
    ) = client.api.postRequest<VerifyingTelCode, String>(
        urlString = "/api/users/me/tel/",
        body = VerifyingTelCode(
            tel = tel,
            code = code
        )
    )
}
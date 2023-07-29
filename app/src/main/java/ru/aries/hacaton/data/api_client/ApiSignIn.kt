package ru.aries.hacaton.data.api_client


import io.ktor.client.call.body
import io.ktor.http.isSuccess
import ru.aries.hacaton.base.util.logE
import ru.aries.hacaton.models.api.BodyAny
import ru.aries.hacaton.models.api.CreatingEmailVerificationCode
import ru.aries.hacaton.models.api.CreatingTelVerificationCode
import ru.aries.hacaton.models.api.GettingFamilyRequest
import ru.aries.hacaton.models.api.GettingTelVerificationCode
import ru.aries.hacaton.models.api.LoginData
import ru.aries.hacaton.models.api.RudApi
import ru.aries.hacaton.models.api.SignUpEmail
import ru.aries.hacaton.models.api.TokenWithUser
import ru.aries.hacaton.models.api.VerificationCode

class ApiSignIn(
    private val client: Client,
) {

    /**Войти По Логину И Паролю
     * RudApi [TokenWithUser]
     * */
    suspend fun postSignIn(
        email: String,
        password: String,
    )= client.api.postRequest<LoginData, TokenWithUser>(
        urlString = "/api/sign-in/email/",
        body = LoginData(email = email, password = password)
    )

    /**Оправить Код Подтверждения На Телефон
     * RudApi [GettingTelVerificationCode]
     * */
    suspend fun postVerificationTel(
        tel: String,
    )= client.api.postRequest<CreatingTelVerificationCode, GettingTelVerificationCode>(
        urlString = "/api/verification-codes/tel/",
        body = CreatingTelVerificationCode(tel = tel)
    )

    /**Оправить Код Подтверждения На Email
     * RudApi [VerificationCode]
     * */
    suspend fun postEmailApi(
        email: String,
    )= client.api.postRequest<CreatingEmailVerificationCode, VerificationCode>(
        urlString = "/api/verification-codes/email/",
        body = CreatingEmailVerificationCode(email = email)
    )

    /**Регистрация С Помощью Email
     * RudApi [TokenWithUser]
     * */
    suspend fun postRegApi(
        email: String,
        password: String,
        code: String,
    )= client.api.postRequest<SignUpEmail, TokenWithUser>(
        urlString = "/api/sign-up/email/",
        body = SignUpEmail(
            email = email,
            password = password,
            code = code
        )
    )
}
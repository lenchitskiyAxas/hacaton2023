package ru.aries.hacaton.use_case

import io.ktor.http.isSuccess
import ru.aries.hacaton.data.api_client.ApiSignIn
import ru.aries.hacaton.data.data_store.DataStorePrefs
import ru.aries.hacaton.models.api.GettingUser
import ru.aries.hacaton.models.api.VerificationCode

class UseCaseSignIn(
    private val apiSignIn: ApiSignIn,
    private val dataStore: DataStorePrefs
) {

    fun getUserLocalData() = dataStore.UserData().get()

    suspend fun postAuthorization(
        email: String,
        password: String,
        flowStart: () -> Unit = {},
        flowSuccess: (GettingUser) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val data = apiSignIn.postSignIn(email = email, password = password)
        if (!data.status.isSuccess()) {
            flowError.invoke()
            flowMessage.invoke(data.response.getDescriptionRudApi())
            return
        }
        data.response.data?.let { newData ->
            dataStore.UserData().update { newData.user }
            dataStore.TokenData().putTokenServer(newData.token)
            flowSuccess.invoke(newData.user)
        } ?: run {
            flowError.invoke()
            flowMessage.invoke(data.response.getDescriptionRudApi())
        }
    }

    suspend fun postReg(
        email: String,
        password: String,
        code: String,
        flowStart: () -> Unit = {},
        flowSuccess: (GettingUser) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val data = apiSignIn.postRegApi(email = email, password = password, code = code)
        if (!data.status.isSuccess()) {
            flowError.invoke()
            flowMessage.invoke(data.response.getDescriptionRudApi())
            return
        }
        data.response.data?.let { newData ->
            dataStore.UserData().update(onUpdate = {newData.user})
            dataStore.TokenData().putTokenServer(newData.token)
            flowSuccess.invoke(newData.user)
        } ?: run {
            flowError.invoke()
            flowMessage.invoke(data.response.getDescriptionRudApi())
        }
    }

    suspend fun postEmail(
        email: String,
        flowStart: () -> Unit = {},
        flowSuccess: (VerificationCode) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val data = apiSignIn.postEmailApi(email = email)
        if (!data.status.isSuccess()) {
            flowError.invoke()
            flowMessage.invoke(data.response.getDescriptionRudApi())
            return
        }
        data.response.data?.let { newData ->
            flowSuccess.invoke(newData)
        } ?: run {
            flowError.invoke()
            flowMessage.invoke(data.response.getDescriptionRudApi())
        }
    }
}
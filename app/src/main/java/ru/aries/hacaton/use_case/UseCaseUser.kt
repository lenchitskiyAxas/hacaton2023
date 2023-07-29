package ru.aries.hacaton.use_case

import android.net.Uri
import androidx.core.net.toFile
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import io.ktor.http.isSuccess
import ru.aries.hacaton.base.util.logD
import ru.aries.hacaton.data.api_client.ApiUser
import ru.aries.hacaton.data.data_store.DataStorePrefs
import ru.aries.hacaton.models.api.GettingUser
import ru.aries.hacaton.models.api.UpdatingUser

class UseCaseUser(
    private val apiUser: ApiUser,
    private val dataStore: DataStorePrefs
) {

    fun getUserLocalData() = dataStore.UserData().get()
    fun getChooseFamilyId() = dataStore.Family().getId()
    fun setChooseFamilyId(id:Int) = dataStore.Family().update { id }

    suspend fun getMe(
        flowStart: () -> Unit = {},
        flowSuccess: (GettingUser) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val data = apiUser.getMe()
        if (!data.isSuccess) {
            flowError.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            return
        }
        logD("getMe",data)
        data.data?.let { newData ->
            dataStore.UserData().update { newData }
            flowSuccess.invoke(newData)
        } ?: run {
            flowError.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
        }
    }

    suspend fun postMyAvatar(
        uri: Uri,
        flowStart: () -> Unit = {},
        flowSuccess: (GettingUser) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val compressedImageFile = Compressor.compress(dataStore.context, uri.toFile()) {
            default(width = 1024)
        }
        val data = apiUser.postMyAvatar(file = compressedImageFile)
        if (!data.status.isSuccess()) {
            flowError.invoke()
            flowMessage.invoke(data.response.getDescriptionRudApi())
            return
        }

        data.response.data?.let { newData ->
            dataStore.UserData().update { newData }
            flowSuccess.invoke(newData)
        } ?: run {
            flowError.invoke()
            flowMessage.invoke(data.response.getDescriptionRudApi())
        }
    }

    suspend fun putMe(
        user: UpdatingUser,
        flowStart: () -> Unit = {},
        flowSuccess: (GettingUser) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val data = apiUser.putMe(user = user)
        if (!data.isSuccess) {
            flowError.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            return
        }
        data.data?.let { newData ->
            dataStore.UserData().update { newData }
            flowSuccess.invoke(newData)
        } ?: run {
            flowError.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
        }
    }
}
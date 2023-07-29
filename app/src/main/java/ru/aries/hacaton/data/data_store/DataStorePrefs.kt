package ru.aries.hacaton.data.data_store

import android.content.Context
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import ru.aries.hacaton.base.util.CryptoManager
import ru.aries.hacaton.base.util.logD
import ru.aries.hacaton.models.api.GettingUser
import ru.aries.hacaton.models.local.LocalAppData
import java.io.InputStream
import java.io.OutputStream


class DataStorePrefs(val context: Context) {

    private val Context.dataStore by dataStore(
        fileName = "user-settings.json",
        serializer = LocalAppDataSerializer(context)
    )

    inner class TokenData {
        fun putTokenServer(token: String) = updateLocalData { it.copy(token = token) }
        fun getTokenServer() = getLocalData().token

        fun putTokenFirebase(token: String) = updateLocalData { it.copy(tokenFirebase = token) }
        fun getTokenFirebase() = getLocalData().tokenFirebase
    }

    inner class Family {
        fun delete() = updateLocalData { it.copy(chooserFamilyId = null) }
        fun getId(): Int? = getLocalData().chooserFamilyId
        fun update(onUpdate: (Int?) -> Int?) {
            updateLocalData { localData ->
                localData.copy(
                    chooserFamilyId = onUpdate.invoke(localData.chooserFamilyId)
                )
            }
        }
    }

    inner class UserData {
        fun delete() = updateLocalData { it.copy(user = GettingUser()) }
        fun get(): GettingUser = getLocalData().user ?: GettingUser()
        fun update(onUpdate: (GettingUser) -> GettingUser) {
            updateLocalData { localData ->
                localData.copy(
                    user = onUpdate.invoke(localData.user ?: GettingUser())
                )
            }
        }
    }

    fun getLocalData(): LocalAppData = runBlocking {
        context.dataStore.data.first()
    }

    fun updateLocalData(
        onUpdate: (LocalAppData) -> LocalAppData
    ) = runBlocking {
        context.dataStore.updateData { onUpdate.invoke(it) }
    }

    fun deleteAppSettings() {
        logD("deleteAppSettings")
        updateLocalData { LocalAppData() }
    }
}

class LocalAppDataSerializer(
    private val cryptoManager: CryptoManager
) : Serializer<LocalAppData> {

    constructor(context: Context) : this(CryptoManager(context))

    override val defaultValue: LocalAppData get() = LocalAppData()

    override suspend fun readFrom(input: InputStream): LocalAppData {
        return try {
            val text = input.reader().use { reader -> reader.readText() }
            val decode = cryptoManager.decrypt(text) ?: run { return defaultValue }
            Json.decodeFromString(
                deserializer = LocalAppData.serializer(),
                string = decode
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: LocalAppData, output: OutputStream) {
        try {
            val jsonString = Json.encodeToString(
                serializer = LocalAppData.serializer(),
                value = t
            )
            val encryptBytes = cryptoManager.encrypt(jsonString) ?: return
            output.also {
                it.write(encryptBytes.encodeToByteArray())
            }
        } catch (e: SerializationException) {
            e.printStackTrace()
        }
    }
}


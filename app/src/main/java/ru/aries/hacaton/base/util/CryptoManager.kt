package ru.aries.hacaton.base.util

import android.content.Context
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


class CryptoManager(private val context: Context) {

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
        private const val SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2With${KeyProperties.KEY_ALGORITHM_HMAC_SHA1}"
        private const val MAX_LIST_SIZE = 30

    }

    @Serializable
    data class CryptoCase(val list: Map<Int, String>? = null)

    class CryptoCasesSerializer() : Serializer<CryptoCase> {
        override val defaultValue: CryptoCase = CryptoCase()
        override suspend fun readFrom(input: InputStream): CryptoCase {
            return try {
                val text = input.reader().use { reader ->
                    reader.readText()
                }
                Json.decodeFromString(
                    deserializer = CryptoCase.serializer(),
                    string = text
                )
            } catch (e: SerializationException) {
                e.printStackTrace()
                defaultValue
            }
        }

        override suspend fun writeTo(data: CryptoCase, output: OutputStream) {
            try {

                val json = Json.encodeToString(
                    serializer = CryptoCase.serializer(),
                    value = data
                ).encodeToByteArray()

                output.also {
                    it.write(json)
                }

            } catch (e: SerializationException) {
                e.printStackTrace()
            }
        }
    }

    private val Context.dataStore by dataStore(
        fileName = "crypro_row.json",
        serializer = CryptoCasesSerializer()
    )

    private val secretKey = getDigitKey(2, 13)
    private val codeSalt = getDigitKey(3, 9)
    private val codeForeIV = getDigitKey(4)

    private fun getDigitKey(numb: Int, plusNumb: Int = 0): Int {
        val timeNuw = System.currentTimeMillis().toString()
        val numbKey = (timeNuw.getOrNull(numb)?.digitToInt() ?: numb) + plusNumb
        if (numbKey > MAX_LIST_SIZE) return numb
        return numbKey.coerceIn(0, MAX_LIST_SIZE)
    }

    private fun getMapKey() = runBlocking { context.dataStore.data.first().list ?: mapOf() }

    private fun generate(): ByteArray {
        val random = SecureRandom()
        val genByteArray = ByteArray(16)
        random.nextBytes(genByteArray)
        return genByteArray
    }

    private fun generateMapKey(): Map<Int, String> {
        val editorMap: Map<Int, String> = (0..MAX_LIST_SIZE).associateWith {
            Base64.encodeToString(generate(), Base64.DEFAULT)
        }
        runBlocking {
            context.dataStore.updateData {
                it.copy(list = editorMap)
            }
        }

        return getMapKey()
    }

    private fun getSecretKey(secretKey: String, codeStr: String): SecretKeySpec {
        val factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM)
        val spec =
            PBEKeySpec(secretKey.toCharArray(), Base64.decode(codeStr, Base64.DEFAULT), 10000, 256)
        val tmp = factory.generateSecret(spec)
        return SecretKeySpec(tmp.encoded, ALGORITHM)
    }

    private fun getIV(codeForeIV: String): IvParameterSpec {
        return IvParameterSpec(Base64.decode(codeForeIV, Base64.DEFAULT))
    }

    fun encrypt(strToEncrypt: String) = try {
        val editorMap = generateMapKey()
        val key = editorMap.getOrElse(secretKey) { "" }
        val salt = editorMap.getOrElse(codeSalt) { "" }
        val iv = editorMap.getOrElse(codeForeIV) { "" }

        val encryptCipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getSecretKey(key, salt), getIV(iv))
        }
        Base64.encodeToString(
            encryptCipher.doFinal(strToEncrypt.toByteArray(Charsets.UTF_8)),
            Base64.DEFAULT
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    fun decrypt(strToDecrypt: String) = try {
        val loadedMap = getMapKey()
        val key = loadedMap.getOrElse(secretKey) { "" }
        val salt = loadedMap.getOrElse(codeSalt) { "" }
        val iv = loadedMap.getOrElse(codeForeIV) { "" }
        val decryptCipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(
                Cipher.DECRYPT_MODE,
                getSecretKey(key, salt),
                getIV(iv)
            )
        }
        String(decryptCipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)))
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

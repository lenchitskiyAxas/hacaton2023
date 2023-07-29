package ru.aries.hacaton.data.api_client

import android.util.Log
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser.parseString
import com.google.gson.JsonSyntaxException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.serialization.jackson.jackson
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.core.Closeable
import kotlinx.serialization.json.Json
import ru.aries.hacaton.BuildConfig
import ru.aries.hacaton.base.util.logE
import ru.aries.hacaton.base.util.logI
import ru.aries.hacaton.data.data_store.DataStorePrefs
import ru.aries.hacaton.models.api.RudApi

class Client(
    private val dataStore: DataStorePrefs
) : Closeable {
    companion object {
        const val BASE_URL: String = "http://10.2.0.23:81"
    }

    val api = HttpClient {
        install(ContentNegotiation) {
            jackson {
                setSerializationInclusion(JsonInclude.Include.NON_NULL)
                setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }
            json(
                json = Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = false // false
                    isLenient = true
                    allowSpecialFloatingPointValues = true
                    allowStructuredMapKeys = true
                    prettyPrint = false
                    useArrayPolymorphism = false
                }
            )
        }

        install(Logging) {
            logger = loggerPretty
            level = LogLevel.ALL
        }
        HttpResponseValidator {
            validateResponse {
                logI("APIClient", "HttpResponseValidator: ${it.status}")
//                when (it.status) {
//                    COMEBACK_LATER_STATUS -> throw TooEarlyVote()
//                    TOO_LATE_STATUS -> throw TooLateVote()
//                    HttpStatusCode.Conflict -> return@validateResponse
//                    HttpStatusCode.Unauthorized -> throw Unauthorized()
//                }
            }
        }
        install(DefaultRequest) {
            url.takeFrom(BASE_URL)
            headers {
                contentType(ContentType.Application.Json)
                dataStore.getLocalData().token?.let { token ->
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
                dataStore.getLocalData().tokenFirebase?.let { tokenFirebase ->
                    header("x-firebase-token", tokenFirebase)
                }
            }
        }
    }

    override fun close() {
        api.close()
    }
}

data class HttpResponseRud<T>(
    val response: RudApi<T>,
    val status: HttpStatusCode,
)

suspend inline fun <reified T, RESP> HttpClient.postRequest(
    urlString: String,
    body: T
): HttpResponseRud<RESP> {
    return try {
        val response = post(
            urlString = urlString,
            block = {
                setBody(body)
            }

        )
        HttpResponseRud(
            response = response.body(),
            status = response.status
        )
    } catch (e: Exception) {
        logE(urlString, e.message)
        e.printStackTrace()
        HttpResponseRud(
            response = RudApi.getError(description = e.message, errorCode = e.hashCode()),
            status = HttpStatusCode(value = 599, description = e.message ?: "Unknown Exception")
        )
    }
}

suspend inline fun <reified T, RESP> HttpClient.postRequest(
    urlString: String,
    body: T,
    query: Map<String, Any?>? = null,
): HttpResponseRud<RESP> {
    return try {
        val queryParams = Parameters.build {
            query?.forEach { (str, any) ->
                any?.let { append(str, any.toString()) }
            }
        }
        val response = post(
            urlString = urlString,
            block = {
                url { parameters.appendAll(queryParams) }
                setBody(body)
            }

        )
        HttpResponseRud(
            response = response.body(),
            status = response.status
        )
    } catch (e: Exception) {
        logE(urlString, e.message)
        e.printStackTrace()
        HttpResponseRud(
            response = RudApi.getError(description = e.message, errorCode = e.hashCode()),
            status = HttpStatusCode(value = 599, description = e.message ?: "Unknown Exception")
        )
    }
}

suspend inline fun <reified T, RESP> HttpClient.postRequest(
    urlString: String,
    query: Map<String, Any?>? = null,
): HttpResponseRud<RESP> {
    return try {
        val queryParams = Parameters.build {
            query?.forEach { (str, any) ->
                any?.let { append(str, any.toString()) }
            }
        }
        val response = post(
            urlString = urlString,
            block = {
                url { parameters.appendAll(queryParams) }
            }

        )
        HttpResponseRud(
            response = response.body(),
            status = response.status
        )
    } catch (e: Exception) {
        logE(urlString, e.message)
        e.printStackTrace()
        HttpResponseRud(
            response = RudApi.getError(description = e.message, errorCode = e.hashCode()),
            status = HttpStatusCode(value = 599, description = e.message ?: "Unknown Exception")
        )
    }
}

suspend inline fun <reified RESP> HttpClient.postRequest(
    urlString: String,
): HttpResponseRud<RESP> {
    return try {
        val response = post(
            urlString = urlString,
        )
        HttpResponseRud(
            response = response.body(),
            status = response.status
        )
    } catch (e: Exception) {
        logE(urlString, e.message)
        e.printStackTrace()
        HttpResponseRud(
            response = RudApi.getError(description = e.message, errorCode = e.hashCode()),
            status = HttpStatusCode(value = 599, description = e.message ?: "Unknown Exception")
        )
    }
}

suspend inline fun <reified T, RESP> HttpClient.getRequest(
    urlString: String,
    body: T
): HttpResponseRud<RESP> {
    val response = get(urlString) { setBody(body) }
    return HttpResponseRud(
        response = response.body(),
        status = response.status
    )
}

suspend inline fun <reified T> HttpClient.getRequest(
    urlString: String
) = get(urlString) { setBody(body) }

suspend inline fun <reified T> HttpClient.putRequest(
    urlString: String, body: T
) = put(urlString) { setBody(body) }

suspend inline fun <reified T> HttpClient.deleteRequest(
    urlString: String, body: T
) = delete(urlString) { setBody(body) }

private val loggerPretty = object : Logger {
    private val BODY_START = "BODY START"
    private val BODY_END = "BODY END"
    private val LOG_NAME = "HTTP Client"
    override fun log(message: String) {
        if (!BuildConfig.DEBUG) return
        val startBody = message.indexOf(BODY_START)
        val endBody = message.indexOf(BODY_END)
        if (startBody != -1 && endBody != -1) {
            try {
                val header = message.substring(0, startBody)
                val jsonBody = message.substring(startBody + BODY_START.length, endBody)

                val prettyPrintJson = GsonBuilder().setPrettyPrinting()
                    .create().toJson(parseString(jsonBody))
                Log.i(LOG_NAME, "$header\n$prettyPrintJson")
            } catch (m: JsonSyntaxException) {
                Log.i(LOG_NAME, message)
            }
        } else {
            Log.e(LOG_NAME, message)
        }
    }
}
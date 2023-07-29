package ru.aries.hacaton.models.api

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.util.logE


data class RudApi<T>(
    val data: T? = null,
    val description: String? = null,
    val errors: List<ErrorApi>? = null,
    val message: String? = null,
    var isSuccess: Boolean = true,
    val meta: MetaApi? = null,
) {

    companion object {
        fun <T> getError(
            description: String? = null,
            errors:List<ErrorApi>? = null,
            errorCode: Int? = null
        ) : RudApi<T> {
            val str  = TextApp.textSomethingWentWrong
            return RudApi(
                data = null,
                description = description ?: str,
                errors = if (errors.isNullOrEmpty()) listOf(ErrorApi(
                    message = description ?: str,
                    code = errorCode ?: 599
                )) else errors,
                message = null,
                isSuccess = false,
                meta = null
            )
        }
    }

    fun setSuccessFalse() = this.copy(isSuccess = false)

    fun getDescriptionRudApi() = if (this.description.isNullOrBlank())
    TextApp.textSomethingWentWrong else this.description

    fun formattedErrors(): String {
        logE("formattedErrors",errors?.toList().toString())
        var errors = ""
        this.errors?.forEach { error ->
            errors += "${error.message.toString()}\n"
        }
        return errors.trim()
    }

    fun getCodeError(): Int {
        if (this.data != null) return 200
        val firstError = this.errors?.firstOrNull()
        return firstError?.code ?: 508
    }
}

data class ErrorApi(
    val additional: String? = null,
    val code: Int? = null,
    val message: String? = null,
    val path: String? = null
)

data class MetaApi(
    val paginator: PaginationApi? = null
)

data class PaginationApi(
    val has_next: Boolean? = null,
    val has_prev: Boolean? = null,
    val page: Int? = null,
    val total: Int? = null
)

//fun <T> Response<T>.errorBodyToMap(): RudApi<Any> {
//    val str = "UNKNOWN ERROR"
//    val responseError = this.errorBody()?.string() ?: str
//    return responseError.jsonToObject() ?: RudApi(
//        errors = listOf(ErrorApi(code = 0, message = str)),
//        message = str)
//}

suspend fun ResponseBody.stringSuspending() = withContext(Dispatchers.IO) { string() }





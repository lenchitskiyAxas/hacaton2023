package ru.aries.hacaton.base.extension

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

fun String.onlyDigit() = Regex("[^0-9]").replace(this, "")

private val LocaleRuTime = Locale("ru", "RU")

fun Long.getYer(): Int =
    SimpleDateFormat("yyyy", LocaleRuTime).format(this).toIntOrNull() ?: 0

fun Long.getMonth(): Int =
    SimpleDateFormat("MM", LocaleRuTime).format(this).toIntOrNull() ?: 0

fun Long.getDay(): Int = SimpleDateFormat("dd", LocaleRuTime).format(this).toIntOrNull() ?: 0
fun Long.toDateString(): String = SimpleDateFormat("dd.MM.yyyy", LocaleRuTime).format(this * 1000L)
fun Long.toDateMillisToUnixString(): String =
    SimpleDateFormat("dd.MM.yyyy", LocaleRuTime).format(this / 1000)

fun Long.toDateMillisToUnixStringFullMonth(): String =
    SimpleDateFormat("dd MMMM yyyy г.", LocaleRuTime).format(this / 1000)

fun Long.unixToTimeHourAndMinutes(): String =
    SimpleDateFormat("HH:mm", LocaleRuTime).format(this * 1000L)

fun Long.formatTimeElapsed(): String {
    val currentUnixTime = Instant.now().epochSecond
    val elapsedTime = currentUnixTime - this

    return when {
        elapsedTime < 60     -> "только что"
        elapsedTime < 3600   -> {
            val minutes = elapsedTime / 60
            "$minutes минут назад"
        }

        elapsedTime < 86400  -> {
            val hours = elapsedTime / 3600
            "сегодня в ${this.unixToTimeHourAndMinutes()}"
        }

        elapsedTime < 172800 -> "вчера в ${this.unixToTimeHourAndMinutes()}"
        else                 -> {
            val date = Instant.ofEpochSecond(this).atZone(ZoneId.systemDefault()).toLocalDate()
            date.toString()
        }
    }
}

fun isTextMatchingRegex(text: String): Boolean {
    val regex = Regex("^.*(?=.{8,})(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!#\$%&? \"]).*\$")
    return regex.matches(text)
}

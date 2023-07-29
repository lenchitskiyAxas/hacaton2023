package ru.aries.hacaton.base.util

import android.util.Log
import ru.aries.hacaton.BuildConfig

private const val mainStr = "AXAS ${BuildConfig.APPLICATION_ID}"
private val SHOW_LOG = BuildConfig.DEBUG

private val separator: CharSequence = ", "
private val prefix: CharSequence = "{{{ "
private val postfix: CharSequence = " }}}"

fun logI(vararg any: Any?) {
    if (!SHOW_LOG) return
    Log.i(mainStr, any.joinToString(separator, prefix, postfix))
}

fun logE(vararg any: Any?) {
    if (!SHOW_LOG) return
    Log.e(mainStr, any.joinToString(separator, prefix, postfix))
}

fun logD(vararg any: Any?) {
    if (!SHOW_LOG) return
    Log.d(mainStr, any.joinToString(separator, prefix, postfix))
}

fun logW(vararg any: Any?) {
    if (!SHOW_LOG) return
    Log.w(mainStr, any.joinToString(separator, prefix, postfix))
}

fun logV(vararg any: Any?) {
    if (!SHOW_LOG) return
    Log.v(mainStr, any.joinToString(separator, prefix, postfix))
}


fun logDRRealise(vararg any: Any?) {
    Log.d(mainStr, any.joinToString(separator, prefix, postfix))
}

fun logIRRealise(vararg any: Any?) {
    Log.i(mainStr, any.joinToString(separator, prefix, postfix))
}
package com.korop.yaroslavhorach.common.utill

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun <T> Collection<T>.firstOr(default: T) = firstOrNull() ?: default

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
fun Context.getCurrentUserLanguage(): String {
    return resources.configuration.locales.get(0).language
        ?: "en"
}
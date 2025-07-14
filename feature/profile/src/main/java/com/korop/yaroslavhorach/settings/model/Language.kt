package com.korop.yaroslavhorach.settings.model

import android.content.Context
import com.korop.yaroslavhorach.common.utill.getCurrentUserLanguage
import java.util.Locale

@JvmInline
value class Language(val value: String) {

    val nativeDisplayName: String
        get() {
            val name = Locale(value).getDisplayName(Locale(value))

            return name.replaceFirstChar { it.uppercase() }
        }

    fun getLocalisedDisplayName(context: Context): String {
        val currentUserLocale = Locale(context.getCurrentUserLanguage())
        val name = Locale(value).getDisplayName(currentUserLocale)

        return name.replaceFirstChar { it.uppercase() }
    }
}
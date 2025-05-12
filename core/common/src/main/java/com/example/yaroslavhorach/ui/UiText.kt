package com.example.yaroslavhorach.ui

import androidx.annotation.ArrayRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource

sealed class UiText {

    class FromString(val value: String?) : UiText()

    class FromResource(@StringRes val resId: Int, vararg val args: Any) : UiText()

    class FromResourcePlural(@PluralsRes val resId: Int, val count: Int, vararg val args: Any) : UiText()

    class RandomFromResourceArray(@ArrayRes val resId: Int) : UiText()

    data object Empty : UiText()

    @Composable
    fun asString(): String {
        return when(this) {
            is FromString -> this.value ?: ""
            is FromResource -> stringResource(id = resId, *args)
            is FromResourcePlural -> pluralStringResource(id = resId, count, *args)
            is RandomFromResourceArray -> stringArrayResource(resId).random()
            is Empty -> ""
        }
    }
}
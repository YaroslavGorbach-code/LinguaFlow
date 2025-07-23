package com.korop.yaroslavhorach.domain.holders

import kotlinx.coroutines.flow.MutableStateFlow

object OpenGameDetailsHolder {
    val gameIdToOpen = MutableStateFlow<Long?>(null)
}
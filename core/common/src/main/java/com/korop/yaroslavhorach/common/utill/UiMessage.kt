package com.korop.yaroslavhorach.common.utill

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID

data class UiMessage<T>(
    val message: T,
    val id: Long = UUID.randomUUID().mostSignificantBits,
)

class UiMessageManager<T>() {
    private val mutex = Mutex()

    private val _messages = MutableStateFlow(emptyList<UiMessage<T>>())

    /**
     * A flow emitting the current message to display.
     */
    val message: Flow<UiMessage<T>?> = _messages.map { it.firstOrNull() }.distinctUntilChanged()

    suspend fun emitMessage(message: UiMessage<T>) {
        mutex.withLock {
            _messages.value += message
        }
    }

    suspend fun clearMessage(id: Long) {
        mutex.withLock {
            _messages.value = _messages.value.filterNot { it.id == id }
        }
    }
}

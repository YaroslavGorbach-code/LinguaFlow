package com.example.yaroslavhorach.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaroslavhorach.common.utill.UiMessage
import com.example.yaroslavhorach.common.utill.UiMessageManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<STATE, ACTION, MESSAGE> : ViewModel() {

    protected abstract val pendingActions: MutableSharedFlow<ACTION>

    abstract val state: StateFlow<STATE>

    protected val uiMessageManager: UiMessageManager<MESSAGE> = UiMessageManager()

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            delay(200)
            uiMessageManager.clearMessage(id)
        }
    }

    fun submitAction(action: ACTION) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }

    fun submitMessage(message: MESSAGE) {
        viewModelScope.launch {
            uiMessageManager.emitMessage(UiMessage(message))
        }
    }
}
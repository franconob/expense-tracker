package com.francoherrero.expensetracker.presentation.controller

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

sealed class UIEvent {
    data class ShowSnackbar(val message: String) : UIEvent()
}

class UIEventController() {
    private val _uiEvents =  MutableSharedFlow<UIEvent>(
        extraBufferCapacity = 64
    )
    val events: SharedFlow<UIEvent> = _uiEvents

    fun emit(event: UIEvent) {
            _uiEvents.tryEmit(event)
    }

    fun showSnackbar(message: String) {
        emit(UIEvent.ShowSnackbar(message))
    }
}
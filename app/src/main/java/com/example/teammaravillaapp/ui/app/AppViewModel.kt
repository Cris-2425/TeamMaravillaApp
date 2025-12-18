package com.example.teammaravillaapp.ui.app

import androidx.lifecycle.ViewModel
import com.example.teammaravillaapp.ui.events.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class AppViewModel : ViewModel() {

    private val _events = MutableSharedFlow<UiEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val events: SharedFlow<UiEvent> = _events

    fun showSnackbar(message: String) {
        _events.tryEmit(UiEvent.ShowSnackbar(message))
    }
}
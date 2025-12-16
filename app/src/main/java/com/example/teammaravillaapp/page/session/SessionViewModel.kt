package com.example.teammaravillaapp.page.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.data.session.SessionStore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn

class SessionViewModel(
    private val sessionStore: SessionStore
) : ViewModel() {

    val isLoggedIn = sessionStore.isLoggedIn
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val username = sessionStore.username
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _events = MutableSharedFlow<SessionEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val events: SharedFlow<SessionEvent> = _events.asSharedFlow()

    fun notifyLoggedIn() {
        _events.tryEmit(SessionEvent.LoggedIn)
    }

    fun notifyLoggedOut() {
        _events.tryEmit(SessionEvent.LoggedOut)
    }
}
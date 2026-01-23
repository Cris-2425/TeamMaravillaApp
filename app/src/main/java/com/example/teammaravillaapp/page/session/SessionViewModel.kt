package com.example.teammaravillaapp.page.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.data.session.SessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionStore: SessionStore,
    sessionManager: SessionManager
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
    val events: SharedFlow<SessionEvent> = sessionManager.events

    fun notifyLoggedIn() {
        _events.tryEmit(SessionEvent.LoggedIn)
    }

    fun notifyLoggedOut() {
        _events.tryEmit(SessionEvent.LoggedOut)
    }
}
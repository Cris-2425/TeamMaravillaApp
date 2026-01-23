package com.example.teammaravillaapp.page.session

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {

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
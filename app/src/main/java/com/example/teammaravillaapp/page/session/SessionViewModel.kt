package com.example.teammaravillaapp.page.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.data.session.SessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionStore: SessionStore
) : ViewModel() {

    val sessionState: StateFlow<SessionState> =
        combine(
            sessionStore.isLoggedIn,
            sessionStore.rememberMe,
            sessionStore.username
        ) { loggedIn, rememberMe, username ->
            if (loggedIn && rememberMe) SessionState.LoggedIn(username)
            else SessionState.LoggedOut
        }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SessionState.Loading
            )
}
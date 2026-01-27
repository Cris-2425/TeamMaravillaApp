package com.example.teammaravillaapp.page.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.data.session.SessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionStore: SessionStore
) : ViewModel() {

    val sessionState = combine(
        sessionStore.isLoggedIn,
        sessionStore.username
    ) { loggedIn, username ->
        if (loggedIn) SessionState.LoggedIn(username)
        else SessionState.LoggedOut
    }
        .distinctUntilChanged()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            SessionState.Loading
        )
}
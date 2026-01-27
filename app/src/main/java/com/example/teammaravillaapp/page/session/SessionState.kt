package com.example.teammaravillaapp.page.session

sealed interface SessionState {
    data object Loading : SessionState
    data object LoggedOut : SessionState
    data class LoggedIn(val username: String?) : SessionState
}
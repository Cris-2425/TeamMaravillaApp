package com.example.teammaravillaapp.page.session

sealed interface SessionEvent {
    data object LoggedIn : SessionEvent
    data object LoggedOut : SessionEvent
}
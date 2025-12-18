package com.example.teammaravillaapp.ui.events

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
}
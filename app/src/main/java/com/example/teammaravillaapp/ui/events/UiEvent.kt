package com.example.teammaravillaapp.ui.events

import androidx.annotation.StringRes

sealed interface UiEvent {

    data class ShowSnackbar(
        @StringRes val messageResId: Int,
        val formatArgs: Array<Any> = emptyArray()
    ) : UiEvent
}

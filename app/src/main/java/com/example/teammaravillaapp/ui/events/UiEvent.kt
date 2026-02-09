package com.example.teammaravillaapp.ui.events

import androidx.annotation.StringRes

sealed interface UiEvent {

    data class ShowSnackbar(
        @StringRes val messageResId: Int,
        val formatArgs: Array<Any> = emptyArray()
    ) : UiEvent

    data class ShowSnackbarAction(
        @StringRes val messageResId: Int,
        @StringRes val actionResId: Int,
        val formatArgs: Array<Any> = emptyArray(),
        val withDismissAction: Boolean = true,
        val onAction: () -> Unit,
        val onDismiss: () -> Unit = {}
    ) : UiEvent
}

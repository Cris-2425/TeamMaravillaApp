package com.example.teammaravillaapp.ui.events

import androidx.annotation.StringRes

sealed interface UiResult<out T> {
    data object Loading : UiResult<Nothing>
    data class Success<T>(val data: T) : UiResult<T>

    data class Error(
        @StringRes val messageResId: Int,
        val cause: Throwable? = null
    ) : UiResult<Nothing>
}
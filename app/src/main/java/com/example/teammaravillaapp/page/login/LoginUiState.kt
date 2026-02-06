package com.example.teammaravillaapp.page.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val rememberMe: Boolean = true,
    val isLoading: Boolean = false
) {
    val isLoginButtonEnabled: Boolean
        get() = username.isNotBlank() && password.length >= 4 && !isLoading
}

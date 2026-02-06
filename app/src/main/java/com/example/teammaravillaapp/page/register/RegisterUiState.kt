package com.example.teammaravillaapp.page.register

data class RegisterUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = true,
    val isLoading: Boolean = false
) {
    val isRegisterEnabled: Boolean
        get() = username.isNotBlank() && email.isNotBlank() && password.length >= 4 && !isLoading
}
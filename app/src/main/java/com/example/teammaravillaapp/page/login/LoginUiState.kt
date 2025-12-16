package com.example.teammaravillaapp.page.login

// Estado de la interfaz de Login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
) {
    val isLoginButtonEnabled: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isLoading
}

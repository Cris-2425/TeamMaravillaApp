package com.example.teammaravillaapp.page.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teammaravillaapp.data.auth.AuthRepository
import com.example.teammaravillaapp.page.session.SessionViewModel

class LoginViewModelFactory(
    private val authRepository: AuthRepository,
    private val sessionViewModel: SessionViewModel
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepository, sessionViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

package com.example.teammaravillaapp.page.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.data.auth.AuthRepository
import com.example.teammaravillaapp.page.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            sessionManager.notifyLoggedOut()
        }
    }
}
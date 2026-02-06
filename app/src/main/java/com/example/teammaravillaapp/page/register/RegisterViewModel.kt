package com.example.teammaravillaapp.page.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.repository.users.UsersRepository
import com.example.teammaravillaapp.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun onUsernameChange(v: String) = _uiState.update { it.copy(username = v) }
    fun onEmailChange(v: String) = _uiState.update { it.copy(email = v) }
    fun onPasswordChange(v: String) = _uiState.update { it.copy(password = v) }
    fun onRememberMeChange(v: Boolean) = _uiState.update { it.copy(rememberMe = v) }

    fun onRegisterClick(onRegistered: () -> Unit) {
        val st = _uiState.value
        if (st.username.isBlank() || st.email.isBlank() || st.password.isBlank()) {
            _events.tryEmit(UiEvent.ShowSnackbar(R.string.register_error_required_fields))
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = runCatching {
                usersRepository.register(st.username, st.email, st.password, st.rememberMe)
            }

            _uiState.update { it.copy(isLoading = false) }

            result.onSuccess { ok ->
                if (ok) onRegistered()
                else _events.tryEmit(UiEvent.ShowSnackbar(R.string.register_error_failed))
            }.onFailure {
                it.printStackTrace()
                _events.tryEmit(UiEvent.ShowSnackbar(R.string.login_error_network))
            }
        }
    }
}
package com.example.teammaravillaapp.page.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.repository.users.UsersRepository
import com.example.teammaravillaapp.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel de Login.
 *
 * Responsabilidades:
 * - Mantener el estado del formulario ([LoginUiState]).
 * - Aplicar validaciones mínimas antes de intentar autenticar.
 * - Llamar a [UsersRepository.login] para autenticar y (opcionalmente) persistir sesión.
 * - Emitir [UiEvent] ante errores (campos vacíos, credenciales inválidas, fallo técnico).
 *
 * @param usersRepository Repositorio encargado de la autenticación y persistencia de sesión.
 * Restricciones:
 * - No nulo.
 *
 * @see UsersRepository.login Operación de login.
 * @see LoginUiState Estado expuesto a UI.
 *
 * Ejemplo:
 * {@code
 * vm.onEmailChange("cris")
 * vm.onPasswordChange("1234")
 * vm.onLoginClick { navigateToHome() }
 * }
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    /**
     * Actualiza el campo de usuario/email.
     *
     * @param v Nuevo valor introducido por el usuario.
     * Restricciones:
     * - Puede ser vacío.
     */
    fun onEmailChange(v: String) = _uiState.update { it.copy(username = v) }

    /**
     * Actualiza el campo de contraseña.
     *
     * @param v Nuevo valor de contraseña.
     * Restricciones:
     * - Puede ser vacío.
     */
    fun onPasswordChange(v: String) = _uiState.update { it.copy(password = v) }

    /**
     * Actualiza la preferencia “recordarme”.
     *
     * @param v true si el usuario quiere persistir la sesión; false en caso contrario.
     */
    fun onRememberMeChange(v: Boolean) = _uiState.update { it.copy(rememberMe = v) }

    /**
     * Ejecuta el flujo de login.
     *
     * Validaciones:
     * - Si usuario o contraseña están vacíos, emite snackbar y no realiza llamada al repositorio.
     *
     * Comportamiento:
     * - Marca [LoginUiState.isLoading] durante la operación.
     * - Si el repositorio devuelve `true`, invoca [onLoggedIn].
     * - Si devuelve `false`, emite error de credenciales.
     * - Si hay excepción (fallo técnico/red), emite error de red.
     *
     * @param onLoggedIn Callback a ejecutar cuando el login finaliza correctamente.
     * Restricciones:
     * - No nulo.
     *
     * @throws Exception No se propaga al exterior; se captura y se traduce a [UiEvent].
     *
     * @see UsersRepository.login
     *
     * Ejemplo:
     * {@code
     * vm.onLoginClick { navController.navigate("home") }
     * }
     */
    fun onLoginClick(onLoggedIn: () -> Unit) {
        val st = _uiState.value
        if (st.username.isBlank() || st.password.isBlank()) {
            _events.tryEmit(UiEvent.ShowSnackbar(R.string.login_error_required_fields))
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = runCatching {
                usersRepository.login(st.username, st.password, st.rememberMe)
            }

            _uiState.update { it.copy(isLoading = false) }

            result.onSuccess { ok ->
                if (ok) onLoggedIn()
                else _events.tryEmit(UiEvent.ShowSnackbar(R.string.login_error_invalid_credentials))
            }.onFailure {
                it.printStackTrace()
                _events.tryEmit(UiEvent.ShowSnackbar(R.string.login_error_network))
            }
        }
    }
}
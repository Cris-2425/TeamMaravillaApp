package com.example.teammaravillaapp.page.register

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
 * ViewModel de Registro.
 *
 * Responsabilidades:
 * - Mantener el estado del formulario ([RegisterUiState]).
 * - Aplicar validaciones mínimas antes de intentar registrar.
 * - Llamar a [UsersRepository.register] para crear la cuenta y (opcionalmente) persistir sesión.
 * - Emitir [UiEvent] ante errores (campos vacíos, registro fallido, fallo técnico/red).
 *
 * @param usersRepository Repositorio encargado del registro y persistencia de sesión.
 * Restricciones:
 * - No nulo.
 *
 * @see UsersRepository.register Operación de registro.
 * @see RegisterUiState Estado expuesto a UI.
 *
 * Ejemplo:
 * {@code
 * vm.onUsernameChange("cris")
 * vm.onEmailChange("cris@correo.com")
 * vm.onPasswordChange("1234")
 * vm.onRegisterClick { navigateToLoginOrHome() }
 * }
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    /**
     * Actualiza el nombre de usuario.
     *
     * @param v Nuevo valor.
     * Restricciones:
     * - Puede ser vacío.
     */
    fun onUsernameChange(v: String) = _uiState.update { it.copy(username = v) }

    /**
     * Actualiza el email.
     *
     * @param v Nuevo valor.
     * Restricciones:
     * - Puede ser vacío.
     */
    fun onEmailChange(v: String) = _uiState.update { it.copy(email = v) }

    /**
     * Actualiza la contraseña.
     *
     * @param v Nuevo valor.
     * Restricciones:
     * - Puede ser vacío.
     */
    fun onPasswordChange(v: String) = _uiState.update { it.copy(password = v) }

    /**
     * Actualiza la preferencia “recordarme”.
     *
     * @param v true si se quiere persistir sesión tras registrarse.
     */
    fun onRememberMeChange(v: Boolean) = _uiState.update { it.copy(rememberMe = v) }

    /**
     * Ejecuta el flujo de registro.
     *
     * Validaciones:
     * - Si username/email/password están vacíos, emite snackbar y no realiza llamada al repositorio.
     *
     * Comportamiento:
     * - Marca [RegisterUiState.isLoading] durante la operación.
     * - Si el repositorio devuelve `true`, invoca [onRegistered].
     * - Si devuelve `false`, emite error de registro fallido.
     * - Si hay excepción (fallo técnico/red), emite error de red.
     *
     * @param onRegistered Callback a ejecutar cuando el registro finaliza correctamente.
     * Restricciones:
     * - No nulo.
     *
     * @throws Exception No se propaga al exterior; se captura y se traduce a [UiEvent].
     *
     * @see UsersRepository.register
     *
     * Ejemplo:
     * {@code
     * vm.onRegisterClick { navController.navigate("home") }
     * }
     */
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
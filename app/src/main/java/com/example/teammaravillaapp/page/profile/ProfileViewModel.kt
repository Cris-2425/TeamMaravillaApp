package com.example.teammaravillaapp.page.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.local.prefs.user.ProfilePhotoPrefs
import com.example.teammaravillaapp.data.repository.users.UsersRepository
import com.example.teammaravillaapp.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel de Perfil.
 *
 * Responsabilidades:
 * - Exponer la URI de foto de perfil persistida (DataStore) como [StateFlow].
 * - Guardar/eliminar la foto de perfil (persistencia mediante [ProfilePhotoPrefs]).
 * - Emitir eventos one-shot [UiEvent] cuando una operación falla.
 * - Cerrar sesión mediante [UsersRepository].
 *
 * @param photoPrefs Preferencias persistentes asociadas a la foto de perfil.
 * Restricciones: no nulo.
 * @param authRepository Repositorio de autenticación/sesión.
 * Restricciones: no nulo.
 *
 * @see ProfilePhotoPrefs Persistencia de la URI.
 * @see UsersRepository.logout Cierre de sesión.
 *
 * Ejemplo:
 * {@code
 * vm.savePhoto("content://...")
 * vm.clearPhoto()
 * vm.logout()
 * }
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val photoPrefs: ProfilePhotoPrefs,
    private val authRepository: UsersRepository
) : ViewModel() {

    /**
     * URI de la foto de perfil persistida.
     *
     * - `null` si el usuario no tiene foto.
     * - Se mantiene en memoria como [StateFlow] para recomposición en Compose.
     */
    val photoUri: StateFlow<String?> =
        photoPrefs.observeUri()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)

    /**
     * Flujo de eventos one-shot para UI (snackbars).
     */
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    /**
     * Persiste la URI de la foto de perfil.
     *
     * @param uriString URI serializada a String (por ejemplo, `content://...`).
     * Restricciones:
     * - No nulo.
     * - Debe ser una URI válida para que la UI pueda cargarla.
     *
     * @throws Exception No se propaga: se captura y se traduce a [UiEvent.ShowSnackbar].
     *
     * Ejemplo:
     * {@code
     * vm.savePhoto(resultUri.toString())
     * }
     */
    fun savePhoto(uriString: String) {
        viewModelScope.launch {
            runCatching { photoPrefs.saveUri(uriString) }
                .onFailure { _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed)) }
        }
    }

    /**
     * Elimina la foto de perfil persistida.
     *
     * @throws Exception No se propaga: se captura y se traduce a [UiEvent.ShowSnackbar].
     *
     * Ejemplo:
     * {@code
     * vm.clearPhoto()
     * }
     */
    fun clearPhoto() {
        viewModelScope.launch {
            runCatching { photoPrefs.clear() }
                .onFailure { _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed)) }
        }
    }

    /**
     * Maneja un error de recorte (UCrop) emitiendo un snackbar genérico.
     *
     * @see com.yalantis.ucrop.UCrop
     */
    fun onCropError() {
        _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed))
    }

    /**
     * Cierra la sesión del usuario actual.
     *
     * Ejecuta la operación de logout contra el repositorio de autenticación.
     * Si ocurre un error técnico (por ejemplo, fallo en DataStore o red),
     * se emite un evento de UI para mostrar un mensaje al usuario.
     *
     * No realiza navegación automática; la pantalla que observe el estado
     * de sesión deberá reaccionar si corresponde.
     *
     * Ejemplo de uso:
     * {@code
     * OutlinedButton(onClick = { vm.logout() }) {
     *     Text(stringResource(R.string.profile_logout))
     * }
     * }
     */
    fun logout() {
        viewModelScope.launch {
            runCatching { authRepository.logout() }
                .onSuccess {
                    _events.tryEmit(
                        UiEvent.ShowSnackbar(R.string.profile_logout_success)
                    )
                }
                .onFailure {
                    _events.tryEmit(
                        UiEvent.ShowSnackbar(R.string.snackbar_action_failed)
                    )
                }
        }
    }
}
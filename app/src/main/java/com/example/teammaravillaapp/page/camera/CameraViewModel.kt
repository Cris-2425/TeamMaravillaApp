package com.example.teammaravillaapp.page.camera

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.local.prefs.user.ReceiptsPrefs
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
 * ViewModel de la pantalla de cámara.
 *
 * Centraliza la lógica de:
 * - permisos (resultado del launcher)
 * - cambios de lente/flash/torch/zoom (estado UI)
 * - ciclo de captura (inicio/éxito/error)
 * - persistencia del "ticket" (URI) asociado a una lista mediante [ReceiptsPrefs]
 *
 * La UI debe:
 * - Renderizar según [uiState]
 * - Enviar callbacks a este VM
 * - Escuchar [events] para snackbars u otros eventos one-shot
 *
 * @see CameraUiState
 * @see ReceiptsPrefs
 *
 * Ejemplo de uso:
 * {@code
 * val uiState by vm.uiState.collectAsState()
 * LaunchedEffect(Unit) { vm.events.collect { onUiEvent(it) } }
 * }
 */
@HiltViewModel
class CameraViewModel @Inject constructor(
    private val receiptsPrefs: ReceiptsPrefs
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    /** Flujo observable del estado de UI (source of truth). */
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    /** Eventos one-shot para UI (snackbars, etc.). */
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    /**
     * Procesa el resultado del permiso de cámara.
     *
     * @param granted True si el usuario concedió permiso. False si lo denegó.
     *
     * @throws RuntimeException (técnica) No lanza explícitamente, pero si el flujo de eventos falla por presión extrema.
     * @throws IllegalStateException (negocio) No aplica: el valor puede ser true/false sin restricciones adicionales.
     */
    fun onPermissionResult(granted: Boolean) {
        _uiState.update { it.copy(hasPermission = granted) }
        if (!granted) _events.tryEmit(UiEvent.ShowSnackbar(R.string.camera_permission_denied))
    }

    /**
     * Alterna entre lente trasera y frontal.
     *
     * Además resetea el estado de torch y zoom por seguridad/consistencia:
     * - torch se apaga para evitar comportamientos inesperados tras el cambio de lente
     * - zoom vuelve a 1f (normal)
     */
    fun toggleLens() {
        _uiState.update {
            val next =
                if (it.lensFacing == CameraUiState.LENS_BACK) CameraUiState.LENS_FRONT
                else CameraUiState.LENS_BACK

            it.copy(
                lensFacing = next,
                torchEnabled = false,
                zoomRatio = 1f
            )
        }
    }

    /**
     * Activa o desactiva el flash de captura (foto).
     *
     * Nota: esta bandera solo cambia el estado; el controlador de cámara debe aplicar el flashMode.
     */
    fun toggleFlash() {
        _uiState.update { it.copy(flashEnabled = !it.flashEnabled) }
    }

    /**
     * Marca la captura como iniciada (se usa para bloquear UI y evitar dobles capturas).
     */
    fun onCaptureStarted() {
        _uiState.update { it.copy(isCapturing = true) }
    }

    /**
     * Registra el resultado exitoso de una captura.
     *
     * @param uri URI no nula donde se guardó la imagen capturada.
     *
     * @throws IllegalArgumentException (negocio) Si se llama con uri inválida. (Actualmente no se lanza; se asume no nula.)
     */
    fun onCaptured(uri: Uri) {
        _uiState.update { it.copy(capturedUri = uri, isCapturing = false) }
    }

    /**
     * Maneja un fallo de captura.
     *
     * Emite un snackbar genérico y desbloquea la UI.
     */
    fun onCaptureFailed() {
        _uiState.update { it.copy(isCapturing = false) }
        _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed))
    }

    /**
     * Descarta la foto capturada para volver al modo preview.
     */
    fun retake() {
        _uiState.update { it.copy(capturedUri = null) }
    }

    /**
     * Guarda el ticket (URI capturada) asociado a una lista.
     *
     * Reglas de negocio:
     * - listId debe ser no nulo y no blanco
     * - debe existir una [capturedUri] previa
     *
     * @param listId Identificador de la lista a la que asociar el ticket. No debe ser null/blank.
     *
     * @throws IllegalStateException (negocio) Si no hay listId o no hay imagen capturada (se notifica con snackbar).
     * @throws Exception (técnica) Cualquier excepción interna al persistir en [ReceiptsPrefs] (se traduce a snackbar genérico).
     *
     * Ejemplo de uso:
     * {@code
     * Button(onClick = { vm.saveReceipt(listId) }) { Text("Guardar") }
     * }
     */
    fun saveReceipt(listId: String?) {
        val id = listId
        val uri = _uiState.value.capturedUri

        if (id.isNullOrBlank()) {
            _events.tryEmit(UiEvent.ShowSnackbar(R.string.camera_error_no_list))
            return
        }
        if (uri == null) {
            _events.tryEmit(UiEvent.ShowSnackbar(R.string.camera_error_no_image))
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            runCatching { receiptsPrefs.saveReceiptUri(id, uri) }
                .onSuccess { _events.tryEmit(UiEvent.ShowSnackbar(R.string.camera_saved_ok)) }
                .onFailure { _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed)) }

            _uiState.update { it.copy(isSaving = false) }
        }
    }

    /**
     * Alterna el torch (linterna continua).
     *
     * Nota: este estado debe ser aplicado por el controlador CameraX mediante `enableTorch`.
     */
    fun toggleTorch() {
        _uiState.update { it.copy(torchEnabled = !it.torchEnabled) }
    }

    /**
     * Establece el zoom ratio actual.
     *
     * @param value Zoom ratio deseado. Se recomienda clamp en UI: 1f..maxZoomRatio.
     */
    fun setZoomRatio(value: Float) {
        _uiState.update { it.copy(zoomRatio = value) }
    }

    /**
     * Informa al ViewModel del zoom máximo real reportado por la cámara.
     *
     * @param max Valor máximo. Si es < 1f se normaliza a 1f.
     */
    fun onMaxZoomKnown(max: Float) {
        _uiState.update { it.copy(maxZoomRatio = max.coerceAtLeast(1f)) }
    }
}
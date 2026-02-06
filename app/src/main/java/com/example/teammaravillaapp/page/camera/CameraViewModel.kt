package com.example.teammaravillaapp.page.camera

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.local.prefs.user.ReceiptsPrefs
import com.example.teammaravillaapp.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val receiptsPrefs: ReceiptsPrefs
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    fun onPermissionResult(granted: Boolean) {
        _uiState.update { it.copy(hasPermission = granted) }
        if (!granted) _events.tryEmit(UiEvent.ShowSnackbar(R.string.camera_permission_denied))
    }

    fun toggleLens() {
        _uiState.update {
            val next = if (it.lensFacing == CameraUiState.LENS_BACK) CameraUiState.LENS_FRONT else CameraUiState.LENS_BACK
            it.copy(
                lensFacing = next,
                torchEnabled = false,
                zoomRatio = 1f
            )
        }
    }

    fun toggleFlash() {
        _uiState.update { it.copy(flashEnabled = !it.flashEnabled) }
    }

    fun onCaptured(uri: Uri) {
        _uiState.update { it.copy(capturedUri = uri, isCapturing = false) }
    }

    fun onCaptureStarted() {
        _uiState.update { it.copy(isCapturing = true) }
    }

    fun onCaptureFailed() {
        _uiState.update { it.copy(isCapturing = false) }
        _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed))
    }

    fun retake() {
        _uiState.update { it.copy(capturedUri = null) }
    }

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

    fun toggleTorch() {
        _uiState.update { it.copy(torchEnabled = !it.torchEnabled) }
    }

    fun setZoomRatio(value: Float) {
        _uiState.update { it.copy(zoomRatio = value) }
    }

    fun onMaxZoomKnown(max: Float) {
        _uiState.update { it.copy(maxZoomRatio = max.coerceAtLeast(1f)) }
    }
}
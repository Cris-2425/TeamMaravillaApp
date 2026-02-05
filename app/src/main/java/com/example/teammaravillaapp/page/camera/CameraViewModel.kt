package com.example.teammaravillaapp.page.camera

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.local.prefs.ReceiptsPrefs
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

    fun onPicked(uri: Uri?) {
        _uiState.update { it.copy(pickedUri = uri) }
    }

    fun onClear() {
        _uiState.value = CameraUiState()
    }

    fun onSave(listId: String?) {
        val id = listId
        val uri = _uiState.value.pickedUri

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
}
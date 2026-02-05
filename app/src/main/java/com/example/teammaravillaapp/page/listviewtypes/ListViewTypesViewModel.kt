package com.example.teammaravillaapp.page.listviewtypes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.local.prefs.ListViewTypePrefs
import com.example.teammaravillaapp.model.ListViewType
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

@HiltViewModel
class ListViewTypesViewModel @Inject constructor(
    private val prefs: ListViewTypePrefs
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListViewTypesUiState())
    val uiState: StateFlow<ListViewTypesUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    init {
        // Carga inicial desde DataStore
        viewModelScope.launch {
            prefs.observe().collect { saved ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        selected = saved
                    )
                }
            }
        }
    }

    fun onSelect(type: ListViewType) {
        _uiState.update { it.copy(selected = type) }
    }

    fun onSave(onSaved: () -> Unit) {
        val selected = _uiState.value.selected
        viewModelScope.launch {
            runCatching { prefs.set(selected) }
                .onSuccess { onSaved() }
                .onFailure { _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed)) }
        }
    }
}
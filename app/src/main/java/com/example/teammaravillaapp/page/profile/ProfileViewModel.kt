package com.example.teammaravillaapp.page.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.repository.users.UsersRepository
import com.example.teammaravillaapp.data.local.prefs.ProfilePhotoPrefs
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

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val photoPrefs: ProfilePhotoPrefs,
    private val authRepository: UsersRepository
) : ViewModel() {

    val photoUri: StateFlow<String?> =
        photoPrefs.observeUri()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    fun savePhoto(uriString: String) {
        viewModelScope.launch {
            runCatching { photoPrefs.saveUri(uriString) }
                .onFailure { _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed)) }
        }
    }

    fun clearPhoto() {
        viewModelScope.launch {
            runCatching { photoPrefs.clear() }
                .onFailure { _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed)) }
        }
    }

    fun onCropError() {
        _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed))
    }

    fun logout() {
        viewModelScope.launch { authRepository.logout() }
    }
}
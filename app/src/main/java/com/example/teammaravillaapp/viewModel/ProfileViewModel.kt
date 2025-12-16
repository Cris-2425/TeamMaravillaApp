package com.example.teammaravillaapp.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(value = ProfileUiState())
    val uiState : StateFlow<ProfileUiState> = _uiState.asStateFlow()
}


data class ProfileUiState(
    val user : String = ""
) {
}
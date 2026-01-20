package com.example.teammaravillaapp.page.createlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.repository.ListsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateListViewModel @Inject constructor(
    private val listsRepository: ListsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateListUiState())
    val uiState: StateFlow<CreateListUiState> = _uiState.asStateFlow()

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onBackgroundSelect(bg: ListBackground) {
        _uiState.update { it.copy(selectedBackground = bg) }
    }

    fun onSuggestedPicked(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun save(onListCreated: (String) -> Unit) {
        val state = _uiState.value
        val newList = UserList(
            id = "",
            name = state.finalName,
            background = state.selectedBackground,
            products = emptyList()
        )

        viewModelScope.launch {
            val id = listsRepository.add(newList)
            onListCreated(id)
        }
    }
}
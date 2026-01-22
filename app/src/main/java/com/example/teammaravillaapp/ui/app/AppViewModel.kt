package com.example.teammaravillaapp.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.repository.ListsRepository
import com.example.teammaravillaapp.repository.ProductRepository
import com.example.teammaravillaapp.repository.RecipesRepository
import com.example.teammaravillaapp.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
    private val productRepository: ProductRepository,
    private val listsRepository: ListsRepository
) : ViewModel() {

    init {
        seedOnAppStart()
    }

    private fun seedOnAppStart() {
        viewModelScope.launch {
            recipesRepository.seedIfEmpty()
            listsRepository.seedIfEmpty()
            productRepository.getProducts()
        }
    }

    private val _events = MutableSharedFlow<UiEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val events: SharedFlow<UiEvent> = _events

    fun showSnackbar(message: String) {
        _events.tryEmit(UiEvent.ShowSnackbar(message))
    }
}
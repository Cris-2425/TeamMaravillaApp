package com.example.teammaravillaapp.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.data.repository.ListsRepository
import com.example.teammaravillaapp.data.repository.ProductRepository
import com.example.teammaravillaapp.data.repository.RecipesRepository
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

    private val isEmulator: Boolean =
        android.os.Build.FINGERPRINT.contains("generic")
                || android.os.Build.MODEL.contains("Emulator")
                || android.os.Build.MODEL.contains("Android SDK built for x86")

    init {
        seedOnAppStart()
    }

    private fun seedOnAppStart() {
        viewModelScope.launch {
            // ✅ siempre
            productRepository.seedIfEmpty()
            recipesRepository.seedIfEmpty()
            listsRepository.seedIfEmpty()

            // 🌐 solo si hay backend accesible
            if (isEmulator) {
                productRepository.getProducts()
            }
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
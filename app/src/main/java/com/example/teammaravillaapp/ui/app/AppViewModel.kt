package com.example.teammaravillaapp.ui.app

import android.os.Build
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.data.repository.lists.ListsRepository
import com.example.teammaravillaapp.data.repository.products.ProductRepository
import com.example.teammaravillaapp.data.repository.recipes.RecipesRepository
import com.example.teammaravillaapp.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
    private val productRepository: ProductRepository,
    private val listsRepository: ListsRepository
) : ViewModel() {

    private val isEmulator: Boolean =
        Build.FINGERPRINT.contains("generic") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK built for x86")

    private val _events = MutableSharedFlow<UiEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

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
                runCatching { productRepository.refreshProducts() }
                // emitir snackbar si falla
            }
        }
    }

    fun showSnackbar(@StringRes messageResId: Int) {
        emitSnackbar(messageResId, emptyArray())
    }

    fun showSnackbar(@StringRes messageResId: Int, vararg args: Any) {
        // Evita casts raros y asegura Array<Any>
        emitSnackbar(messageResId, args.toList().toTypedArray())
    }

    private fun emitSnackbar(
        @StringRes messageResId: Int,
        formatArgs: Array<Any>
    ) {
        _events.tryEmit(UiEvent.ShowSnackbar(messageResId, formatArgs))
    }
}
package com.example.teammaravillaapp.ui.app

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

/**
 * ViewModel de *bootstrap* de la aplicación.
 *
 * Centraliza la inicialización de datos para evitar que cada pantalla replique lógica de arranque:
 * - inserta seeds locales si el caché está vacío
 * - dispara intentos de refresh/sincronización según la implementación de cada repositorio
 *
 * ## Concurrencia
 * Ejecuta tareas en `viewModelScope` para vincular su ciclo de vida al proceso de UI, y expone
 * eventos one-shot por [SharedFlow] para integrarse con Compose.
 *
 * @property events Flujo de eventos one-shot consumible desde UI (snackbars, etc.).
 * @constructor Inyecta repositorios de dominio necesarios para bootstrap.
 *
 * @see UiEvent
 * @see RecipesRepository
 * @see ProductRepository
 * @see ListsRepository
 */
@HiltViewModel
class AppViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
    private val productRepository: ProductRepository,
    private val listsRepository: ListsRepository
) : ViewModel() {

    /**
     * Buffer de eventos one-shot.
     *
     * - `replay = 0`: evita re-entregar eventos antiguos tras rotaciones.
     * - `extraBufferCapacity = 1`: tolera picos de emisión sin suspender al emisor.
     */
    private val _events = MutableSharedFlow<UiEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )

    /**
     * Flujo público de eventos one-shot.
     *
     * Recomendación de consumo en Compose:
     * - `collectLatest` dentro de `LaunchedEffect`.
     */
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    init {
        seedOnAppStart()
    }

    /**
     * Dispara la inicialización de datos al arrancar.
     *
     * Se ejecuta como *best-effort*: un fallo en un repositorio no debe impedir inicializar otros.
     * La propagación de errores se evita usando `runCatching`.
     */
    private fun seedOnAppStart() {
        viewModelScope.launch {
            runCatching { productRepository.seedIfEmpty() }
            runCatching { recipesRepository.seedIfEmpty() }
            runCatching { listsRepository.seedIfEmpty() }
        }
    }
}
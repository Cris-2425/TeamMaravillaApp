package com.example.teammaravillaapp.ui.app

import android.os.Build
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
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
 * ViewModel de arranque global de la app.
 *
 * Responsabilidades:
 * - Ejecutar seeds iniciales (Room/local) al iniciar la app.
 * - Intentar refrescar datos remotos cuando procede (ej. emulador con backend disponible).
 * - Exponer eventos one-shot (snackbars) consumibles desde UI.
 *
 * Motivo:
 * - Centralizar el “bootstrap” de datos para evitar duplicaciones en pantallas.
 *
 * @param recipesRepository Repositorio de recetas (seed local).
 * @param productRepository Repositorio de productos (seed local + refresh remoto).
 * @param listsRepository Repositorio de listas (seed local).
 */
@HiltViewModel
class AppViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
    private val productRepository: ProductRepository,
    private val listsRepository: ListsRepository
) : ViewModel() {

    /**
     * Heurística para detectar emulador.
     *
     * Nota:
     * - No es un “contrato” oficial, solo una aproximación.
     * - Útil para habilitar refresh remoto cuando el backend solo es accesible desde emulador.
     */
    private val isEmulator: Boolean = isProbablyEmulator()

    private val _events = MutableSharedFlow<UiEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )

    /**
     * Flujo de eventos one-shot para UI (snackbars).
     *
     * Recomendación:
     * - Consumirlo con `collectLatest` en un `LaunchedEffect`.
     */
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    init {
        seedOnAppStart()
    }

    /**
     * Ejecuta la inicialización de datos al arrancar la app.
     *
     * Reglas:
     * - Seeds: siempre (best-effort).
     * - Refresh remoto: solo en emulador (según heurística).
     *
     * @throws Exception No se propaga: se maneja mediante `runCatching`.
     */
    private fun seedOnAppStart() {
        viewModelScope.launch {
            runCatching { productRepository.seedIfEmpty() }
            runCatching { recipesRepository.seedIfEmpty() }
            runCatching { listsRepository.seedIfEmpty() }
        }
        /*
                if (isEmulator) {
                    productRepository.refreshProducts()
                        .onFailure { showSnackbar(R.string.snackbar_action_failed) }
                }

         */
    }

    /**
     * Emite un snackbar simple sin parámetros de formato.
     *
     * @param messageResId String resource del mensaje.
     */
    fun showSnackbar(@StringRes messageResId: Int) {
        emitSnackbar(messageResId, emptyArray())
    }

    /**
     * Emite un snackbar con argumentos de formato (`%s`, `%d`, etc.).
     *
     * @param messageResId String resource del mensaje.
     * @param args Argumentos para `getString(messageResId, *args)`.
     */
    fun showSnackbar(@StringRes messageResId: Int, vararg args: Any) {
        emitSnackbar(messageResId, args.toList().toTypedArray())
    }

    /**
     * Emite el evento real de snackbar.
     *
     * @param messageResId String resource del mensaje.
     * @param formatArgs Argumentos de formateo. Debe ser `Array<Any>` para poder splattear.
     */
    private fun emitSnackbar(
        @StringRes messageResId: Int,
        formatArgs: Array<Any>
    ) {
        _events.tryEmit(UiEvent.ShowSnackbar(messageResId, formatArgs))
    }

    private fun isProbablyEmulator(): Boolean =
        Build.FINGERPRINT.contains("generic", ignoreCase = true) ||
                Build.MODEL.contains("Emulator", ignoreCase = true) ||
                Build.MODEL.contains("Android SDK built for x86", ignoreCase = true)
}
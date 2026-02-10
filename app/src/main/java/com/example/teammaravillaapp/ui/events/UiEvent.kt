package com.example.teammaravillaapp.ui.events

import androidx.annotation.StringRes

/**
 * Eventos one-shot para comunicación desde ViewModels/uso-casos hacia la UI.
 *
 * Motivo:
 * - Representar acciones efímeras (snackbars, diálogos, navegación puntual) sin “contaminar” el UiState.
 * - Evitar repetir lógica de UI en múltiples pantallas.
 *
 * Nota:
 * - Normalmente se consumen en Compose con `LaunchedEffect` + `collectLatest`.
 */
sealed interface UiEvent {

    /**
     * Muestra un snackbar simple (sin acción).
     *
     * @param messageResId Recurso de string del mensaje a mostrar.
     * @param formatArgs Argumentos de formateo para `getString(messageResId, *formatArgs)`.
     * Restricciones:
     * - Deben coincidir con los placeholders del string (`%s`, `%d`, etc.).
     *
     * Ejemplo:
     * {@code
     * _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_saved))
     * _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_count, arrayOf(3)))
     * }
     */
    data class ShowSnackbar(
        @StringRes val messageResId: Int,
        val formatArgs: Array<Any> = emptyArray()
    ) : UiEvent

    /**
     * Muestra un snackbar con acción (por ejemplo “Deshacer”).
     *
     * @param messageResId Recurso de string del mensaje.
     * @param actionResId Recurso de string del texto del botón de acción.
     * @param formatArgs Argumentos de formateo del mensaje.
     * @param withDismissAction Si es true, muestra icono de “cerrar”.
     * @param onAction Callback que se ejecuta si el usuario pulsa la acción.
     * @param onDismiss Callback que se ejecuta si el snackbar se descarta o expira sin acción.
     *
     * Ejemplo:
     * {@code
     * _events.tryEmit(
     *   UiEvent.ShowSnackbarAction(
     *     messageResId = R.string.snackbar_item_removed,
     *     actionResId = R.string.common_undo,
     *     onAction = { restoreItem() }
     *   )
     * )
     * }
     */
    data class ShowSnackbarAction(
        @StringRes val messageResId: Int,
        @StringRes val actionResId: Int,
        val formatArgs: Array<Any> = emptyArray(),
        val withDismissAction: Boolean = true,
        val onAction: () -> Unit,
        val onDismiss: () -> Unit = {}
    ) : UiEvent
}
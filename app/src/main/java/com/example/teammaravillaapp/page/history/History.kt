package com.example.teammaravillaapp.page.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Pantalla contenedora del historial de listas abiertas recientemente.
 *
 * Responsabilidades:
 * - Obtener el [HistoryViewModel] (Hilt).
 * - Suscribirse a [HistoryViewModel.uiState] como fuente de verdad.
 * - Consumir eventos one-shot ([HistoryViewModel.events]) y reenviarlos a la capa superior.
 * - Delegar el render a [HistoryContent] (UI pura) para facilitar previews y test.
 *
 * @param onBack Callback de navegación hacia atrás.
 * Restricciones:
 * - No debe ser nulo.
 * - Debe ser rápido (se ejecuta en UI thread).
 * @param onOpenList Callback para abrir el detalle de una lista.
 * Restricciones:
 * - No debe ser nulo.
 * - El [String] recibido es el id de la lista (no vacío).
 * @param onUiEvent Consumidor de eventos de UI (snackbars, etc.).
 * Restricciones:
 * - No debe ser nulo.
 *
 * @see HistoryContent Render “presentación” de la pantalla.
 * @see HistoryViewModel Lógica de negocio y orquestación de estado.
 *
 * Ejemplo de uso:
 * {@code
 * History(
 *   onBack = navController::popBackStack,
 *   onOpenList = { id -> navController.navigate("listDetail/$id") },
 *   onUiEvent = { event -> handleUiEvent(event) }
 * )
 * }
 */
@Composable
fun History(
    onBack: () -> Unit,
    onOpenList: (String) -> Unit,
    onUiEvent: (UiEvent) -> Unit,
    vm: HistoryViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(vm) {
        vm.events.collect { onUiEvent(it) }
    }

    HistoryContent(
        uiState = uiState,
        onBack = onBack,
        onOpenList = onOpenList,
        onClearAll = vm::onClearAll,
        onRemove = vm::onRemove
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewHistory_Loading() {
    TeamMaravillaAppTheme {
        HistoryContent(
            uiState = HistoryUiState(isLoading = true),
            onBack = {},
            onOpenList = {},
            onClearAll = {},
            onRemove = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHistory_Empty() {
    TeamMaravillaAppTheme {
        HistoryContent(
            uiState = HistoryUiState(isLoading = false, rows = emptyList()),
            onBack = {},
            onOpenList = {},
            onClearAll = {},
            onRemove = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHistory_WithRows() {
    TeamMaravillaAppTheme {
        HistoryContent(
            uiState = HistoryUiState(
                isLoading = false,
                rows = listOf(
                    HistoryRow(id = "l1", name = "Compra semanal"),
                    HistoryRow(id = "l2", name = "BBQ sábado")
                )
            ),
            onBack = {},
            onOpenList = {},
            onClearAll = {},
            onRemove = {}
        )
    }
}
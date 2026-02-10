package com.example.teammaravillaapp.page.listviewtypes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.model.ListViewType
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Pantalla contenedora para seleccionar el tipo de vista de una lista (burbujas / lista / compacto).
 *
 * Responsabilidades:
 * - Resolver el [ListViewTypesViewModel] mediante Hilt.
 * - Suscribirse al [ListViewTypesViewModel.uiState] como fuente de verdad.
 * - Escuchar eventos one-shot ([ListViewTypesViewModel.events]) y reenviarlos a [onUiEvent].
 * - Delegar el render a [ListViewTypesContent] (UI pura), facilitando tests y @Preview.
 *
 * @param onCancel Callback de navegación para cancelar la operación y cerrar la pantalla.
 * Restricciones:
 * - No nulo.
 * - Debe ser rápido (se ejecuta en UI thread).
 * @param onSaved Callback invocado cuando el usuario guarda correctamente la preferencia.
 * Restricciones:
 * - No nulo.
 * - Debe encargarse de cerrar la pantalla o navegar (p.ej. popBackStack).
 * @param onUiEvent Consumidor de eventos de UI (snackbars, etc.).
 * Restricciones:
 * - No nulo.
 * @param vm ViewModel inyectado por Hilt. Se permite override en tests.
 *
 * @see ListViewTypesContent Render de presentación.
 * @see ListViewTypesViewModel Lógica de carga/guardado de preferencias.
 *
 * Ejemplo de uso:
 * {@code
 * ListViewTypesScreen(
 *   onCancel = navController::popBackStack,
 *   onSaved = navController::popBackStack,
 *   onUiEvent = { event -> handleUiEvent(event) }
 * )
 * }
 */
@Composable
fun ListViewTypes(
    onCancel: () -> Unit,
    onSaved: () -> Unit,
    onUiEvent: (UiEvent) -> Unit,
    vm: ListViewTypesViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(vm) {
        vm.events.collect { onUiEvent(it) }
    }

    ListViewTypesContent(
        uiState = uiState,
        onCancel = onCancel,
        onSelect = vm::onSelect,
        onSave = { vm.onSave(onSaved) }
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewListViewTypes_Loading() {
    TeamMaravillaAppTheme {
        ListViewTypesContent(
            uiState = ListViewTypesUiState(isLoading = true, selected = ListViewType.BUBBLES),
            onCancel = {},
            onSelect = {},
            onSave = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewListViewTypes_SelectedBubbles() {
    TeamMaravillaAppTheme {
        ListViewTypesContent(
            uiState = ListViewTypesUiState(isLoading = false, selected = ListViewType.BUBBLES),
            onCancel = {},
            onSelect = {},
            onSave = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewListViewTypes_SelectedCompact() {
    TeamMaravillaAppTheme {
        ListViewTypesContent(
            uiState = ListViewTypesUiState(isLoading = false, selected = ListViewType.COMPACT),
            onCancel = {},
            onSelect = {},
            onSave = {}
        )
    }
}
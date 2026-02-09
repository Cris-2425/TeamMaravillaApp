package com.example.teammaravillaapp.page.createlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Route/Contenedor de la pantalla **CreateList**.
 *
 * Responsabilidad:
 * - Obtener el ViewModel mediante Hilt.
 * - Recoger [uiState] y eventos one-shot.
 * - Delegar el pintado a [CreateListContent] (UI pura).
 *
 * @param onBack Acción al cancelar/volver. No debe ser nulo.
 * @param onListCreated Acción al crear correctamente la lista. Recibe el ID creado.
 * @param onUiEvent Consumidor de eventos one-shot (snackbar, etc.).
 * @param vm ViewModel inyectado por Hilt. En ejecución normal no se pasa manualmente.
 *
 * Ejemplo:
 * {@code
 * CreateListRoute(
 *   onBack = { nav.popBackStack() },
 *   onListCreated = { id -> nav.navigate("listDetail/$id") },
 *   onUiEvent = { /* snackbar */ }
 * )
 * }
 */
@Composable
fun CreateList(
    onBack: () -> Unit,
    onListCreated: (String) -> Unit,
    onUiEvent: (UiEvent) -> Unit,
    vm: CreateListViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(vm) {
        vm.events.collect { onUiEvent(it) }
    }

    CreateListContent(
        uiState = uiState,
        onBack = onBack,
        onNameChange = vm::onNameChange,
        onBackgroundSelect = vm::onBackgroundSelect,
        onSuggestedPick = { name, ids -> vm.onSuggestedPicked(name, ids) },
        onSave = { vm.save(onListCreated) }
    )
}

@Preview(showBackground = true, name = "CreateList - Vacía")
@Composable
private fun PreviewCreateListContent_Empty() {
    TeamMaravillaAppTheme {
        CreateListContent(
            uiState = CreateListUiState(
                name = "",
                selectedBackground = ListBackground.FONDO1
            ),
            onBack = {},
            onNameChange = {},
            onBackgroundSelect = {},
            onSuggestedPick = { _, _ -> },
            onSave = {}
        )
    }
}

@Preview(showBackground = true, name = "CreateList - Con datos")
@Composable
private fun PreviewCreateListContent_Filled() {
    TeamMaravillaAppTheme {
        CreateListContent(
            uiState = CreateListUiState(
                name = "Compra semanal",
                selectedBackground = ListBackground.FONDO3
            ),
            onBack = {},
            onNameChange = {},
            onBackgroundSelect = {},
            onSuggestedPick = { _, _ -> },
            onSave = {}
        )
    }
}
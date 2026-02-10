package com.example.teammaravillaapp.page.selectlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.Recipe
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Contenedor de la pantalla “Seleccionar lista”.
 *
 * Responsabilidades:
 * - Recolectar estado desde [SelectListViewModel].
 * - Escuchar eventos one-shot del VM y reenviarlos a [onUiEvent].
 * - Delegar la UI a [SelectListContent] (presentación pura).
 *
 * @param onBack Navegación hacia atrás.
 * @param onCreateList Navegación a la creación de una nueva lista.
 * @param onListSelected Navegación al detalle de la lista (solo tras éxito).
 * @param onUiEvent Consumidor de eventos (snackbar).
 * @param vm ViewModel inyectado por Hilt (override para tests).
 *
 * @see SelectListContent
 *
 * Ejemplo de uso:
 * {@code
 * SelectList(
 *   onBack = navController::popBackStack,
 *   onCreateList = { navController.navigate(NavRoute.CreateList.route) },
 *   onListSelected = { id -> navController.navigate("listdetail/$id") },
 *   onUiEvent = { event -> ... }
 * )
 * }
 */
@Composable
fun SelectList(
    onBack: () -> Unit,
    onCreateList: () -> Unit,
    onListSelected: (String) -> Unit,
    onUiEvent: (UiEvent) -> Unit,
    vm: SelectListViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(vm) {
        vm.events.collect { onUiEvent(it) }
    }

    SelectListContent(
        uiState = uiState,
        onBack = onBack,
        onCreateList = onCreateList,
        onListClick = { listId ->
            vm.onListClicked(listId) {
                onListSelected(listId)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewSelectList_Loading() {
    TeamMaravillaAppTheme {
        SelectListContent(
            uiState = SelectListUiState(isLoading = true),
            onBack = {},
            onCreateList = {},
            onListClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSelectList_NotFound() {
    TeamMaravillaAppTheme {
        SelectListContent(
            uiState = SelectListUiState(isLoading = false, isRecipeNotFound = true),
            onBack = {},
            onCreateList = {},
            onListClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSelectList_EmptyLists() {
    val recipe = Recipe(
        id = 10,
        title = "Ensalada rápida",
        productIds = listOf("p1", "p2"),
        instructions = "Mezcla todo y listo",
        imageRes = null,
        imageUrl = null
    )

    TeamMaravillaAppTheme {
        SelectListContent(
            uiState = SelectListUiState(
                isLoading = false,
                recipe = recipe,
                lists = emptyList(),
                isSaving = false
            ),
            onBack = {},
            onCreateList = {},
            onListClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSelectList_WithLists_SavingDisabledClick() {
    val recipe = Recipe(
        id = 10,
        title = "Ensalada rápida",
        productIds = listOf("p1", "p2"),
        instructions = "Mezcla todo y listo",
        imageRes = null,
        imageUrl = null
    )

    val lists = listOf(
        UserList(id = "l1", name = "Compra semanal", background = ListBackground.FONDO1, productIds = listOf("p3")),
        UserList(id = "l2", name = "Cena especial", background = ListBackground.FONDO2, productIds = emptyList())
    )

    TeamMaravillaAppTheme {
        SelectListContent(
            uiState = SelectListUiState(
                isLoading = false,
                recipe = recipe,
                lists = lists,
                isSaving = true
            ),
            onBack = {},
            onCreateList = {},
            onListClick = {}
        )
    }
}
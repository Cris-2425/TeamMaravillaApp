package com.example.teammaravillaapp.page.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.data.repository.lists.ListProgress
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import kotlinx.coroutines.flow.collectLatest

/**
 * Pantalla **Home** (contenedor).
 *
 * Este Composable actúa como *puente* entre la capa de UI y la lógica de aplicación:
 * - Obtiene el {@link HomeViewModel} mediante Hilt.
 * - Observa el {@link HomeUiState} de forma lifecycle-aware.
 * - Escucha eventos de un solo uso (por ejemplo, Snackbars) y los delega hacia fuera.
 * - Delega el pintado real a {@link HomeContent}, manteniendo una separación clara
 *   entre contenedor (orquestación) y presentación (UI pura).
 *
 * ¿Por qué así?
 * - Facilita pruebas y previews (el Content se puede previsualizar sin ViewModel).
 * - Evita lógica de negocio dentro de Composables de UI.
 * - Cumple MVVM y las buenas prácticas de Compose.
 *
 * @param requestFocusSearch Indica si la pantalla debe solicitar foco para el campo de búsqueda al entrar.
 *        Normalmente viene de navegación (por ejemplo, cuando vuelves desde otra pantalla).
 * @param onFocusSearchConsumed Callback para “consumir” {@code requestFocusSearch} tras aplicar el foco.
 *        Debe llamarse para evitar que el foco se solicite repetidamente en recomposiciones.
 * @param onNavigateCreateList Acción de navegación hacia la pantalla de crear lista.
 * @param onNavigateRecipes Acción de navegación hacia la pantalla de recetas.
 * @param onNavigateHistory Acción de navegación hacia la pantalla de historial.
 * @param onOpenList Acción de navegación hacia el detalle de una lista concreta.
 *        Recibe el {@code id} de la lista (no nulo y no vacío).
 * @param onUiEvent Callback para propagar eventos de UI (por ejemplo, Snackbars) al scaffold superior.
 *        Permite centralizar el manejo de UI events en un nivel superior (MainScaffold).
 * @param vm Instancia del {@link HomeViewModel}. Por defecto se obtiene con {@code hiltViewModel()}.
 *
 * @throws IllegalStateException Puede ocurrir si Hilt no está correctamente configurado
 *         o si este Composable se invoca fuera de un {@code NavBackStackEntry} válido (caso raro).
 *
 * @see HomeViewModel
 * @see HomeContent
 * @see HomeUiState
 *
 * Ejemplo de uso:
 * {@code
 * NavHost(navController, startDestination = "home") {
 *   composable("home") {
 *     Home(
 *       requestFocusSearch = false,
 *       onNavigateCreateList = { navController.navigate("createList") },
 *       onOpenList = { id -> navController.navigate("listDetail/$id") },
 *       onUiEvent = { event -> scaffoldState.showSnackbar(...) }
 *     )
 *   }
 * }
 */
@Composable
fun Home(
    requestFocusSearch: Boolean,
    onFocusSearchConsumed: () -> Unit = {},
    onNavigateCreateList: () -> Unit = {},
    onNavigateRecipes: () -> Unit = {},
    onNavigateHistory: () -> Unit = {},
    onOpenList: (String) -> Unit = {},
    onUiEvent: (UiEvent) -> Unit = {},
    vm: HomeViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    // Eventos one-shot (snackbar, navegación puntual, etc.)
    // Preguntar si se puede hacer a nivel global esto
    LaunchedEffect(vm) {
        vm.events.collectLatest { onUiEvent(it) }
    }

    HomeContent(
        uiState = uiState,
        requestFocusSearch = requestFocusSearch,
        onFocusSearchConsumed = onFocusSearchConsumed,
        onSearchChange = vm::onSearchChange,
        onNavigateCreateList = onNavigateCreateList,
        onNavigateRecipes = onNavigateRecipes,
        onNavigateHistory = onNavigateHistory,
        onOpenList = { id ->
            vm.onOpenList(id)
            onOpenList(id)
        },
        onDelete = vm::requestDelete,
        onRename = vm::renameList
    )
}

@Preview(showBackground = true, name = "Home - Vacía")
@Composable
private fun PreviewHome_Empty() {
    TeamMaravillaAppTheme {
        HomeContent(
            uiState = HomeUiState(search = "", rows = emptyList()),
            requestFocusSearch = false,
            onFocusSearchConsumed = {},
            onSearchChange = {},
            onNavigateCreateList = {},
            onNavigateRecipes = {},
            onNavigateHistory = {},
            onOpenList = {},
            onDelete = {},
            onRename = { _, _ -> }
        )
    }
}

@Preview(showBackground = true, name = "Home - Con listas")
@Composable
private fun PreviewHome_WithRows() {
    val list1 = UserList(
        id = "list-1",
        name = "Compra semanal",
        background = ListBackground.FONDO1,
        productIds = emptyList()
    )
    val list2 = UserList(
        id = "list-2",
        name = "BBQ sábado",
        background = ListBackground.FONDO4,
        productIds = emptyList()
    )

    TeamMaravillaAppTheme {
        HomeContent(
            uiState = HomeUiState(
                search = "com",
                rows = listOf(
                    HomeListRow(
                        id = list1.id,
                        list = list1,
                        progress = ListProgress(checkedCount = 2, totalCount = 12)
                    ),
                    HomeListRow(
                        id = list2.id,
                        list = list2,
                        progress = ListProgress(checkedCount = 0, totalCount = 0)
                    )
                )
            ),
            requestFocusSearch = false,
            onFocusSearchConsumed = {},
            onSearchChange = {},
            onNavigateCreateList = {},
            onNavigateRecipes = {},
            onNavigateHistory = {},
            onOpenList = {},
            onDelete = {},
            onRename = { _, _ -> }
        )
    }
}
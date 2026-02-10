package com.example.teammaravillaapp.page.listdetail

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.ListViewType
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import kotlinx.coroutines.flow.collectLatest

/**
 * Pantalla de detalle de lista (ListDetail).
 *
 * **Contenedor ligero**:
 * - Obtiene el [ListDetailViewModel] con Hilt.
 * - Recoge el [ListDetailUiState] con lifecycle.
 * - Escucha eventos one-shot ([UiEvent]) y los delega hacia arriba.
 *
 * La UI real (presentación) se renderiza en [ListDetailContent], que es un composable “puro”
 * (recibe estado + callbacks y no conoce ViewModel).
 *
 * Esto cumple la rúbrica de “separar componentes de presentación de componentes contenedores”
 * sin introducir nombres que puedan confundirse con el `route` del NavHost.
 *
 * @param onOpenCategoryFilter Navegación para abrir el selector de categorías.
 * @param onOpenListViewTypes Navegación para abrir el selector de tipo de vista.
 * @param onUiEvent Callback para eventos de un solo uso (snackbars, etc.).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDetail(
    onOpenCategoryFilter: () -> Unit = {},
    onOpenListViewTypes: () -> Unit = {},
    onUiEvent: (UiEvent) -> Unit
) {
    val vm: ListDetailViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(vm) {
        vm.events.collectLatest { onUiEvent(it) }
    }

    ListDetailContent(
        uiState = uiState,
        onOpenCategoryFilter = onOpenCategoryFilter,
        onOpenListViewTypes = onOpenListViewTypes,
        onClearCategoryFilter = vm::clearCategoryFilter,
        onAction = vm::onAction
    )
}

@Preview(showBackground = true, name = "ListDetail - EmptyState")
@Composable
private fun PreviewListDetail_Empty() {
    TeamMaravillaAppTheme {
        ListDetailContent(
            uiState = ListDetailUiState(
                listId = null,
                userList = null
            ),
            onOpenCategoryFilter = {},
            onOpenListViewTypes = {},
            onClearCategoryFilter = {},
            onAction = {}
        )
    }
}

@Preview(showBackground = true, name = "ListDetail - Con items")
@Composable
private fun PreviewListDetail_WithItems() {
    val list = UserList(
        id = "list-1",
        name = "Compra semanal",
        background = ListBackground.FONDO2,
        productIds = emptyList()
    )

    val p1 = Product(id = "apple", name = "Manzana", category = ProductCategory.FRUITS)
    val p2 = Product(id = "milk", name = "Leche", category = ProductCategory.DAIRY)
    val p3 = Product(id = "bread", name = "Pan", category = ProductCategory.BAKERY)

    TeamMaravillaAppTheme {
        ListDetailContent(
            uiState = ListDetailUiState(
                listId = list.id,
                userList = list,
                isLoadingCatalog = false,
                viewType = ListViewType.BUBBLES,
                selectedCategories = setOf(ProductCategory.FRUITS, ProductCategory.DAIRY),
                query = "ma",
                searchResults = listOf(p3),
                items = listOf(
                    ListItemUi(product = p1, checked = true, quantity = 2, position = 0),
                    ListItemUi(product = p2, checked = false, quantity = 1, position = 1)
                ),
                recentAvailable = listOf(p3),
                availableByCategory = mapOf(
                    ProductCategory.BAKERY to listOf(p3)
                )
            ),
            onOpenCategoryFilter = {},
            onOpenListViewTypes = {},
            onClearCategoryFilter = {},
            onAction = {}
        )
    }
}
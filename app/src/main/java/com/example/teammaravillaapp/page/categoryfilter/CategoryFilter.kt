package com.example.teammaravillaapp.page.categoryfilter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Route/Contenedor de **CategoryFilter**.
 *
 * Responsabilidad:
 * - Obtener ViewModel con Hilt.
 * - Recoger uiState y eventos.
 * - Delegar el pintado a {@link CategoryFilterContent} (UI pura).
 *
 * @param onSave Acción de UI cuando el usuario confirma guardar (normalmente navegar atrás).
 * @param onCancel Acción al cancelar (normalmente navegar atrás sin cambios).
 * @param onUiEvent Consumidor de eventos one-shot (snackbars, etc.). No debe ser nulo.
 * @param vm ViewModel inyectado por Hilt. En ejecución normal no se pasa manualmente.
 *
 * Ejemplo de uso:
 * {@code
 * CategoryFilterRoute(
 *   onSave = { navController.popBackStack() },
 *   onCancel = { navController.popBackStack() },
 *   onUiEvent = { event -> /* snackbar */ }
 * )
 * }
 */
@Composable
fun CategoryFilter(
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onUiEvent: (UiEvent) -> Unit,
    vm: CategoryFilterViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(vm) {
        vm.events.collect { onUiEvent(it) }
    }

    CategoryFilterContent(
        uiState = uiState,
        onCancel = onCancel,
        onSave = { vm.onSave(onSave) },
        onToggleAll = vm::onToggleAll,
        onToggle = vm::onToggle
    )
}

/* ----------------------------- PREVIEWS ----------------------------- */

@Preview(showBackground = true, name = "CategoryFilter - Cargando")
@Composable
private fun PreviewCategoryFilter_Loading() {
    TeamMaravillaAppTheme {
        CategoryFilterContent(
            uiState = CategoryFilterUiState(isLoading = true),
            onSave = {},
            onCancel = {},
            onToggleAll = {},
            onToggle = {}
        )
    }
}

@Preview(showBackground = true, name = "CategoryFilter - Con selección")
@Composable
private fun PreviewCategoryFilter_Selected() {
    TeamMaravillaAppTheme {
        CategoryFilterContent(
            uiState = CategoryFilterUiState(
                isLoading = false,
                selected = setOf(ProductCategory.FRUITS, ProductCategory.DAIRY, ProductCategory.MEAT)
            ),
            onSave = {},
            onCancel = {},
            onToggleAll = {},
            onToggle = {}
        )
    }
}
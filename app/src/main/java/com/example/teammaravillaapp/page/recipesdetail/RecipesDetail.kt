package com.example.teammaravillaapp.page.recipesdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.model.Recipe
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import kotlinx.coroutines.flow.collectLatest

/**
 * Contenedor de la pantalla de detalle de receta.
 *
 * Responsabilidades:
 * - Recolectar estado desde [RecipesDetailViewModel].
 * - Escuchar eventos one-shot y reenviarlos a [onUiEvent].
 * - Delegar el render de UI a [RecipesDetailContent].
 *
 * @param onBack Navegación hacia atrás.
 * Restricciones: no nulo.
 * @param onAddToShoppingList Acción al pulsar “Añadir ingredientes”.
 * Restricciones:
 * - Debe aceptar un id de receta válido (>0).
 * @param onUiEvent Consumidor de eventos one-shot (snackbars).
 * @param vm ViewModel inyectado por Hilt (override permitido en tests).
 *
 * @see RecipesDetailContent Presentación pura.
 *
 * Ejemplo de uso:
 * {@code
 * RecipesDetail(
 *   onBack = navController::popBackStack,
 *   onAddToShoppingList = { recipeId -> ... },
 *   onUiEvent = { event -> ... }
 * )
 * }
 */
@Composable
fun RecipesDetail(
    onBack: () -> Unit,
    onAddToShoppingList: (Int) -> Unit,
    onUiEvent: (UiEvent) -> Unit,
    vm: RecipesDetailViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(vm) {
        vm.events.collectLatest { onUiEvent(it) }
    }

    RecipesDetailContent(
        uiState = uiState,
        onToggleFavorite = vm::toggleFavorite,
        onAddToShoppingList = onAddToShoppingList,
        onBack = onBack
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewRecipesDetail_Loading() {
    TeamMaravillaAppTheme {
        RecipesDetailContent(
            uiState = RecipesDetailUiState(isLoading = true),
            onToggleFavorite = {},
            onAddToShoppingList = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRecipesDetail_NotFound() {
    TeamMaravillaAppTheme {
        RecipesDetailContent(
            uiState = RecipesDetailUiState(isLoading = false, isNotFound = true),
            onToggleFavorite = {},
            onAddToShoppingList = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRecipesDetail_Content() {
    val recipe = Recipe(
        id = 1,
        title = "Tortilla de patatas",
        productIds = listOf("p1", "p2", "p3"),
        instructions = "1) Pelar patatas\n2) Freír\n3) Mezclar con huevo\n4) Cuajar",
        imageRes = null,
        imageUrl = null
    )

    val ingredients = listOf(
        Product(id = "p1", name = "Patatas", category = ProductCategory.OTHER),
        Product(id = "p2", name = "Huevos", category = ProductCategory.OTHER),
        Product(id = "p3", name = "Cebolla", category = ProductCategory.OTHER)
    )

    TeamMaravillaAppTheme {
        RecipesDetailContent(
            uiState = RecipesDetailUiState(
                isLoading = false,
                recipe = recipe,
                ingredients = ingredients,
                isFavorite = true
            ),
            onToggleFavorite = {},
            onAddToShoppingList = {},
            onBack = {}
        )
    }
}
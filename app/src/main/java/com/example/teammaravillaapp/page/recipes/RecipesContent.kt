package com.example.teammaravillaapp.page.recipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.FilterRecipes
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.RecipeCard
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.model.Recipe
import com.example.teammaravillaapp.model.RecipeWithIngredients
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * UI pura de la pantalla de Recetas.
 *
 * Esta función NO:
 * - conoce ViewModels ni repositorios
 * - lanza side effects
 * - accede a navegación directamente (solo callbacks)
 *
 * Renderiza:
 * - título dinámico (Todas / Mis recetas)
 * - filtros (All vs Mine)
 * - estados: loading / empty / lista de recetas
 *
 * @param uiState Estado de UI ya resuelto. Restricciones: no nulo.
 * @param onShowAll Acción al seleccionar el filtro “Todas”. Restricciones: no nulo.
 * @param onShowMine Acción al seleccionar el filtro “Mis recetas”. Restricciones: no nulo.
 * @param onToggleFavorite Acción para alternar favorito. Recibe id de receta. Restricciones: id > 0 recomendado.
 * @param onRecipeClick Acción al pulsar una receta. Recibe id de receta. Restricciones: id > 0 recomendado.
 * @param onBack Acción para volver (si decides pintar botón). Restricciones: no nulo.
 *
 * @throws IllegalArgumentException No se lanza directamente, pero los callbacks deberían validar ids si vienen
 * de una fuente externa no confiable.
 *
 * @see RecipesUiState Modelo de estado de pantalla.
 * @see RecipeCard Card de receta.
 *
 * Ejemplo de uso:
 * {@code
 * RecipesContent(
 *   uiState = state,
 *   onShowAll = { vm.setShowMine(false) },
 *   onShowMine = { vm.setShowMine(true) },
 *   onToggleFavorite = { id -> vm.toggleFavorite(id) },
 *   onRecipeClick = { id -> navController.navigate("recipe/$id") },
 *   onBack = { navController.popBackStack() }
 * )
 * }
 */
@Composable
fun RecipesContent(
    uiState: RecipesUiState,
    onShowAll: () -> Unit,
    onShowMine: () -> Unit,
    onToggleFavorite: (Int) -> Unit,
    onRecipeClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        GeneralBackground(overlayAlpha = 0.20f) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
            ) {
                Spacer(Modifier.height(12.dp))

                Surface(
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterRecipes(
                            text = stringResource(R.string.recipes_filter_all),
                            selected = !uiState.showMine,
                            onClick = onShowAll
                        )
                        FilterRecipes(
                            text = stringResource(R.string.recipes_filter_mine),
                            selected = uiState.showMine,
                            onClick = onShowMine
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                when {
                    uiState.isLoading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = stringResource(R.string.common_loading),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    uiState.isEmpty -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = if (uiState.showMine)
                                    stringResource(R.string.recipes_empty_mine)
                                else
                                    stringResource(R.string.recipes_empty_all),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            contentPadding = PaddingValues(bottom = 120.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(uiState.visibleRecipes) { item ->
                                RecipeCard(
                                    recipe = item.recipe,
                                    ingredients = item.ingredients,
                                    isFavorite = item.recipe.id in uiState.favoriteIds,
                                    onToggleFavorite = { onToggleFavorite(item.recipe.id) },
                                    onClick = { onRecipeClick(item.recipe.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRecipesContent_Loading() {
    TeamMaravillaAppTheme {
        RecipesContent(
            uiState = RecipesUiState(isLoading = true),
            onShowAll = {},
            onShowMine = {},
            onToggleFavorite = {},
            onRecipeClick = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRecipesContent_EmptyMine() {
    TeamMaravillaAppTheme {
        RecipesContent(
            uiState = RecipesUiState(isLoading = false, showMine = true, visibleRecipes = emptyList()),
            onShowAll = {},
            onShowMine = {},
            onToggleFavorite = {},
            onRecipeClick = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRecipesContent_WithItems() {
    TeamMaravillaAppTheme {
        RecipesContent(
            uiState = RecipesUiState(
                isLoading = false,
                showMine = false,
                favoriteIds = setOf(1),
                visibleRecipes = listOf(fakeRecipeWithIngredients(id = 1), fakeRecipeWithIngredients(id = 2))
            ),
            onShowAll = {},
            onShowMine = {},
            onToggleFavorite = {},
            onRecipeClick = {},
            onBack = {}
        )
    }
}

/**
 * Helper de preview (solo UI): crea un [RecipeWithIngredients] mínimo y consistente con tu modelo [Recipe].
 *
 * Motivo: permitir @Preview sin depender de Hilt/Room/Retrofit.
 *
 * @param id Identificador de la receta. Restricciones: debe ser > 0.
 * @return Una instancia de [RecipeWithIngredients] con receta e ingredientes falsos (de preview).
 *
 * Ejemplo de uso:
 * {@code
 * val item = fakeRecipeWithIngredients(1)
 * }
 */
private fun fakeRecipeWithIngredients(id: Int): RecipeWithIngredients {
    val ingredients = listOf(
        Product(
            id = "p1",
            name = "Tomate",
            category = ProductCategory.VEGETABLES,
            imageUrl = null
        ),
        Product(
            id = "p2",
            name = "Aceite de oliva",
            category = ProductCategory.OTHER,
            imageUrl = null
        )
    )

    return RecipeWithIngredients(
        recipe = Recipe(
            id = id,
            title = "Receta $id",
            productIds = ingredients.map { it.id },
            instructions = "1) Preparar ingredientes\n2) Cocinar\n3) Servir",
            imageRes = null,
            imageUrl = null
        ),
        ingredients = ingredients
    )
}
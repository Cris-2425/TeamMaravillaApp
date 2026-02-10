package com.example.teammaravillaapp.page.recipes

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Pantalla contenedora de Recetas.
 *
 * Responsabilidades:
 * - Obtener el [RecipesViewModel] mediante Hilt (o recibirlo por parámetro en tests).
 * - Recolectar el estado reactivo ([RecipesUiState]) con lifecycle-awareness.
 * - Delegar el render a [RecipesContent] (presentación pura).
 *
 * Motivo del diseño:
 * - Separar “plataforma/estado” de “UI pura” facilita previews, test y mantenimiento.
 *
 * @param onBack Callback de navegación hacia atrás. Restricciones: no nulo.
 * @param onRecipeClick Callback al pulsar una receta. Recibe el id de receta. Restricciones: no nulo.
 * @param vm ViewModel inyectado por Hilt. Se permite override para tests.
 *
 * @see RecipesContent UI pura.
 * @see RecipesViewModel Orquestación de filtros, favoritos y catálogo.
 *
 * Ejemplo de uso:
 * {@code
 * RecipesScreen(
 *   onBack = { navController.popBackStack() },
 *   onRecipeClick = { recipeId -> navController.navigate("recipe/$recipeId") }
 * )
 * }
 */
@Composable
fun Recipes(
    onBack: () -> Unit = {},
    onRecipeClick: (Int) -> Unit = {},
    vm: RecipesViewModel = hiltViewModel()
) {
    val uiState = vm.uiState.collectAsStateWithLifecycle().value

    RecipesContent(
        uiState = uiState,
        onShowAll = { vm.setShowMine(false) },
        onShowMine = { vm.setShowMine(true) },
        onToggleFavorite = vm::toggleFavorite,
        onRecipeClick = onRecipeClick,
        onBack = onBack
    )
}

/**
 * Preview mínimo de Screen.
 *
 * Nota:
 * - No usamos Hilt aquí para evitar crashes en Preview.
 * - Para preview de UI real, usa [PreviewRecipesContent_*].
 */
@Preview(showBackground = true)
@Composable
private fun PreviewRecipesScreen_Minimal() {
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
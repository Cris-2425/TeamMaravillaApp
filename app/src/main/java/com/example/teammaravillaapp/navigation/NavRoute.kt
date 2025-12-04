package com.example.teammaravillaapp.navigation

/**
 * Conjunto de **rutas de navegación** usadas por la aplicación.
 *
 * Se utiliza una `sealed class` para:
 * - Garantizar un único origen de verdad para todas las rutas.
 * - Facilitar la autocompletación y evitar errores tipográficos.
 * - Permitir rutas con parámetros (por ejemplo, `RecipeDetail/{id}`).
 *
 * Cada objeto representa una pantalla de la app.
 */

sealed class NavRoute(val route: String) {

    data object Home : NavRoute("home")

    data object CreateList : NavRoute("create_list")

    data object ListDetail : NavRoute("list_detail/{listId}") {
        const val ARG_LIST_ID = "listId"
        fun createRoute(listId: String): String =
            "list_detail/$listId"
    }

    data object Recipes : NavRoute("recipes")

    data object RecipesDetail : NavRoute("recipes_detail/{recipeId}") {
        const val ARG_RECIPE_ID = "recipeId"
        fun createRoute(recipeId: Int): String = "recipes_detail/$recipeId"
    }

    data object Profile : NavRoute("profile")

    data object Login : NavRoute("login")

    data object ListViewTypes : NavRoute("list_view_types")

    data object CategoryFilter : NavRoute("category_filter")
}
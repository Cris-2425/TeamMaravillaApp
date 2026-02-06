package com.example.teammaravillaapp.navigation

sealed class NavRoute(val route: String) {

    data object Home : NavRoute("home")
    data object CreateList : NavRoute("create_list")

    data object ListDetail : NavRoute("list_detail/{listId}") {
        const val ARG_LIST_ID = "listId"
        fun createRoute(listId: String): String = "list_detail/$listId"
    }

    data object Recipes : NavRoute("recipes")

    data object RecipesDetail : NavRoute("recipes_detail/{recipeId}") {
        const val ARG_RECIPE_ID = "recipeId"
        fun createRoute(recipeId: Int): String = "recipes_detail/$recipeId"
    }

    data object SelectList : NavRoute("select_list/{recipeId}") {
        const val ARG_RECIPE_ID = "recipeId"
        fun createRoute(recipeId: Int): String = "select_list/$recipeId"
    }

    data object Profile : NavRoute("profile")
    data object Login : NavRoute("login")

    data object ListViewTypes : NavRoute("list_view_types")
    data object CategoryFilter : NavRoute("category_filter")
    data object ProductsDebug : NavRoute("products_debug")

    data object Camera : NavRoute("camera?listId={listId}") {
        const val ARG_LIST_ID = "listId"
        fun createRoute(listId: String? = null): String =
            if (listId.isNullOrBlank()) "camera" else "camera?listId=$listId"
    }

    data object Splash : NavRoute("splash")
    data object Settings : NavRoute("settings")
    data object Stats : NavRoute("stats")
    data object Help : NavRoute("help")
    data object History : NavRoute("history")
    data object Register : NavRoute("register")
}

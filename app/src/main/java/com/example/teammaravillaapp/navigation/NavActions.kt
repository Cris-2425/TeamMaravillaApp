package com.example.teammaravillaapp.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavGraph.Companion.findStartDestination

/**
 * Conjunto de acciones de navegación tipadas para centralizar rutas y opciones.
 *
 * Objetivos:
 * - Evitar strings sueltos por la UI.
 * - Unificar patrones de navegación (singleTop, restoreState, popUpTo).
 *
 * @param navController Controlador de navegación.
 */
class NavActions(private val navController: NavHostController) {

    // -------- Helpers --------

    private fun navigate(route: String, builder: (NavOptionsBuilder.() -> Unit)? = null) {
        navController.navigate(route) { builder?.invoke(this) }
    }

    fun up(): Boolean = navController.navigateUp()

    /**
     * Vuelve a la raíz del grafo (destino inicial).
     *
     * Útil para resetear la pila tras login/logout.
     */
    // No se usa, pero está curioso para otros proyectos y tener la referencia
    fun popToRoot() {
        val startId = navController.graph.findStartDestination().id
        navController.popBackStack(startId, inclusive = false)
    }

    // -------- Top level --------

    fun toHome() = navigate(NavRoute.Home.route) {
        val startId = navController.graph.findStartDestination().id
        popUpTo(startId) { inclusive = false }
        launchSingleTop = true
    }

    fun toLogin() = navigate(NavRoute.Login.route) {
        val startId = navController.graph.findStartDestination().id
        popUpTo(startId) { inclusive = false }
        launchSingleTop = true
    }

    fun toSplash() = navigate(NavRoute.Splash.route) {
        val startId = navController.graph.findStartDestination().id
        popUpTo(startId) { inclusive = false }
        launchSingleTop = true
    }

    // -------- Main flow --------

    fun toCreateList() = navigate(NavRoute.CreateList.route)

    fun toListDetail(listId: String) =
        navigate(NavRoute.ListDetail.createRoute(listId))

    fun toRecipes() = navigate(NavRoute.Recipes.route)

    fun toRecipesDetail(recipeId: Int) =
        navigate(NavRoute.RecipesDetail.createRoute(recipeId))

    fun toSelectList(recipeId: Int) =
        navigate(NavRoute.SelectList.createRoute(recipeId))

    fun toProfile() = navigate(NavRoute.Profile.route)
    fun toSettings() = navigate(NavRoute.Settings.route)
    fun toStats() = navigate(NavRoute.Stats.route)
    fun toHelp() = navigate(NavRoute.Help.route)
    fun toHistory() = navigate(NavRoute.History.route)

    fun toCamera(listId: String? = null) =
        navigate(NavRoute.Camera.createRoute(listId))

    fun toListViewTypes() = navigate(NavRoute.ListViewTypes.route)
    fun toCategoryFilter() = navigate(NavRoute.CategoryFilter.route)

    fun toRegister() = navigate(NavRoute.Register.route) { launchSingleTop = true }

    // -------- SingleTop helpers --------

    fun toListDetailAndRemove(routeToPop: String, listId: String) =
        navigate(NavRoute.ListDetail.createRoute(listId)) {
            popUpTo(routeToPop) { inclusive = true }
            launchSingleTop = true
        }

    // -------- Tabs: singleTop + restoreState --------

    fun toHomeSingleTop() = navigate(NavRoute.Home.route) {
        popUpTo(NavRoute.Home.route) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }

    fun toRecipesSingleTop() = navigate(NavRoute.Recipes.route) {
        popUpTo(NavRoute.Home.route) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }

    fun toHistorySingleTop() = navigate(NavRoute.History.route) {
        popUpTo(NavRoute.Home.route) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }

    fun toProfileSingleTop() = navigate(NavRoute.Profile.route) {
        popUpTo(NavRoute.Home.route) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
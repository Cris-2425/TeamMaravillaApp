package com.example.teammaravillaapp.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder

class NavActions(private val navController: NavHostController) {

    // -------- Helpers --------

    private fun navigate(route: String, builder: (NavOptionsBuilder.() -> Unit)? = null) {
        navController.navigate(route) { builder?.invoke(this) }
    }

    fun up() = navController.navigateUp()

    fun popToRoot() {
        navController.popBackStack(route = NavRoute.Home.route, inclusive = false)
    }

    // -------- Top level --------

    fun toHome() = navigate(NavRoute.Home.route) {
        popUpTo(0)
        launchSingleTop = true
    }

    fun toLogin() = navigate(NavRoute.Login.route) {
        popUpTo(0)
        launchSingleTop = true
    }

    fun toSplash() = navigate(NavRoute.Splash.route) {
        popUpTo(0)
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

    fun toProductsDebug() = navigate(NavRoute.ProductsDebug.route)

    fun toCamera(listId: String? = null) =
        navigate(NavRoute.Camera.createRoute(listId))

    fun toListViewTypes() = navigate(NavRoute.ListViewTypes.route)

    fun toCategoryFilter() = navigate(NavRoute.CategoryFilter.route)

    // -------- SingleTop helpers (opcional) --------

    fun toCreateListAndClearFromBackStack() = navigate(NavRoute.CreateList.route) {
        launchSingleTop = true
    }

    fun toListDetailAndRemove(routeToPop: String, listId: String) =
        navigate(NavRoute.ListDetail.createRoute(listId)) {
            popUpTo(routeToPop) { inclusive = true }
            launchSingleTop = true
        }
    fun toRegister() {
        navController.navigate(NavRoute.Register.route) {
            launchSingleTop = true
        }
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
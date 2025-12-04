package com.example.teammaravillaapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.teammaravillaapp.model.ListStyle
import com.example.teammaravillaapp.model.ProfileOption
import com.example.teammaravillaapp.page.CategoryFilter
import com.example.teammaravillaapp.page.CreateListt
import com.example.teammaravillaapp.page.Home
import com.example.teammaravillaapp.page.ListDetail
import com.example.teammaravillaapp.page.ListViewTypes
import com.example.teammaravillaapp.page.Login
import com.example.teammaravillaapp.page.Profile
import com.example.teammaravillaapp.page.Recipes
import com.example.teammaravillaapp.page.RecipesDetail

/**
 * `NavHost` principal de la aplicación.
 *
 * Define TODAS las pantallas y sus rutas, así como las transiciones entre ellas.
 *
 * Este NavHost conecta:
 * - Home
 * - Crear lista
 * - Detalle de lista
 * - Recetas
 * - Detalle de receta
 * - Perfil
 * - Login
 * - Filtro de vista de listas
 * - Filtro de categorías
 *
 * ## Responsabilidades:
 * - Gestionar navegación declarativa con Navigation Compose.
 * - Recibir `NavHostController` desde `MainActivity`.
 * - Proveer callbacks de navegación a cada pantalla.
 *
 * @param navController Controlador de navegación compartido.
 * @param modifier Modificador opcional para posicionar el NavHost.
 * @param startDestination Ruta inicial (por defecto, Home).
 */

@Composable
fun TeamMaravillaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = NavRoute.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        // HOME
        composable(NavRoute.Home.route) {
            Home(
                onNavigateCreateList = {
                    navController.navigate(NavRoute.CreateList.route)
                },
                onNavigateHome = {
                    navController.navigate(NavRoute.Home.route) {
                        popUpTo(NavRoute.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateProfile = {
                    navController.navigate(NavRoute.Profile.route)
                },
                onNavigateCamera = {
                    // futuro: pantalla de cámara / escáner
                },
                onNavigateRecipes = {
                    navController.navigate(NavRoute.Recipes.route)
                },
                onExitApp = {
                    // hacer navController.popBackStack()
                    // o delegar en la Activity si quieres cerrar la app.
                },
                onOpenList = { listId ->
                    navController.navigate(NavRoute.ListDetail.createRoute(listId))
                }
            )
        }

        // CREAR LISTA
        composable(NavRoute.CreateList.route) {
            CreateListt(
                onBack = { navController.navigateUp() },
                onListCreated = { listId ->
                    // navega directamente al detalle de la lista creada
                    navController.navigate(NavRoute.ListDetail.createRoute(listId))
                }
            )
        }

        // DETALLE DE LISTA
        composable(
            route = NavRoute.ListDetail.route,
            arguments = listOf(
                navArgument(NavRoute.ListDetail.ARG_LIST_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getString(NavRoute.ListDetail.ARG_LIST_ID)

            ListDetail(
                listId = listId,
                onBack = { navController.navigateUp() }
            )
        }

        // RECETAS
        composable(NavRoute.Recipes.route) {
            Recipes(
                onBack = { navController.navigateUp() },
                onRecipeClick = { recipeId ->
                    navController.navigate(NavRoute.RecipesDetail.createRoute(recipeId))
                }
            )
        }

        // DETALLE DE RECETA
        composable(
            route = NavRoute.RecipesDetail.route,
            arguments = listOf(
                navArgument(NavRoute.RecipesDetail.ARG_RECIPE_ID) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val recipeId =
                backStackEntry.arguments?.getInt(NavRoute.RecipesDetail.ARG_RECIPE_ID) ?: -1

            RecipesDetail(
                recipeId = recipeId,
                onBack = { navController.navigateUp() },
                onAddToShoppingList = { _ ->
                    // Abrir un diálogo para elegir lista
                    // o añadir a una lista por defecto.
                }
            )
        }

        // PERFIL
        composable(NavRoute.Profile.route) {
            Profile(
                onBack = { navController.navigateUp() },
                onNavigate = { option ->
                    when (option) {
                        ProfileOption.LISTS -> {
                            // Por ahora volvemos a Home, más adelante
                            // navegar a una pantalla de "Mis listas" si la creo.
                            navController.navigate(NavRoute.Home.route)
                        }
                        ProfileOption.RECIPES -> {
                            navController.navigate(NavRoute.Recipes.route)
                        }
                        ProfileOption.SETTINGS -> {
                            // TODO: crear pantalla de ajustes si hace falta
                        }
                        ProfileOption.STATS -> {
                            // TODO: crear pantalla de estadísticas
                        }
                        ProfileOption.HELP -> {
                            // TODO: pantalla de ayuda
                        }
                        ProfileOption.LOGIN -> {
                            navController.navigate(NavRoute.Login.route)
                        }
                    }
                }
            )
        }

        // LOGIN
        composable(NavRoute.Login.route) {
            Login(
                onBack = { navController.navigateUp() },
                onLoginSuccess = { _username ->
                    navController.navigate(NavRoute.Home.route) {
                        popUpTo(NavRoute.Home.route) { inclusive = false }
                    }
                }
            )
        }

        // TIPO DE VISTA DE LISTA
        composable(NavRoute.ListViewTypes.route) {
            ListViewTypes(
                selected = ListStyle.LISTA,
                onSelect = { _style ->
                },
                onCancel = { navController.navigateUp() },
                onSave = {
                    navController.navigateUp()
                }
            )
        }

        // FILTRO DE CATEGORÍAS
        composable(NavRoute.CategoryFilter.route) {
            CategoryFilter(
                onCancel = { navController.navigateUp() },
                onSave = {
                    navController.navigateUp()
                }
            )
        }
    }
}
package com.example.teammaravillaapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.teammaravillaapp.data.auth.AuthRepository
import com.example.teammaravillaapp.model.ListStyle
import com.example.teammaravillaapp.model.ProfileOption
import com.example.teammaravillaapp.page.CategoryFilter
import com.example.teammaravillaapp.page.ListViewTypes
import com.example.teammaravillaapp.page.Profile
import com.example.teammaravillaapp.page.recipes.Recipes
import com.example.teammaravillaapp.page.recipesdetail.RecipesDetail
import com.example.teammaravillaapp.page.createlist.CreateListt
import com.example.teammaravillaapp.page.home.Home
import com.example.teammaravillaapp.page.listdetail.ListDetail
import com.example.teammaravillaapp.page.login.Login
import com.example.teammaravillaapp.page.login.LoginViewModel
import com.example.teammaravillaapp.page.login.LoginViewModelFactory
import com.example.teammaravillaapp.page.selectlist.SelectList
import com.example.teammaravillaapp.page.session.SessionEvent
import com.example.teammaravillaapp.page.session.SessionViewModel
import com.example.teammaravillaapp.ui.app.AppViewModel
import kotlinx.coroutines.launch

@Composable
fun TeamMaravillaNavHost(
    navController: NavHostController,
    sessionViewModel: SessionViewModel,
    authRepository: AuthRepository,
    appViewModel: AppViewModel,
    modifier: Modifier = Modifier,
    startDestination: String = NavRoute.Home.route
) {
    val scope = rememberCoroutineScope()

    val isLoggedIn by sessionViewModel.isLoggedIn.collectAsState()
    val username by sessionViewModel.username.collectAsState()

    LaunchedEffect(Unit) {
        sessionViewModel.events.collect { event ->
            when (event) {
                SessionEvent.LoggedIn -> {
                    navController.navigate(NavRoute.Home.route) {
                        popUpTo(NavRoute.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }

                SessionEvent.LoggedOut -> {
                    navController.navigate(NavRoute.Login.route) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(NavRoute.Home.route) {
            Home(
                onNavigateCreateList = { navController.navigate(NavRoute.CreateList.route) },
                onNavigateHome = {
                    navController.navigate(NavRoute.Home.route) {
                        popUpTo(NavRoute.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateProfile = { navController.navigate(NavRoute.Profile.route) },
                onNavigateCamera = { },
                onNavigateRecipes = { navController.navigate(NavRoute.Recipes.route) },
                onExitApp = { },
                onOpenList = { listId ->
                    navController.navigate(NavRoute.ListDetail.createRoute(listId))
                }
            )
        }

        composable(NavRoute.CreateList.route) {
            CreateListt(
                onBack = { navController.navigateUp() },
                onListCreated = { listId ->
                    navController.navigate(NavRoute.ListDetail.createRoute(listId)) {
                        popUpTo(NavRoute.CreateList.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = NavRoute.ListDetail.route,
            arguments = listOf(
                navArgument(NavRoute.ListDetail.ARG_LIST_ID) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getString(NavRoute.ListDetail.ARG_LIST_ID)
            ListDetail(
                listId = listId,
                onBack = { navController.navigateUp() }
            )
        }

        composable(NavRoute.Recipes.route) {
            Recipes(
                onBack = { navController.navigateUp() },
                onRecipeClick = { recipeId ->
                    navController.navigate(NavRoute.RecipesDetail.createRoute(recipeId))
                }
            )
        }

        composable(
            route = NavRoute.RecipesDetail.route,
            arguments = listOf(
                navArgument(NavRoute.RecipesDetail.ARG_RECIPE_ID) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val recipeId =
                backStackEntry.arguments?.getInt(NavRoute.RecipesDetail.ARG_RECIPE_ID) ?: -1

            RecipesDetail(
                recipeId = recipeId,
                onBack = { navController.navigateUp() },
                onAddToShoppingList = { _ ->
                    navController.navigate(NavRoute.SelectList.createRoute(recipeId))
                }
            )
        }

        composable(
            route = NavRoute.SelectList.route,
            arguments = listOf(
                navArgument(NavRoute.SelectList.ARG_RECIPE_ID) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val recipeId =
                backStackEntry.arguments?.getInt(NavRoute.SelectList.ARG_RECIPE_ID) ?: -1

            SelectList(
                recipeId = recipeId,
                appViewModel = appViewModel,
                onBack = { navController.navigateUp() },
                onCreateList = { navController.navigate(NavRoute.CreateList.route) },
                onListSelected = { listId ->
                    navController.navigate(NavRoute.ListDetail.createRoute(listId)) {
                        popUpTo(NavRoute.SelectList.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(NavRoute.Profile.route) {
            Profile(
                onBack = { navController.navigateUp() },
                onNavigate = { option ->
                    when (option) {
                        ProfileOption.LISTS -> navController.navigate(NavRoute.Home.route)
                        ProfileOption.RECIPES -> navController.navigate(NavRoute.Recipes.route)
                        ProfileOption.SETTINGS -> {}
                        ProfileOption.STATS -> {}
                        ProfileOption.HELP -> {}

                        ProfileOption.LOGIN -> {
                            if (isLoggedIn) {
                                scope.launch { authRepository.logout() }
                            } else {
                                navController.navigate(NavRoute.Login.route)
                            }
                        }
                    }
                },
                username = username,
                isLoggedIn = isLoggedIn,
                onLogout = { scope.launch { authRepository.logout() } }
            )
        }

        composable(NavRoute.Login.route) {
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(
                    authRepository = authRepository,
                    sessionViewModel = sessionViewModel
                )
            )

            Login(
                onBack = { navController.navigateUp() },
                loginViewModel = loginViewModel
            )
        }

        composable(NavRoute.ListViewTypes.route) {
            ListViewTypes(
                selected = ListStyle.LISTA,
                onSelect = { _ -> },
                onCancel = { navController.navigateUp() },
                onSave = { navController.navigateUp() }
            )
        }

        composable(NavRoute.CategoryFilter.route) {
            CategoryFilter(
                onCancel = { navController.navigateUp() },
                onSave = { navController.navigateUp() }
            )
        }
    }
}
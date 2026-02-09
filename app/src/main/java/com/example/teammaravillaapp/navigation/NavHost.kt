package com.example.teammaravillaapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.teammaravillaapp.model.ProfileOption
import com.example.teammaravillaapp.page.camera.CameraScreen
import com.example.teammaravillaapp.page.categoryfilter.CategoryFilter
import com.example.teammaravillaapp.page.createlist.CreateListt
import com.example.teammaravillaapp.page.help.Help
import com.example.teammaravillaapp.page.history.History
import com.example.teammaravillaapp.page.home.Home
import com.example.teammaravillaapp.page.listdetail.ListDetail
import com.example.teammaravillaapp.page.listviewtypes.ListViewTypes
import com.example.teammaravillaapp.page.login.Login
import com.example.teammaravillaapp.page.profile.Profile
import com.example.teammaravillaapp.page.profile.ProfileViewModel
import com.example.teammaravillaapp.page.recipes.Recipes
import com.example.teammaravillaapp.page.recipesdetail.RecipesDetail
import com.example.teammaravillaapp.page.register.Register
import com.example.teammaravillaapp.page.selectlist.SelectList
import com.example.teammaravillaapp.page.session.SessionState
import com.example.teammaravillaapp.page.session.SessionViewModel
import com.example.teammaravillaapp.page.settings.SettingsScreen
import com.example.teammaravillaapp.page.splash.SplashScreen
import com.example.teammaravillaapp.page.stats.Stats
import com.example.teammaravillaapp.ui.debug.ProductsDebugScreen
import com.example.teammaravillaapp.ui.app.MainScaffold

@Composable
fun TeamMaravillaNavHost(
    navController: NavHostController,
    sessionViewModel: SessionViewModel,
    modifier: Modifier = Modifier,
    startDestination: String = NavRoute.Splash.route
) {
    val sessionState by sessionViewModel.sessionState.collectAsState()
    val isLoggedIn = sessionState is SessionState.LoggedIn
    val actions = rememberNavActions(navController)

    SessionNavEffect(
        navController = navController,
        sessionState = sessionState,
        splashDelayMs = 900L
    )

    MainScaffold(navController = navController, modifier = modifier) { innerModifier, onUiEvent ->

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = innerModifier
        ) {
            composable(NavRoute.Splash.route) { SplashScreen() }

            composable(NavRoute.Login.route) {
                Login(
                    onGoRegister = { actions.toRegister() },
                    onLoggedIn = { actions.toHome() },
                    onUiEvent = onUiEvent
                )
            }

            composable(NavRoute.Register.route) {
                Register(
                    onBackToLogin = { actions.up() },
                    onRegistered = { actions.toHome() },
                    onUiEvent = onUiEvent
                )
            }

            composable(NavRoute.Home.route) { backStackEntry ->
                val focusSearch =
                    backStackEntry.savedStateHandle.get<Boolean>("focusSearch") == true

                Home(
                    requestFocusSearch = focusSearch,
                    onFocusSearchConsumed = {
                        backStackEntry.savedStateHandle["focusSearch"] = false
                    },
                    onNavigateCreateList = { actions.toCreateList() },
                    onNavigateRecipes = { actions.toRecipesSingleTop() },
                    onNavigateHistory = { actions.toHistorySingleTop() },
                    onOpenList = { listId -> actions.toListDetail(listId) },
                    onUiEvent = onUiEvent
                )
            }

            composable(NavRoute.CreateList.route) {
                CreateListt(
                    onBack = { actions.up() },
                    onListCreated = { listId ->
                        actions.toListDetailAndRemove(
                            routeToPop = NavRoute.CreateList.route,
                            listId = listId
                        )
                    },
                    onUiEvent = onUiEvent
                )
            }

            composable(
                route = NavRoute.ListDetail.route,
                arguments = listOf(navArgument(NavRoute.ListDetail.ARG_LIST_ID) {
                    type = NavType.StringType
                })
            ) {
                ListDetail(
                    onBack = { actions.up() },
                    onOpenCategoryFilter = { actions.toCategoryFilter() },
                    onOpenListViewTypes = { actions.toListViewTypes() },
                    onUiEvent = onUiEvent
                )
            }

            composable(NavRoute.Recipes.route) {
                Recipes(
                    onBack = { actions.up() },
                    onRecipeClick = { id -> actions.toRecipesDetail(id) }
                )
            }

            composable(
                route = NavRoute.RecipesDetail.route,
                arguments = listOf(navArgument(NavRoute.RecipesDetail.ARG_RECIPE_ID) {
                    type = NavType.IntType
                })
            ) {
                RecipesDetail(
                    onBack = { actions.up() },
                    onAddToShoppingList = { recipeId -> actions.toSelectList(recipeId) },
                    onUiEvent = onUiEvent
                )
            }

            composable(
                route = NavRoute.SelectList.route,
                arguments = listOf(navArgument(NavRoute.SelectList.ARG_RECIPE_ID) {
                    type = NavType.IntType
                })
            ) {
                SelectList(
                    onBack = { actions.up() },
                    onCreateList = { actions.toCreateList() },
                    onListSelected = { listId ->
                        actions.toListDetailAndRemove(
                            routeToPop = NavRoute.SelectList.route,
                            listId = listId
                        )
                    },
                    onUiEvent = onUiEvent
                )
            }

            composable(NavRoute.Profile.route) {
                val vm: ProfileViewModel = hiltViewModel()

                Profile(
                    onBack = { actions.up() },
                    onNavigate = { option ->
                        when (option) {
                            ProfileOption.LISTS -> actions.toHomeSingleTop()
                            ProfileOption.RECIPES -> actions.toRecipesSingleTop()
                            ProfileOption.SETTINGS -> actions.toSettings()
                            ProfileOption.STATS -> actions.toStats()
                            ProfileOption.HELP -> actions.toHelp()
                            ProfileOption.DEBUG_PRODUCTS -> actions.toProductsDebug()
                            ProfileOption.LOGIN -> if (isLoggedIn) vm.logout() else actions.toLogin()
                        }
                    },
                    isLoggedIn = isLoggedIn,
                    onUiEvent = onUiEvent
                )
            }

            composable(NavRoute.ListViewTypes.route) {
                ListViewTypes(
                    onCancel = { actions.up() },
                    onSaved = { actions.up() },
                    onUiEvent = onUiEvent
                )
            }

            composable(NavRoute.CategoryFilter.route) {
                CategoryFilter(
                    onCancel = { actions.up() },
                    onSave = { actions.up() },
                    onUiEvent = onUiEvent
                )
            }

            composable(NavRoute.ProductsDebug.route) {
                ProductsDebugScreen(onBack = { actions.up() })
            }

            composable(
                route = NavRoute.Camera.route,
                arguments = listOf(
                    navArgument(NavRoute.Camera.ARG_LIST_ID) {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val listId = backStackEntry.arguments?.getString(NavRoute.Camera.ARG_LIST_ID)
                CameraScreen(
                    listId = listId,
                    onBack = { actions.up() },
                    onUiEvent = onUiEvent
                )
            }

            composable(NavRoute.Settings.route) { SettingsScreen(onBack = { actions.up() }) }

            composable(NavRoute.Stats.route) {
                Stats(
                    onBack = { actions.up() },
                    onUiEvent = onUiEvent
                )
            }

            composable(NavRoute.Help.route) { Help(onBack = { actions.up() }) }

            composable(NavRoute.History.route) {
                History(
                    onBack = { actions.up() },
                    onOpenList = { listId -> actions.toListDetail(listId) },
                    onUiEvent = onUiEvent
                )
            }
        }
    }
}
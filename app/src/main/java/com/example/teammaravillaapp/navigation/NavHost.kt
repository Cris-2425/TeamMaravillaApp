package com.example.teammaravillaapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.edit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.teammaravillaapp.data.prefs.PrefsKeys.LIST_VIEW_TYPE
import com.example.teammaravillaapp.data.prefs.userPrefsDataStore
import com.example.teammaravillaapp.model.ListViewType
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
import com.example.teammaravillaapp.page.selectlist.SelectList
import com.example.teammaravillaapp.page.session.SessionState
import com.example.teammaravillaapp.page.session.SessionViewModel
import com.example.teammaravillaapp.page.settings.SettingsScreen
import com.example.teammaravillaapp.page.splash.SplashScreen
import com.example.teammaravillaapp.page.stats.Stats
import com.example.teammaravillaapp.ui.app.AppViewModel
import com.example.teammaravillaapp.ui.app.ThemeViewModel
import com.example.teammaravillaapp.ui.debug.ProductsDebugScreen
import com.example.teammaravillaapp.ui.events.UiEvent
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun TeamMaravillaNavHost(
    navController: NavHostController,
    sessionViewModel: SessionViewModel,
    appViewModel: AppViewModel,
    modifier: Modifier = Modifier,
    startDestination: String = NavRoute.Splash.route,
    themeViewModel: ThemeViewModel,
    onUiEvent: (UiEvent) -> Unit
) {
    val sessionState by sessionViewModel.sessionState.collectAsState()
    val isLoggedIn = sessionState is SessionState.LoggedIn


    LaunchedEffect(sessionState) {
        when (sessionState) {
            SessionState.Loading -> Unit
            SessionState.LoggedOut -> {
                if (navController.currentDestination?.route != NavRoute.Login.route) {
                    navController.navigate(NavRoute.Login.route) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            }
            is SessionState.LoggedIn -> {
                if (navController.currentDestination?.route != NavRoute.Home.route) {
                    navController.navigate(NavRoute.Home.route) {
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
                onNavigateCamera = { navController.navigate(NavRoute.Camera.createRoute()) },
                onNavigateRecipes = { navController.navigate(NavRoute.Recipes.route) },
                onNavigateHistory = { navController.navigate(NavRoute.History.route) },
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
                onBack = { navController.navigateUp() },
                onOpenCategoryFilter = { navController.navigate(NavRoute.CategoryFilter.route) },
                onOpenListViewTypes = { navController.navigate(NavRoute.ListViewTypes.route) },
                onUiEvent = onUiEvent
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
            arguments = listOf(navArgument(NavRoute.RecipesDetail.ARG_RECIPE_ID) { type = NavType.IntType })
        ) {
            RecipesDetail(
                onBack = { navController.navigateUp() },
                onAddToShoppingList = { recipeId ->
                    navController.navigate(NavRoute.SelectList.createRoute(recipeId))
                },
                onUiEvent = onUiEvent
            )
        }

        composable(
            route = NavRoute.SelectList.route,
            arguments = listOf(navArgument(NavRoute.SelectList.ARG_RECIPE_ID) { type = NavType.IntType })
        ) {
            SelectList(
                onBack = { navController.navigateUp() },
                onCreateList = { navController.navigate(NavRoute.CreateList.route) },
                onListSelected = { listId ->
                    navController.navigate(NavRoute.ListDetail.createRoute(listId)) {
                        popUpTo(NavRoute.SelectList.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onUiEvent = onUiEvent
            )
        }

        composable(NavRoute.Profile.route) {
            val profileViewModel: ProfileViewModel = hiltViewModel()

            Profile(
                onBack = { navController.navigateUp() },
                onNavigate = { option ->
                    when (option) {
                        ProfileOption.LISTS -> navController.navigate(NavRoute.Home.route)
                        ProfileOption.RECIPES -> navController.navigate(NavRoute.Recipes.route)
                        ProfileOption.SETTINGS -> navController.navigate(NavRoute.Settings.route)
                        ProfileOption.STATS -> navController.navigate(NavRoute.Stats.route)
                        ProfileOption.HELP -> navController.navigate(NavRoute.Help.route)
                        ProfileOption.DEBUG_PRODUCTS -> navController.navigate(NavRoute.ProductsDebug.route)
                        ProfileOption.LOGIN -> {
                            if (isLoggedIn) profileViewModel.logout()
                            else navController.navigate(NavRoute.Login.route)
                        }
                    }
                },
                isLoggedIn = isLoggedIn,
                onUiEvent = onUiEvent
            )
        }

        composable(NavRoute.Login.route) {
            Login(
                onBack = { navController.navigateUp() },
                onUiEvent = onUiEvent
            )
        }

        composable(NavRoute.ListViewTypes.route) {
            val ctx = LocalContext.current
            val scope = rememberCoroutineScope()

            val currentName by ctx.userPrefsDataStore.data
                .map { it[LIST_VIEW_TYPE] ?: ListViewType.BUBBLES.name }
                .collectAsState(initial = ListViewType.BUBBLES.name)

            val current = remember(currentName) {
                runCatching { ListViewType.valueOf(currentName) }.getOrElse { ListViewType.BUBBLES }
            }

            ListViewTypes(
                current = current,
                onCancel = { navController.navigateUp() },
                onSave = { selected ->
                    scope.launch {
                        ctx.userPrefsDataStore.edit { prefs ->
                            prefs[LIST_VIEW_TYPE] = selected.name
                        }
                        navController.navigateUp()
                    }
                }
            )
        }

        composable(NavRoute.CategoryFilter.route) {
            CategoryFilter(
                onCancel = { navController.navigateUp() },
                onSave = { navController.navigateUp() },
                onUiEvent = onUiEvent
            )
        }

        composable(NavRoute.ProductsDebug.route) {
            ProductsDebugScreen(onBack = { navController.navigateUp() })
        }

        composable(
            route = NavRoute.Camera.route,
            arguments = listOf(navArgument(NavRoute.Camera.ARG_LIST_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getString(NavRoute.Camera.ARG_LIST_ID)
            CameraScreen(
                listId = listId, onBack = { navController.navigateUp() },
                onUiEvent = onUiEvent
            )
        }

        composable(NavRoute.Splash.route) {
            LaunchedEffect(sessionState) {
                when (sessionState) {
                    SessionState.Loading -> Unit
                    SessionState.LoggedOut -> navController.navigate(NavRoute.Login.route) {
                        popUpTo(0); launchSingleTop = true
                    }
                    is SessionState.LoggedIn -> navController.navigate(NavRoute.Home.route) {
                        popUpTo(0); launchSingleTop = true
                    }
                }
            }
            SplashScreen(onFinish = { })
        }

        composable(NavRoute.Settings.route) {
            SettingsScreen(
                onBack = { navController.navigateUp() }
            )
        }

        composable(NavRoute.Stats.route) {
            Stats(onBack = { navController.navigateUp() },
                    onUiEvent = onUiEvent
            )
        }

        composable(NavRoute.Help.route) {
            Help(onBack = { navController.navigateUp() })
        }

        composable(NavRoute.History.route) {
            History(
                onBack = { navController.navigateUp() },
                onOpenList = { listId ->
                    navController.navigate(NavRoute.ListDetail.createRoute(listId))
                },
                onUiEvent = onUiEvent
            )
        }
    }
}
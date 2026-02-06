package com.example.teammaravillaapp.ui.app
/*


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.teammaravillaapp.navigation.NavRoute
import com.example.teammaravillaapp.navigation.bottomDestinations
import com.example.teammaravillaapp.ui.components.navigation.AppBottomBar

@Composable
fun MainScaffold(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    content: @Composable (PaddingValues) -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Rutas donde NO quieres bottom bar
    val hideBottomBarRoutes = setOf(
        NavRoute.Login.route,
        NavRoute.Register.route
        // añade Splash si tienes, etc.
    )

    val showBottomBar = currentRoute != null && currentRoute !in hideBottomBarRoutes

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                AppBottomBar(
                    navController = navController,
                    items = bottomDestinations
                )
            }
        }
    ) { padding ->
        content(padding)
    }
}

 */
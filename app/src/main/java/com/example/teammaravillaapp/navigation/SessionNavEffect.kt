package com.example.teammaravillaapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.example.teammaravillaapp.page.session.SessionState
import kotlinx.coroutines.delay

@Composable
fun SessionNavEffect(
    navController: NavHostController,
    sessionState: SessionState,
    splashDelayMs: Long = 900L
) {
    LaunchedEffect(sessionState) {
        val currentRoute = navController.currentDestination?.route
        val authRoutes = setOf(NavRoute.Splash.route, NavRoute.Login.route)

        when (sessionState) {
            SessionState.Loading -> Unit

            SessionState.LoggedOut -> {
                if (currentRoute == NavRoute.Splash.route) delay(splashDelayMs)

                if (currentRoute != NavRoute.Login.route) {
                    navController.navigate(NavRoute.Login.route) {
                        popUpTo(NavRoute.Splash.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            is SessionState.LoggedIn -> {
                if (currentRoute == NavRoute.Splash.route) delay(splashDelayMs)

                // ✅ Solo “empuja” a Home si vienes de Splash/Login
                if (currentRoute in authRoutes) {
                    navController.navigate(NavRoute.Home.route) {
                        popUpTo(NavRoute.Splash.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }
}
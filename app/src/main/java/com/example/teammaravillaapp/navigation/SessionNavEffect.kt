package com.example.teammaravillaapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.example.teammaravillaapp.page.session.SessionState
import kotlinx.coroutines.delay

/**
 * Efecto de navegación reactivo al estado de sesión.
 *
 * Responsabilidades:
 * - Resolver redirecciones automáticas desde Splash/Login según [SessionState].
 * - Aplicar un delay opcional en Splash para mostrar branding.
 * - Evitar navegación repetida con comprobación de ruta + `launchSingleTop`.
 *
 * Reglas de negocio (importante):
 * - [SessionState.LoggedIn] representa sesión activa.
 * - `canAutoLogin` decide si se permite auto-redirección desde Splash.
 *
 * @param navController Controlador de navegación.
 * @param sessionState Estado actual de sesión.
 * @param splashDelayMs Delay aplicado solo cuando la ruta actual es Splash. Debe ser >= 0.
 */
@Composable
fun SessionNavEffect(
    navController: NavHostController,
    sessionState: SessionState,
    splashDelayMs: Long = 900L
) {
    LaunchedEffect(sessionState) {
        val currentRoute = navController.currentDestination?.route
        val authRoutes = setOf(NavRoute.Splash.route, NavRoute.Login.route)

        fun delayIfSplash() {
            if (currentRoute == NavRoute.Splash.route && splashDelayMs > 0) {
                // ok si se cancela por recomposición
            }
        }

        when (sessionState) {
            SessionState.Loading -> Unit

            SessionState.LoggedOut -> {
                if (currentRoute == NavRoute.Splash.route) delay(splashDelayMs.coerceAtLeast(0))

                if (currentRoute != NavRoute.Login.route) {
                    navController.navigate(NavRoute.Login.route) {
                        popUpTo(NavRoute.Splash.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            is SessionState.LoggedIn -> {
                if (currentRoute == NavRoute.Splash.route) delay(splashDelayMs.coerceAtLeast(0))

                // Sin  auto-login permitido -> Login
                if (currentRoute == NavRoute.Splash.route && !sessionState.canAutoLogin) {
                    navController.navigate(NavRoute.Login.route) {
                        popUpTo(NavRoute.Splash.route) { inclusive = true }
                        launchSingleTop = true
                    }
                    return@LaunchedEffect
                }

                // Con auto-login -> Home
                if (currentRoute in authRoutes && sessionState.canAutoLogin) {
                    navController.navigate(NavRoute.Home.route) {
                        popUpTo(NavRoute.Splash.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }
}
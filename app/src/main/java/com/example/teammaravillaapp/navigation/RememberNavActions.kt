package com.example.teammaravillaapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController

/**
 * Crea y memoiza un wrapper de acciones de navegación para evitar recreaciones
 * en recomposiciones.
 *
 * @param navController Controlador de navegación.
 * @return Instancia estable de [NavActions].
 */
@Composable
fun rememberNavActions(navController: NavHostController): NavActions =
    remember(navController) { NavActions(navController) }
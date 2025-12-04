package com.example.teammaravillaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.teammaravillaapp.navigation.TeamMaravillaNavHost
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * # MainActivity
 *
 * Punto de entrada principal de la aplicación **Team Maravilla**.
 *
 * Configura el tema global y establece el sistema de navegación basado en
 * **Navigation Compose**, permitiendo que las pantallas se gestionen mediante
 * un `NavHost` centralizado definido en [TeamMaravillaNavHost].
 *
 * ---
 *
 * ## Responsabilidades:
 * - Crear el `NavController`.
 * - Iniciar el `NavHost` responsable de todas las pantallas.
 * - Aplicar el tema `TeamMaravillaAppTheme`.
 *
 * ---
 *
 * ## Flujo general:
 * ```
 * MainActivity
 *     └── rememberNavController()
 *             └── TeamMaravillaNavHost()
 *                     ├── Home
 *                     ├── CreateList
 *                     ├── ListDetail
 *                     ├── Recipes
 *                     ├── RecipesDetail
 *                     ├── Profile
 *                     ├── Login
 *                     ├── CategoryFilter
 *                     └── ListViewTypes
 * ```
 *
 * ---
 *
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeamMaravillaAppTheme {
                val navController = rememberNavController()
                TeamMaravillaNavHost(navController = navController)
            }
        }
    }
}
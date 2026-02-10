package com.example.teammaravillaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.teammaravillaapp.model.ThemeMode
import com.example.teammaravillaapp.navigation.TeamMaravillaNavHost
import com.example.teammaravillaapp.page.session.SessionViewModel
import com.example.teammaravillaapp.ui.app.ThemeViewModel
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Actividad principal (entry point) de TeamMaravillaApp.
 *
 * Responsabilidades:
 * - Instalar el SplashScreen del sistema.
 * - Resolver el modo de tema (System/Light/Dark) a partir de [ThemeViewModel].
 * - Montar el árbol Compose principal: Theme -> NavController -> [TeamMaravillaNavHost].
 *
 * Decisiones de arquitectura:
 * - El tema se controla en Activity (nivel superior) para aplicarse a toda la app.
 * - El flujo de navegación y la lógica de sesión se delegan al NavHost + SessionViewModel.
 * - No se declara Scaffold aquí; el scaffold global vive dentro de [TeamMaravillaNavHost]/MainScaffold.
 *
 * @see TeamMaravillaNavHost Grafo de navegación principal.
 * @see ThemeViewModel Preferencias de tema.
 * @see com.example.teammaravillaapp.page.session.SessionViewModel Estado global de sesión.
 *
 * Ejemplo de uso:
 * {@code
 * // Se ejecuta automáticamente por el sistema Android como launcher activity.
 * }
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            val themeMode by themeViewModel.themeMode.collectAsStateWithLifecycle()

            val darkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
            }

            val navController = rememberNavController()

            val sessionViewModel: SessionViewModel = hiltViewModel()

            TeamMaravillaAppTheme(darkTheme = darkTheme) {
                TeamMaravillaNavHost(
                    navController = navController,
                    sessionViewModel = sessionViewModel
                )
            }
        }
    }
}
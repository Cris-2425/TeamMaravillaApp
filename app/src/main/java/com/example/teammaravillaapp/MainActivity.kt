package com.example.teammaravillaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.teammaravillaapp.model.ThemeMode
import com.example.teammaravillaapp.navigation.TeamMaravillaNavHost
import com.example.teammaravillaapp.page.session.SessionViewModel
import com.example.teammaravillaapp.ui.app.ThemeViewModel
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            val themeMode by themeViewModel.themeMode.collectAsState()

            val darkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
            }

            TeamMaravillaAppTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()
                val sessionViewModel: SessionViewModel = hiltViewModel()

                // ❌ NO Scaffold aquí
                TeamMaravillaNavHost(
                    navController = navController,
                    sessionViewModel = sessionViewModel
                )
            }
        }
    }
}
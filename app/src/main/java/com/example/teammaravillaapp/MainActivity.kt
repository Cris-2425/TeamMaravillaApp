package com.example.teammaravillaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.teammaravillaapp.model.ThemeMode
import com.example.teammaravillaapp.navigation.TeamMaravillaNavHost
import com.example.teammaravillaapp.page.session.SessionViewModel
import com.example.teammaravillaapp.ui.app.AppViewModel
import com.example.teammaravillaapp.ui.app.ThemeViewModel
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // ViewModel de tema a nivel Activity (persistente entre pantallas)
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

            TeamMaravillaAppTheme(
                darkTheme = darkTheme
                // si tu Theme soporta dynamicColor:
                // , dynamicColor = true
            ) {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                val sessionViewModel: SessionViewModel = hiltViewModel()
                val appViewModel: AppViewModel = hiltViewModel()

                LaunchedEffect(Unit) {
                    appViewModel.events.collect { event ->
                        when (event) {
                            is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                        }
                    }
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    TeamMaravillaNavHost(
                        navController = navController,
                        sessionViewModel = sessionViewModel,
                        appViewModel = appViewModel,
                        modifier = Modifier.padding(innerPadding),

                        themeViewModel = themeViewModel
                    )
                }
            }
        }
    }
}
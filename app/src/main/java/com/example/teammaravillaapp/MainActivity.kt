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
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

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
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()

                val sessionViewModel: SessionViewModel = hiltViewModel()
                val appViewModel: AppViewModel = hiltViewModel()

                suspend fun handleUiEvent(event: UiEvent) {
                    when (event) {
                        is UiEvent.ShowSnackbar -> {
                            val message = getString(event.messageResId, *event.formatArgs)
                            snackbarHostState.showSnackbar(message)
                        }
                    }
                }

                // ✅ Eventos globales de AppViewModel
                LaunchedEffect(appViewModel) {
                    appViewModel.events.collect { event ->
                        handleUiEvent(event)
                    }
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    TeamMaravillaNavHost(
                        navController = navController,
                        sessionViewModel = sessionViewModel,
                        modifier = Modifier.padding(innerPadding),
                        onUiEvent = { event ->
                            scope.launch { handleUiEvent(event) }
                        }
                    )
                }
            }
        }
    }
}
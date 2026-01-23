package com.example.teammaravillaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.teammaravillaapp.navigation.TeamMaravillaNavHost
import com.example.teammaravillaapp.page.session.SessionViewModel
import com.example.teammaravillaapp.ui.app.AppViewModel
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TeamMaravillaAppTheme {

                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                // ✅ SessionViewModel por Hilt
                val sessionViewModel: SessionViewModel = hiltViewModel()

                // AppViewModel: si NO es Hilt todavía, puedes dejar viewModel()
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
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
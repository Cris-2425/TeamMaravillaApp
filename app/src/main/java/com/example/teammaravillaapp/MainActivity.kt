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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.teammaravillaapp.data.auth.FakeAuthRepository
import com.example.teammaravillaapp.data.session.SessionStore
import com.example.teammaravillaapp.navigation.TeamMaravillaNavHost
import com.example.teammaravillaapp.page.session.SessionViewModel
import com.example.teammaravillaapp.page.session.SessionViewModelFactory
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.ui.app.AppViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔹 Infraestructura base
        val sessionStore = SessionStore(applicationContext)
        val authRepository = FakeAuthRepository(sessionStore)

        // 🔹 ViewModels con Factory (SIN Hilt)
        val sessionViewModel = ViewModelProvider(
            this,
            SessionViewModelFactory(sessionStore)
        )[SessionViewModel::class.java]

        val appViewModel = ViewModelProvider(this)[AppViewModel::class.java]

        setContent {
            TeamMaravillaAppTheme {

                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                // 🔹 Escucha de eventos globales (Snackbar)
                LaunchedEffect(Unit) {
                    appViewModel.events.collect { event ->
                        when (event) {
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                        }
                    }
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->

                    TeamMaravillaNavHost(
                        navController = navController,
                        sessionViewModel = sessionViewModel,
                        authRepository = authRepository,
                        appViewModel = appViewModel,
                        modifier = androidx.compose.ui.Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
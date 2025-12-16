package com.example.teammaravillaapp

import FakeAuthRepository
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.teammaravillaapp.data.session.SessionStore
import com.example.teammaravillaapp.navigation.NavRoute
import com.example.teammaravillaapp.navigation.TeamMaravillaNavHost
import com.example.teammaravillaapp.page.session.SessionViewModel
import com.example.teammaravillaapp.page.session.SessionViewModelFactory
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

class MainActivity : ComponentActivity() {

    private val sessionStore by lazy { SessionStore(applicationContext) }

    private val sessionViewModel: SessionViewModel by viewModels {
        SessionViewModelFactory(sessionStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TeamMaravillaAppTheme {
                val navController = rememberNavController()
                val authRepository = remember { FakeAuthRepository(sessionStore) }

                val isLoggedIn by sessionViewModel.isLoggedIn.collectAsState()

                val startDestination = if (isLoggedIn) {
                    NavRoute.Home.route
                } else {
                    NavRoute.Login.route
                }

                TeamMaravillaNavHost(
                    navController = navController,
                    sessionViewModel = sessionViewModel,
                    authRepository = authRepository,
                    startDestination = startDestination
                )
            }
        }
    }
}
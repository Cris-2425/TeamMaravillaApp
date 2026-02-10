package com.example.teammaravillaapp.page.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Pantalla contenedora de Login.
 *
 * Responsabilidades:
 * - Resolver el [LoginViewModel] mediante Hilt.
 * - Recolectar [LoginViewModel.uiState] como fuente de verdad.
 * - Escuchar [LoginViewModel.events] (one-shot) y reenviarlos a [onUiEvent].
 * - Delegar la UI pura a [LoginContent] para facilitar testeo y @Preview.
 *
 * @param onGoRegister Navegación hacia la pantalla de registro.
 * Restricciones:
 * - No nulo.
 * - Debe ejecutarse rápido (UI thread).
 * @param onLoggedIn Navegación/callback que se ejecuta cuando el login finaliza con éxito.
 * Restricciones:
 * - No nulo.
 * - Debe encargarse de cambiar de pantalla (ej. ir a Home).
 * @param onUiEvent Consumidor de eventos de UI (snackbars, etc.).
 * Restricciones:
 * - No nulo.
 * @param vm ViewModel inyectado por Hilt. Se permite override para tests.
 *
 * @see LoginContent Render de presentación.
 * @see LoginViewModel Lógica de login y validaciones.
 *
 * Ejemplo de uso:
 * {@code
 * LoginScreen(
 *   onGoRegister = { navController.navigate("register") },
 *   onLoggedIn = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
 *   onUiEvent = { event -> handleUiEvent(event) }
 * )
 * }
 */
@Composable
fun Login(
    onGoRegister: () -> Unit,
    onLoggedIn: () -> Unit,
    onUiEvent: (UiEvent) -> Unit,
    vm: LoginViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(vm) { vm.events.collect(onUiEvent) }

    LoginContent(
        uiState = uiState,
        onUsernameChange = vm::onEmailChange,
        onPasswordChange = vm::onPasswordChange,
        onRememberMeChange = vm::onRememberMeChange,
        onLogin = { vm.onLoginClick(onLoggedIn) },
        onGoRegister = onGoRegister
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewLogin_Empty() {
    TeamMaravillaAppTheme {
        LoginContent(
            uiState = LoginUiState(),
            onUsernameChange = {},
            onPasswordChange = {},
            onRememberMeChange = {},
            onLogin = {},
            onGoRegister = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLogin_Loading() {
    TeamMaravillaAppTheme {
        LoginContent(
            uiState = LoginUiState(username = "cris", password = "1234", isLoading = true),
            onUsernameChange = {},
            onPasswordChange = {},
            onRememberMeChange = {},
            onLogin = {},
            onGoRegister = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLogin_ReadyEnabled() {
    TeamMaravillaAppTheme {
        LoginContent(
            uiState = LoginUiState(username = "cris", password = "1234", rememberMe = true, isLoading = false),
            onUsernameChange = {},
            onPasswordChange = {},
            onRememberMeChange = {},
            onLogin = {},
            onGoRegister = {}
        )
    }
}
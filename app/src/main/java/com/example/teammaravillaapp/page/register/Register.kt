package com.example.teammaravillaapp.page.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Pantalla contenedora de Registro.
 *
 * Responsabilidades:
 * - Resolver el [RegisterViewModel] mediante Hilt.
 * - Recolectar [RegisterViewModel.uiState] como fuente de verdad.
 * - Escuchar [RegisterViewModel.events] (one-shot) y reenviarlos a [onUiEvent].
 * - Delegar la UI pura a [RegisterContent] para facilitar testeo y @Preview.
 *
 * @param onBackToLogin Navegación hacia la pantalla de login.
 * Restricciones:
 * - No nulo.
 * - Debe ejecutarse rápido (UI thread).
 * @param onRegistered Callback a ejecutar cuando el registro finaliza con éxito.
 * Restricciones:
 * - No nulo.
 * - Debe encargarse de la navegación posterior (ej. volver a login o entrar a home).
 * @param onUiEvent Consumidor de eventos de UI (snackbars, etc.).
 * Restricciones:
 * - No nulo.
 * @param vm ViewModel inyectado por Hilt. Se permite override para tests.
 *
 * @see RegisterContent Render de presentación.
 * @see RegisterViewModel Lógica de validación y registro.
 *
 * Ejemplo de uso:
 * {@code
 * RegisterScreen(
 *   onBackToLogin = { navController.popBackStack() },
 *   onRegistered = { navController.navigate("home") },
 *   onUiEvent = { event -> handleUiEvent(event) }
 * )
 * }
 */
@Composable
fun Register(
    onBackToLogin: () -> Unit,
    onRegistered: () -> Unit,
    onUiEvent: (UiEvent) -> Unit,
    vm: RegisterViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(vm) { vm.events.collect(onUiEvent) }

    RegisterContent(
        uiState = uiState,
        onUsernameChange = vm::onUsernameChange,
        onEmailChange = vm::onEmailChange,
        onPasswordChange = vm::onPasswordChange,
        onRememberMeChange = vm::onRememberMeChange,
        onRegister = { vm.onRegisterClick(onRegistered) },
        onBackToLogin = onBackToLogin
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewRegister_Empty() {
    TeamMaravillaAppTheme {
        RegisterContent(
            uiState = RegisterUiState(),
            onUsernameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onRememberMeChange = {},
            onRegister = {},
            onBackToLogin = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRegister_Loading() {
    TeamMaravillaAppTheme {
        RegisterContent(
            uiState = RegisterUiState(
                username = "cris",
                email = "cris@correo.com",
                password = "1234",
                isLoading = true
            ),
            onUsernameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onRememberMeChange = {},
            onRegister = {},
            onBackToLogin = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRegister_ReadyEnabled() {
    TeamMaravillaAppTheme {
        RegisterContent(
            uiState = RegisterUiState(
                username = "cris",
                email = "cris@correo.com",
                password = "1234",
                rememberMe = true,
                isLoading = false
            ),
            onUsernameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onRememberMeChange = {},
            onRegister = {},
            onBackToLogin = {}
        )
    }
}
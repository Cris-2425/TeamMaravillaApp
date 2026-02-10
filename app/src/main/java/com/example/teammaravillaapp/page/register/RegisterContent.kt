package com.example.teammaravillaapp.page.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.GeneralBackground

/**
 * UI pura de la pantalla de Registro.
 *
 * Esta función NO:
 * - conoce ViewModels
 * - recolecta Flows
 * - lanza side-effects
 *
 * Solo renderiza [RegisterUiState] y expone callbacks para acciones de usuario.
 *
 * @param uiState Estado actual del formulario de registro.
 * Restricciones:
 * - No nulo.
 * - Si [RegisterUiState.isLoading] es true, se deben deshabilitar inputs y acciones.
 * @param onUsernameChange Callback cuando cambia el nombre de usuario.
 * Restricciones:
 * - Puede ser vacío mientras se escribe.
 * @param onEmailChange Callback cuando cambia el email.
 * Restricciones:
 * - Puede ser vacío mientras se escribe.
 * - La validación real (formato) puede añadirse en ViewModel si lo exige la rúbrica.
 * @param onPasswordChange Callback cuando cambia la contraseña.
 * Restricciones:
 * - Puede ser vacío mientras se escribe.
 * @param onRememberMeChange Callback para alternar “recordarme”.
 * @param onRegister Acción de registro.
 * Restricciones:
 * - Se recomienda que el caller respete [RegisterUiState.isRegisterEnabled] (la UI ya lo hace).
 * @param onBackToLogin Acción para volver a Login.
 *
 * @throws IllegalStateException No se lanza aquí directamente, pero [onRegister] no debería ejecutar
 * operaciones si [uiState.isLoading] es true (la UI deshabilita el botón).
 *
 * @see RegisterUiState Estado del formulario.
 *
 * Ejemplo:
 * {@code
 * RegisterContent(
 *   uiState = RegisterUiState(username = "cris", email = "a@b.com", password = "1234"),
 *   onUsernameChange = {},
 *   onEmailChange = {},
 *   onPasswordChange = {},
 *   onRememberMeChange = {},
 *   onRegister = { /* submit */ },
 *   onBackToLogin = { /* nav */ }
 * )
 * }
 */
@Composable
fun RegisterContent(
    uiState: RegisterUiState,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onRegister: () -> Unit,
    onBackToLogin: () -> Unit
) {
    GeneralBackground(overlayAlpha = 0.20f) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.register_title),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.register_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(18.dp))

            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.username,
                        onValueChange = onUsernameChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        label = { Text(stringResource(R.string.register_username)) }
                    )

                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = onEmailChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        label = { Text(stringResource(R.string.register_email)) }
                    )

                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = onPasswordChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        visualTransformation = PasswordVisualTransformation(),
                        label = { Text(stringResource(R.string.register_password)) }
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = uiState.rememberMe,
                            onCheckedChange = onRememberMeChange,
                            enabled = !uiState.isLoading
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(text = stringResource(R.string.login_remember_me))
                    }

                    Button(
                        onClick = onRegister,
                        enabled = uiState.isRegisterEnabled,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(stringResource(R.string.common_loading))
                        } else {
                            Text(stringResource(R.string.register_button))
                        }
                    }

                    TextButton(
                        onClick = onBackToLogin,
                        enabled = !uiState.isLoading
                    ) {
                        Text(stringResource(R.string.register_back_to_login))
                    }
                }
            }
        }
    }
}
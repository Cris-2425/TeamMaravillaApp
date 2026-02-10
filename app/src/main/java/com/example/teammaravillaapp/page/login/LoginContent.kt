package com.example.teammaravillaapp.page.login

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
 * UI pura de la pantalla de Login.
 *
 * Esta función NO:
 * - conoce ViewModels
 * - recolecta Flows
 * - lanza side-effects
 *
 * Solo renderiza [LoginUiState] y expone callbacks para acciones de usuario.
 *
 * @param uiState Estado actual del formulario de login.
 * Restricciones:
 * - No nulo.
 * - Si [LoginUiState.isLoading] es true, se deben deshabilitar inputs y acciones.
 * @param onUsernameChange Callback cuando cambia el usuario/email.
 * Restricciones:
 * - El valor puede ser vacío (la validación final se hace al intentar login).
 * @param onPasswordChange Callback cuando cambia la contraseña.
 * Restricciones:
 * - El valor puede ser vacío.
 * @param onRememberMeChange Callback para alternar “recordarme”.
 * @param onLogin Acción de login.
 * Restricciones:
 * - Se recomienda que el caller respete [LoginUiState.isLoginButtonEnabled] (la UI ya lo hace).
 * @param onGoRegister Acción para navegar a registro.
 *
 * @throws IllegalStateException No se lanza aquí directamente, pero [onLogin] no debería ejecutar
 * llamadas de red si [uiState.isLoading] es true (la UI deshabilita el botón).
 *
 * @see LoginUiState Estado del formulario.
 *
 * Ejemplo:
 * {@code
 * LoginContent(
 *   uiState = LoginUiState(username = "cris", password = "1234"),
 *   onUsernameChange = {},
 *   onPasswordChange = {},
 *   onRememberMeChange = {},
 *   onLogin = { /* submit */ },
 *   onGoRegister = { /* nav */ }
 * )
 * }
 */
@Composable
fun LoginContent(
    uiState: LoginUiState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onLogin: () -> Unit,
    onGoRegister: () -> Unit
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
                text = stringResource(R.string.login_title),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.login_subtitle),
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
                        label = { Text(stringResource(R.string.login_username_placeholder)) }
                    )

                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = onPasswordChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        visualTransformation = PasswordVisualTransformation(),
                        label = { Text(stringResource(R.string.login_password_placeholder)) }
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
                        onClick = onLogin,
                        enabled = uiState.isLoginButtonEnabled,
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
                            Text(stringResource(R.string.login_button_text))
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.login_no_account),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.width(6.dp))
                        TextButton(
                            onClick = onGoRegister,
                            enabled = !uiState.isLoading
                        ) {
                            Text(stringResource(R.string.login_go_register))
                        }
                    }
                }
            }
        }
    }
}
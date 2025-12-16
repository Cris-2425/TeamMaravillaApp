package com.example.teammaravillaapp.page.login

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

@Composable
fun Login(
    onBack: () -> Unit,
    loginViewModel: LoginViewModel
) {
    val uiState by loginViewModel.uiState.collectAsState()

    Box(Modifier.fillMaxSize()) {

        GeneralBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(28.dp))

            Title(
                texto = stringResource(R.string.login_title)
            )

            Spacer(Modifier.height(160.dp))

            Surface(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                tonalElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = { loginViewModel.onEmailChange(it) },
                        placeholder = { Text(stringResource(R.string.login_username_placeholder)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)),
                        singleLine = true,
                        enabled = !uiState.isLoading
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = { loginViewModel.onPasswordChange(it) },
                        placeholder = { Text(stringResource(R.string.login_password_placeholder)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        enabled = !uiState.isLoading
                    )

                    if (uiState.errorMessage != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = uiState.errorMessage!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            Log.d(TAG_GLOBAL, "Login → click")
                            loginViewModel.onLoginClick()
                        },
                        enabled = uiState.isLoginButtonEnabled,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                modifier = Modifier.height(18.dp)
                            )
                            Spacer(Modifier.height(0.dp))
                        } else {
                            Text(stringResource(R.string.login_button_text))
                        }
                    }

                    if (uiState.isLoading) {
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Cargando...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Box(Modifier.align(Alignment.BottomStart)) {
            BackButton(
                onClick = {
                    if (!uiState.isLoading) {
                        Log.d(TAG_GLOBAL, "Login → BackButton")
                        onBack()
                    } else {
                        Log.d(TAG_GLOBAL, "Login → BackButton bloqueado (loading)")
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewLogin() {
    TeamMaravillaAppTheme {
        // Preview
    }
}
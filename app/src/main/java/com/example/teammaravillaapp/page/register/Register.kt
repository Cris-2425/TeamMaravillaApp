package com.example.teammaravillaapp.page.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import com.example.teammaravillaapp.R
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.ui.events.UiEvent

@Composable
fun Register(
    onBackToLogin: () -> Unit,
    onRegistered: () -> Unit,
    onUiEvent: (UiEvent) -> Unit,
    vm: RegisterViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(vm) { vm.events.collect(onUiEvent) }

    GeneralBackground(overlayAlpha = 0.20f) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = stringResource(R.string.register_title), style = MaterialTheme.typography.headlineSmall)
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
                        onValueChange = vm::onUsernameChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        label = { Text(stringResource(R.string.register_username)) }
                    )

                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = vm::onEmailChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        label = { Text(stringResource(R.string.register_email)) }
                    )

                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = vm::onPasswordChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        visualTransformation = PasswordVisualTransformation(),
                        label = { Text(stringResource(R.string.register_password)) }
                    )

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(
                            checked = uiState.rememberMe,
                            onCheckedChange = vm::onRememberMeChange,
                            enabled = !uiState.isLoading
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(text = stringResource(R.string.login_remember_me))
                    }

                    Button(
                        onClick = { vm.onRegisterClick(onRegistered) },
                        enabled = uiState.isRegisterEnabled,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(10.dp))
                            Text(stringResource(R.string.common_loading))
                        } else {
                            Text(stringResource(R.string.register_button))
                        }
                    }

                    TextButton(onClick = onBackToLogin, enabled = !uiState.isLoading) {
                        Text(stringResource(R.string.register_back_to_login))
                    }
                }
            }
        }
    }
}
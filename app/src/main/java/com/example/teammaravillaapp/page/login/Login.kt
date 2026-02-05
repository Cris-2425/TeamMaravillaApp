package com.example.teammaravillaapp.page.login

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.util.TAG_GLOBAL

@Composable
fun Login(
    onBack: () -> Unit,
    onUiEvent: (UiEvent) -> Unit,
    vm: LoginViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(vm) {
        vm.events.collect { onUiEvent(it) }
    }

    Box(Modifier.fillMaxSize()) {
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
                            value = uiState.email,
                            onValueChange = vm::onEmailChange,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            enabled = !uiState.isLoading,
                            placeholder = { Text(stringResource(R.string.login_username_placeholder)) }
                        )

                        OutlinedTextField(
                            value = uiState.password,
                            onValueChange = vm::onPasswordChange,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            enabled = !uiState.isLoading,
                            visualTransformation = PasswordVisualTransformation(),
                            placeholder = { Text(stringResource(R.string.login_password_placeholder)) },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboard?.hide()
                                    if (uiState.isLoginButtonEnabled) {
                                        vm.onLoginClick()
                                    } else {
                                        vm.onLoginClick() // opcional: si quieres que lance snackbar "campos obligatorios"
                                        // o si prefieres NO llamar:
                                        // onUiEvent(UiEvent.ShowSnackbar(R.string.login_error_required_fields))
                                    }
                                }
                            )
                        )

                        Button(
                            onClick = {
                                Log.d(TAG_GLOBAL, "Login → click")
                                vm.onLoginClick()
                            },
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
                    }
                }
            }

            //Box(Modifier.align(Alignment.BottomStart)) {
            //    BackButton(onClick = { if (!uiState.isLoading) onBack() })
            //}
        }
    }
}
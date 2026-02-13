package com.example.teammaravillaapp.page.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.OptionsGrid
import com.example.teammaravillaapp.component.ProfileImage
import com.example.teammaravillaapp.model.ProfileOption

/**
 * UI pura de la pantalla de Perfil.
 *
 * Esta función NO:
 * - usa ActivityResult launchers
 * - depende de UCrop
 * - habla con ViewModels
 *
 * Renderiza datos ya resueltos y expone callbacks para que el contenedor decida qué hacer.
 *
 * @param username Nombre visible del usuario. Si es null, se muestra un placeholder.
 * @param isLoggedIn Si es true, se muestra el botón “Cerrar sesión”.
 * @param photoUriString URI en formato String de la imagen de perfil. Si es null/blank, se muestra placeholder.
 * @param showPhotoMenu Controla si el menú contextual de la foto está abierto.
 * @param onShowPhotoMenuChange Callback para abrir/cerrar el menú de foto.
 * @param onPickNewPhoto Acción para seleccionar una nueva foto (el contenedor abrirá el picker).
 * @param onRemovePhoto Acción para eliminar la foto guardada (el contenedor/VM limpiará prefs).
 * @param onLogout Acción para cerrar sesión.
 * @param onNavigate Acción al pulsar una opción del grid.
 * @param onBack Acción para volver atrás (si decides añadir BackButton).
 *
 * @throws IllegalArgumentException No se lanza directamente, pero [onNavigate] debería validar índices/estado
 * si se implementa con mapping externo.
 *
 * @see ProfileOption Opciones disponibles en el perfil.
 *
 * Ejemplo de uso:
 * {@code
 * ProfileContent(
 *   username = "Cristian",
 *   isLoggedIn = true,
 *   photoUriString = null,
 *   showPhotoMenu = false,
 *   onShowPhotoMenuChange = {},
 *   onPickNewPhoto = {},
 *   onRemovePhoto = {},
 *   onLogout = {},
 *   onNavigate = {},
 *   onBack = {}
 * )
 * }
 */
@Composable
fun ProfileContent(
    username: String?,
    isLoggedIn: Boolean,
    photoUriString: String?,
    showPhotoMenu: Boolean,
    onShowPhotoMenuChange: (Boolean) -> Unit,
    onPickNewPhoto: () -> Unit,
    onRemovePhoto: () -> Unit,
    onLogout: () -> Unit,
    onNavigate: (ProfileOption) -> Unit,
    onBack: () -> Unit
) {
    val options = ProfileOption.entries.toTypedArray()

    Box(Modifier.fillMaxSize()) {
        GeneralBackground(overlayAlpha = 0.20f) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(10.dp))

                Box {
                    ProfileImage(
                        imageRes = null,
                        uriString = photoUriString,
                        modifier = Modifier.clickable { onShowPhotoMenuChange(true) }
                    )

                    DropdownMenu(
                        expanded = showPhotoMenu,
                        onDismissRequest = { onShowPhotoMenuChange(false) }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.profile_change_photo)) },
                            onClick = {
                                onShowPhotoMenuChange(false)
                                onPickNewPhoto()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.profile_remove_photo)) },
                            enabled = !photoUriString.isNullOrBlank(),
                            onClick = {
                                onShowPhotoMenuChange(false)
                                onRemovePhoto()
                            }
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                Surface(
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = 2.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = username ?: stringResource(R.string.profile_username_placeholder),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = if (isLoggedIn)
                                stringResource(R.string.profile_status_logged_in)
                            else
                                stringResource(R.string.profile_status_logged_out),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OptionsGrid(
                            options = options.map { stringResource(it.labelRes) },
                            onOptionClick = { index ->
                                options.getOrNull(index)?.let(onNavigate)
                            }
                        )

                        if (isLoggedIn) {
                            OutlinedButton(
                                onClick = onLogout,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                ),
                                border = ButtonDefaults.outlinedButtonBorder
                            ) {
                                Text(stringResource(R.string.profile_logout))
                            }
                        }
                    }
                }

                Spacer(Modifier.height(90.dp))
            }
        }
    }
}
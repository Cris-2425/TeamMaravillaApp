package com.example.teammaravillaapp.page.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.ThemeModeRow
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.model.ThemeMode

/**
 * UI pura de la pantalla de Ajustes.
 *
 * Esta función NO:
 * - depende de Hilt
 * - lee DataStore directamente
 * - conoce ViewModels
 *
 * Renderiza el estado ya resuelto y expone callbacks para que el contenedor decida la acción.
 *
 * @param mode Modo de tema actualmente seleccionado.
 * Restricciones:
 * - No nulo.
 * @param onBack Acción de navegación hacia atrás (opcional si añades BackButton).
 * Restricciones:
 * - No nulo.
 * @param onSelectMode Acción cuando el usuario selecciona un modo de tema.
 * Restricciones:
 * - No nulo.
 * - Debe aceptar cualquier valor de [ThemeMode].
 *
 * @throws IllegalArgumentException No se lanza directamente, pero si tu implementación de [onSelectMode]
 * valida estados o restringe opciones podría lanzarse en esa capa.
 *
 * @see ThemeMode
 * @see ThemeModeRow
 *
 * Ejemplo de uso:
 * {@code
 * SettingsContent(
 *   mode = ThemeMode.SYSTEM,
 *   onBack = {},
 *   onSelectMode = { selected -> viewModel.setThemeMode(selected) }
 * )
 * }
 */
@Composable
fun SettingsContent(
    mode: ThemeMode,
    onBack: () -> Unit,
    onSelectMode: (ThemeMode) -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        GeneralBackground(overlayAlpha = 0.20f) {

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    tonalElevation = 2.dp
                ) {
                    Spacer(Modifier.height(12.dp))

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.settings_theme_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = stringResource(R.string.settings_theme_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(6.dp))

                        ThemeModeRow(
                            title = stringResource(R.string.settings_theme_system),
                            selected = mode == ThemeMode.SYSTEM,
                            onClick = { onSelectMode(ThemeMode.SYSTEM) }
                        )
                        ThemeModeRow(
                            title = stringResource(R.string.settings_theme_light),
                            selected = mode == ThemeMode.LIGHT,
                            onClick = { onSelectMode(ThemeMode.LIGHT) }
                        )
                        ThemeModeRow(
                            title = stringResource(R.string.settings_theme_dark),
                            selected = mode == ThemeMode.DARK,
                            onClick = { onSelectMode(ThemeMode.DARK) }
                        )
                    }
                }
            }
            // Box(Modifier.align(Alignment.BottomStart)) { BackButton(onClick = onBack) }
        }
    }
}
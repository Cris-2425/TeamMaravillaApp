package com.example.teammaravillaapp.page.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.model.ThemeMode
import com.example.teammaravillaapp.ui.app.ThemeViewModel
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Pantalla contenedora de Ajustes.
 *
 * Responsabilidades:
 * - Recolectar el estado de tema desde [ThemeViewModel].
 * - Traducir interacciones de UI (selección de tema) a acciones del ViewModel.
 * - Delegar el render a [SettingsContent] (presentación pura) para mejorar testeo y previews.
 *
 * Motivo:
 * - Separar responsabilidades: UI pura vs capa que conoce ViewModels/Hilt.
 * - Facilitar previews sin depender de Hilt/DataStore.
 *
 * @param onBack Callback de navegación hacia atrás.
 * Restricciones:
 * - No nulo.
 * - Debe ser rápido (se ejecuta en UI thread).
 * @param vm ViewModel de tema inyectado por Hilt. Se permite override para tests.
 *
 * @throws IllegalStateException No se lanza directamente, pero puede ocurrir si Hilt no puede proveer [ThemeViewModel]
 * en un entorno incorrecto (por ejemplo, previews sin configuración).
 *
 * @see SettingsContent UI pura de Ajustes.
 * @see ThemeViewModel Fuente de verdad del modo de tema.
 *
 * Ejemplo de uso:
 * {@code
 * SettingsScreen(
 *   onBack = navController::popBackStack
 * )
 * }
 */
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    vm: ThemeViewModel = hiltViewModel()
) {
    val mode by vm.themeMode.collectAsStateWithLifecycle()

    SettingsContent(
        mode = mode,
        onBack = onBack,
        onSelectMode = vm::setThemeMode
    )
}


@Preview(showBackground = true)
@Composable
private fun PreviewSettings_System() {
    TeamMaravillaAppTheme {
        SettingsContent(
            mode = ThemeMode.SYSTEM,
            onBack = {},
            onSelectMode = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSettings_Light() {
    TeamMaravillaAppTheme {
        SettingsContent(
            mode = ThemeMode.LIGHT,
            onBack = {},
            onSelectMode = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSettings_Dark() {
    TeamMaravillaAppTheme {
        SettingsContent(
            mode = ThemeMode.DARK,
            onBack = {},
            onSelectMode = {}
        )
    }
}
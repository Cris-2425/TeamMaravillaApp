package com.example.teammaravillaapp.page.splash

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Pantalla contenedora de Splash.
 *
 * Responsabilidades:
 * - Orquestar posibles efectos secundarios (delay, navegación automática, chequeo de sesión).
 * - Delegar el render visual a [SplashContent].
 *
 * Actualmente no contiene lógica de temporización ni navegación automática,
 * pero se mantiene como contenedor para preservar la arquitectura
 * consistente (contenedor vs presentación).
 *
 * @param onSplashFinished Callback opcional que se ejecutará cuando el splash termine.
 * Restricciones:
 * - Puede ser vacío si la navegación se gestiona externamente.
 *
 * @see SplashContent
 *
 * Ejemplo de uso:
 * {@code
 * SplashScreen(
 *     onSplashFinished = { navController.navigate("home") }
 * )
 * }
 */
@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit = {}
) {
    SplashContent(
        imageRes = R.drawable.splash_maravilla
    )
}


@Preview(showBackground = true)
@Composable
private fun PreviewSplash() {
    TeamMaravillaAppTheme {
        SplashContent(
            imageRes = R.drawable.splash_maravilla
        )
    }
}
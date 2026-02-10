package com.example.teammaravillaapp.page.splash

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

/**
 * UI pura del Splash.
 *
 * Esta función:
 * - No conoce ViewModels
 * - No gestiona navegación
 * - No ejecuta delays
 *
 * Únicamente renderiza la imagen de inicio a pantalla completa.
 *
 * @param imageRes Recurso drawable que se mostrará como fondo del splash.
 * Restricciones:
 * - Debe ser un recurso válido.
 * - No debe ser 0.
 *
 * @throws IllegalArgumentException Puede lanzarse si el recurso no existe
 * (controlado internamente por painterResource).
 *
 * @see SplashScreen Contenedor lógico.
 *
 * Ejemplo de uso:
 * {@code
 * SplashContent(imageRes = R.drawable.splash_maravilla)
 * }
 */
@Composable
fun SplashContent(
    @DrawableRes imageRes: Int
) {
    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
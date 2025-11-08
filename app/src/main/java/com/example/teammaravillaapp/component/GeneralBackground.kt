package com.example.teammaravillaapp.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Fondo general de la app.
 *
 * `Box` de pantalla, para que el resto del
 * contenido se pinte encima.
 *
 * @param bgRes imagen opcional (si `null`, usa fondo por defecto).
 * @param overlayColor velo para contraste.
 * @param overlayAlpha opacidad del velo (0f = sin velo).
 */
@Composable
fun GeneralBackground(
    @DrawableRes bgRes: Int? = null,
    overlayColor: Color = MaterialTheme.colorScheme.surface,
    overlayAlpha: Float = 0.3f
) {
    Box(Modifier.fillMaxSize()) {
        val backgroundRes = bgRes ?: R.drawable.background_app_final

        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (overlayAlpha > 0f) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(overlayColor.copy(alpha = overlayAlpha))
            )
        }
    }
}

@Preview
@Composable
fun PreviewGeneralBackground() {
    TeamMaravillaAppTheme {
        GeneralBackground()
    }
}
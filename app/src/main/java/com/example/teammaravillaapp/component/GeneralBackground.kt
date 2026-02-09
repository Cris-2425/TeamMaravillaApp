package com.example.teammaravillaapp.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Fondo general reutilizable para pantallas de la aplicación.
 *
 * Proporciona un fondo visual consistente (imagen base + grano sutil + overlay) y opcionalmente
 * un “halo” superior para dar profundidad. Permite sustituir la imagen base con [bgRes] para
 * casos como fondos personalizados de listas.
 *
 * Recomendación: mantener [overlayAlpha] y [haloAlpha] en el rango 0..1 para evitar resultados
 * visuales inesperados.
 *
 * @param modifier Modificador de Compose para controlar tamaño/posición del contenedor.
 * @param bgRes Recurso drawable opcional para usar como fondo. Si es `null`, se usa el fondo oficial de la app.
 * @param overlayColor Color del overlay usado para mejorar contraste del contenido.
 * @param overlayAlpha Opacidad del overlay (0 = sin overlay, 1 = opaco).
 * @param showTopHalo Indica si se muestra el halo superior.
 * @param haloHeight Altura del halo superior.
 * @param haloAlpha Opacidad del halo (0 = transparente, 1 = opaco).
 * @param content Contenido composable que se dibuja por encima del fondo.
 *
 * Ejemplo de uso:
 * {@code
 * GeneralBackground {
 *   Scaffold { padding ->  ... }
 * }
 * }
 */
@Composable
fun GeneralBackground(
    modifier: Modifier = Modifier,
    @DrawableRes bgRes: Int? = null,
    overlayColor: Color = MaterialTheme.colorScheme.surface,
    overlayAlpha: Float = 0.18f,
    showTopHalo: Boolean = true,
    haloHeight: Dp = 280.dp,
    haloAlpha: Float = 0.10f,
    content: @Composable BoxScope.() -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val backgroundRes = bgRes ?: R.drawable.bg_app

    Box(modifier = modifier.fillMaxSize()) {

        // Fondo base
        Image(
            painter = painterResource(backgroundRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Halo superior
        if (showTopHalo) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(haloHeight)
                    .background(
                        Brush.verticalGradient(
                            0f to cs.primary.copy(alpha = haloAlpha),
                            1f to Color.Transparent
                        )
                    )
            )
        }

        // Grain / noise
        Image(
            painter = painterResource(R.drawable.bg_noise),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.035f
        )

        // Overlay para contraste
        if (overlayAlpha > 0f) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(overlayColor.copy(alpha = overlayAlpha))
            )
        }

        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun GeneralBackgroundPreview() {
    TeamMaravillaAppTheme {
        GeneralBackground {
            Box(modifier = Modifier.fillMaxSize())
        }
    }
}
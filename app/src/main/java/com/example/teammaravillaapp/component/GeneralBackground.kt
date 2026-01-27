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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R

/**
 * Fondo oficial de la app (pro).
 *
 * - Fondo por defecto: bg_app (gradiente + noise)
 * - Permite imagen alternativa (ej: fondos de listas)
 * - Halo superior sutil
 * - Overlay configurable para mejorar contraste
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

        // Grain
        Image(
            painter = painterResource(R.drawable.bg_noise),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.035f
        )

        // Overlay
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
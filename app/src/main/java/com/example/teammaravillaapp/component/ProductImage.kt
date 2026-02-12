package com.example.teammaravillaapp.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import java.util.Locale

/**
 * Renderiza la imagen de un producto con soporte **offline-friendly**:
 * - **Remoto** vía URL (Coil)
 * - **Local** vía `@DrawableRes` (seed / fallback)
 * - **Fallback textual** (abreviatura) cuando no hay imagen usable
 *
 * ### Prioridad de renderizado
 * 1) Si `imageUrl` no es nula ni vacía → carga remota con Coil.
 * 2) Si no, si `imageRes` es válido → muestra drawable local.
 * 3) Si no, si `showAbbrFallback` es `true` → muestra abreviatura derivada de `name`.
 * 4) Si no → muestra `errorRes` como último recurso.
 *
 * ### Por qué `SubcomposeAsyncImage`
 * Permite **componer UI distinta** según el estado del painter (Loading/Error/Success) sin duplicar el
 * contenedor, manteniendo una experiencia consistente incluso con recursos locales.
 *
 * ## Concurrencia
 * Función **pura de UI**: no bloquea hilos. La carga de imagen ocurre en background gestionada por Coil.
 *
 * @param name Nombre del producto. Se usa como `contentDescription` y para construir la abreviatura.
 * @param imageUrl URL remota opcional (p. ej. la devuelta por tu API).
 * @param imageRes Drawable local opcional (p. ej. productos seed).
 * @param modifier Modificador de Compose (tamaño, clip, padding, etc.).
 * @param contentScale Escalado de la imagen dentro del contenedor.
 * @param showAbbrFallback Si `true`, muestra abreviatura cuando no hay imagen disponible.
 * @param placeholderRes Drawable usado mientras carga la imagen remota (o si no hay `imageRes` local).
 * @param errorRes Drawable usado como último recurso si falla la carga y no se muestra abreviatura.
 */
@Composable
fun ProductImage(
    name: String,
    imageUrl: String?,
    @DrawableRes imageRes: Int?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    showAbbrFallback: Boolean = true,
    @DrawableRes placeholderRes: Int = R.drawable.logo,
    @DrawableRes errorRes: Int = R.drawable.logo
) {
    val context = LocalContext.current
    val abbr = name.trim().take(3).uppercase(Locale.getDefault()).ifEmpty { "?" }

    when {
        !imageUrl.isNullOrBlank() -> {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = name,
                modifier = modifier,
                contentScale = contentScale
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        if (imageRes != null && imageRes != 0) {
                            Image(
                                painter = painterResource(imageRes),
                                contentDescription = name,
                                modifier = modifier,
                                contentScale = contentScale
                            )
                        } else {
                            Image(
                                painter = painterResource(placeholderRes),
                                contentDescription = null,
                                modifier = modifier,
                                contentScale = contentScale
                            )
                        }
                    }

                    is AsyncImagePainter.State.Error -> {
                        when {
                            imageRes != null && imageRes != 0 -> Image(
                                painter = painterResource(imageRes),
                                contentDescription = name,
                                modifier = modifier,
                                contentScale = contentScale
                            )

                            showAbbrFallback -> Box(
                                modifier = modifier,
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = abbr, style = MaterialTheme.typography.labelMedium)
                            }

                            else -> Image(
                                painter = painterResource(errorRes),
                                contentDescription = name,
                                modifier = modifier,
                                contentScale = contentScale
                            )
                        }
                    }

                    else -> {
                        SubcomposeAsyncImageContent()
                    }
                }
            }
        }

        imageRes != null && imageRes != 0 -> {
            Image(
                painter = painterResource(imageRes),
                contentDescription = name,
                contentScale = contentScale,
                modifier = modifier
            )
        }

        showAbbrFallback -> {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                Text(text = abbr, style = MaterialTheme.typography.labelMedium)
            }
        }

        else -> {
            Image(
                painter = painterResource(errorRes),
                contentDescription = name,
                contentScale = contentScale,
                modifier = modifier
            )
        }
    }
}

/**
 * Muestras de uso para KDoc.
 */
@Suppress("unused")
private object ProductImageSamples {
    @Composable
    fun basic() {
        ProductImage(
            name = "Manzana",
            imageUrl = "https://example.com/images/apple.png",
            imageRes = null
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductImagePreview() {
    TeamMaravillaAppTheme {
        ProductImage(
            name = "Manzana",
            imageUrl = null,
            imageRes = R.drawable.logo
        )
    }
}
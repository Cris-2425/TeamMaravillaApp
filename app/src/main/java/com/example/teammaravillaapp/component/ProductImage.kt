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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import java.util.Locale

/**
 * Imagen de producto con soporte para:
 * - URL remota (Coil)
 * - Recurso local drawable
 * - Fallback textual (abreviatura) si no hay imagen
 *
 * Prioridad de renderizado:
 * 1) Si [imageUrl] no es nula ni vacía, se muestra imagen remota.
 * 2) Si no, si [imageRes] es un drawable válido, se muestra imagen local.
 * 3) Si no, si [showAbbrFallback] es `true`, se muestra abreviatura del nombre.
 * 4) En caso contrario, se muestra [errorRes] como último recurso.
 *
 * @param name Nombre del producto. Se usa como `contentDescription` y para la abreviatura.
 * @param imageUrl URL remota opcional (por ejemplo, de tu API).
 * @param imageRes Drawable local opcional (por ejemplo, productos seed).
 * @param modifier Modificador de Compose para tamaño, recorte, etc.
 * @param contentScale Escalado de la imagen dentro del contenedor.
 * @param showAbbrFallback Indica si debe mostrarse abreviatura cuando no hay imagen disponible.
 * @param placeholderRes Drawable usado como placeholder durante la carga remota.
 * @param errorRes Drawable usado cuando falla la carga o como fallback final.
 *
 * Ejemplo de uso:
 * {@code
 * ProductImage(
 *   name = product.name,
 *   imageUrl = product.imageUrl,
 *   imageRes = product.imageRes
 * )
 * }
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
    val abbr = name
        .trim()
        .take(3)
        .uppercase(Locale.getDefault())
        .ifEmpty { "?" }

    when {
        !imageUrl.isNullOrBlank() -> {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = name,
                contentScale = contentScale,
                modifier = modifier,
                placeholder = painterResource(placeholderRes),
                error = painterResource(errorRes)
            )
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
                Text(
                    text = abbr,
                    style = MaterialTheme.typography.labelMedium
                )
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
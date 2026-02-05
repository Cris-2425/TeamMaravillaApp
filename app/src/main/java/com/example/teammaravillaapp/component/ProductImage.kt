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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.teammaravillaapp.R

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
    val abbr = name.take(3)

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
            // Si no quieres abreviatura, al menos dejamos un placeholder
            Image(
                painter = painterResource(errorRes),
                contentDescription = name,
                contentScale = contentScale,
                modifier = modifier
            )
        }
    }
}
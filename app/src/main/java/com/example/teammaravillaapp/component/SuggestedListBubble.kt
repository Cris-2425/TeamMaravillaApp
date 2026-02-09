package com.example.teammaravillaapp.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.model.SuggestedList
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Burbuja circular para representar una lista sugerida (imagen + etiqueta).
 *
 * Componente de presentación: muestra una imagen circular y, sobre ella, una banda inferior con el nombre.
 * La interacción se delega a [onClick].
 *
 * @param suggested Modelo con el nombre e imagen de la lista sugerida.
 * @param modifier Modificador de Compose para controlar tamaño/posición. Por defecto aplica un tamaño compacto.
 * @param onClick Acción a ejecutar cuando el usuario pulsa la burbuja.
 *
 * Ejemplo de uso:
 * {@code
 * SuggestedListBubble(
 *   suggested = item,
 *   onClick = { viewModel.onSuggestedListSelected(item) }
 * )
 * }
 */
@Composable
fun SuggestedListBubble(
    suggested: SuggestedList,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.secondary
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = suggested.imageRes),
                contentDescription = suggested.name,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.55f))
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = suggested.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SuggestedListBubblePreview() {
    TeamMaravillaAppTheme {
        SuggestedListBubble(
            suggested = SuggestedList(
                name = "BBQ sábado",
                imageRes = R.drawable.fondo_bbq
            )
        )
    }
}
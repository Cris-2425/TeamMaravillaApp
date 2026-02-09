package com.example.teammaravillaapp.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Tarjeta individual de un fondo seleccionable.
 *
 * Se utiliza dentro de una cuadrícula de selección. Si [selected] es `true`,
 * se muestra un borde resaltado y un icono de confirmación.
 *
 * @param modifier Modificador para ajustar tamaño, padding y comportamiento de layout.
 * @param selected Indica si esta opción está seleccionada.
 * @param title Texto visible sobre la tarjeta (por ejemplo, el nombre del fondo).
 * @param imageRes Recurso drawable a mostrar como imagen de fondo.
 * @param onClick Acción a ejecutar cuando el usuario pulsa la tarjeta.
 *
 * Ejemplo de uso:
 * {@code
 * BackgroundTile(
 *   selected = uiState.selectedBackground == bg,
 *   title = bg.name,
 *   imageRes = ListBackgrounds.getBackgroundRes(bg),
 *   onClick = { viewModel.onBackgroundSelected(bg) }
 * )
 * }
 */
@Composable
fun BackgroundTile(
    modifier: Modifier = Modifier,
    selected: Boolean,
    title: String,
    @DrawableRes imageRes: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(76.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(22.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .padding(3.dp)
            )
        }

        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BackgroundTileSelectedPreview() {
    TeamMaravillaAppTheme {
        BackgroundTile(
            selected = true,
            title = "FONDO1",
            imageRes = android.R.drawable.ic_menu_gallery,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BackgroundTileUnselectedPreview() {
    TeamMaravillaAppTheme {
        BackgroundTile(
            selected = false,
            title = "FONDO1",
            imageRes = android.R.drawable.ic_menu_gallery,
            onClick = {}
        )
    }
}
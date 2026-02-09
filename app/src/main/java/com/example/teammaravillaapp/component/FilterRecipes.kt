package com.example.teammaravillaapp.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Chip de filtro seleccionable para la pantalla de recetas.
 *
 * Este componente es puramente de presentación: no gestiona estado interno.
 * El estado de selección se recibe mediante [selected] y la interacción se delega a [onClick].
 *
 * @param modifier Modificador de Compose para controlar padding, tamaño o posición.
 * @param text Texto que se muestra en el chip. Recomendado: corto para evitar truncados.
 * @param selected Indica si el chip está seleccionado (aplica estilos de énfasis).
 * @param onClick Acción a ejecutar cuando el usuario pulsa el chip.
 *
 * Ejemplo de uso:
 * {@code
 * FilterRecipes(
 *   text = stringResource(R.string.recipes_filter_favorites),
 *   selected = uiState.onlyFavorites,
 *   onClick = { viewModel.onToggleFavorites() }
 * )
 * }
 */
@Composable
fun FilterRecipes(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        color = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer,
        contentColor = if (selected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterRecipesSelectedPreview() {
    TeamMaravillaAppTheme {
        FilterRecipes(
            text = "Vegetarianas",
            selected = true,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterRecipesUnselectedPreview() {
    TeamMaravillaAppTheme {
        FilterRecipes(
            text = "Vegetarianas",
            selected = false,
            onClick = {}
        )
    }
}
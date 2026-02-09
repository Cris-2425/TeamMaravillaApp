package com.example.teammaravillaapp.page.stats.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Tarjeta compacta para mostrar una estadística en formato "título + valor".
 *
 * Componente de presentación usado en la pantalla de estadísticas.
 *
 * @param title Título corto de la métrica (por ejemplo, "Listas creadas").
 * @param value Valor de la métrica (por ejemplo, "12").
 * @param modifier Modificador de Compose para controlar tamaño/posición.
 *
 * Ejemplo de uso:
 * {@code
 * StatTile(
 *   title = stringResource(R.string.stats_lists_created),
 *   value = uiState.listsCreated.toString()
 * )
 * }
 */
@Composable
internal fun StatTile(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatTilePreview() {
    TeamMaravillaAppTheme {
        StatTile(
            title = "Listas creadas",
            value = "12",
            modifier = Modifier.padding(16.dp)
        )
    }
}
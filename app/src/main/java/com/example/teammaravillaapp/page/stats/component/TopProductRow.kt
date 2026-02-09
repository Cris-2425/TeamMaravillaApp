package com.example.teammaravillaapp.page.stats.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Fila de ranking para mostrar un producto "top" en la pantalla de estadísticas.
 *
 * @param rank Posición en el ranking (1, 2, 3...).
 * @param name Nombre del producto.
 * @param times Número de veces registrado (por ejemplo, comprado o añadido).
 *
 * Ejemplo de uso:
 * {@code
 * TopProductRow(
 *   rank = 1,
 *   name = "Manzana",
 *   times = 24
 * )
 * }
 */
@Composable
internal fun TopProductRow(
    rank: Int,
    name: String,
    times: Int
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        tonalElevation = 0.dp,
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Text(
                    text = "#$rank",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(R.string.stats_times_format, times),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopProductRowPreview() {
    TeamMaravillaAppTheme {
        TopProductRow(
            rank = 1,
            name = "Manzana Golden",
            times = 24
        )
    }
}
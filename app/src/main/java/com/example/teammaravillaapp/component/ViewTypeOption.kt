package com.example.teammaravillaapp.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Opción seleccionable para elegir un tipo de vista (por ejemplo: Lista / Cuadrícula).
 *
 * Componente de presentación: el estado [selected] viene de fuera y la interacción se delega a [onClick].
 *
 * @param modifier Modificador de Compose para controlar layout.
 * @param title Título de la opción.
 * @param desc Descripción breve de la opción.
 * @param selected Indica si esta opción es la seleccionada actualmente.
 * @param onClick Acción al pulsar la fila o el radio.
 *
 * Ejemplo de uso:
 * {@code
 * ViewTypeOption(
 *   title = stringResource(R.string.view_type_list),
 *   desc = stringResource(R.string.view_type_list_desc),
 *   selected = uiState.viewType == ViewType.LIST,
 *   onClick = { viewModel.onViewTypeSelected(ViewType.LIST) }
 * )
 * }
 */
@Composable
fun ViewTypeOption(
    modifier: Modifier = Modifier,
    title: String,
    desc: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        tonalElevation = 1.dp,
        modifier = modifier.fillMaxWidth(),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = if (selected) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onSurface,
        onClick = onClick
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RadioButton(selected = selected, onClick = onClick)
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(2.dp))
                Text(
                    desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ViewTypeOptionPreview() {
    TeamMaravillaAppTheme {
        ViewTypeOption(
            title = "Vista lista",
            desc = "Muestra los elementos en filas verticales.",
            selected = true,
            onClick = {}
        )
    }
}
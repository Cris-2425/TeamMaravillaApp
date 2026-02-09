package com.example.teammaravillaapp.component.legacy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Opción circular seleccionable.
 *
 * Se utiliza para representar una elección simple (por ejemplo, un “tipo de vista” o “estilo de lista”).
 * El componente no gestiona estado interno: recibe [selected] como entrada y delega la interacción a [onClick].
 *
 * @param modifier Modificador de Compose para controlar tamaño, padding o alineación. Por defecto aplica un tamaño circular.
 * @param label Texto mostrado dentro del círculo. Recomendado: corto (1–2 palabras) para evitar cortes.
 * @param selected Indica si la opción está seleccionada. Cuando es `true` se muestra un check y estilos de énfasis.
 * @param onClick Acción a ejecutar al pulsar la opción.
 *
 * @see Surface
 *
 * Ejemplo de uso:
 * {@code
 * CircularOption(
 *   label = "Lista",
 *   selected = uiState.viewType == ViewType.LIST,
 *   onClick = { viewModel.onViewTypeSelected(ViewType.LIST) }
 * )
 * }
 */
@Composable
fun CircularOption(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
        contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary,
        modifier = modifier.size(92.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (selected) {
                    Icon(Icons.Filled.Check, contentDescription = null)
                    Spacer(Modifier.height(4.dp))
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CircularOptionPreview() {
    TeamMaravillaAppTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularOption(label = "Seleccionado", selected = true, onClick = {})
            Spacer(Modifier.height(16.dp))
            CircularOption(label = "No seleccionado", selected = false, onClick = {})
        }
    }
}
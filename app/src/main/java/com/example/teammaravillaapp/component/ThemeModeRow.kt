package com.example.teammaravillaapp.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Fila seleccionable para elegir un modo de tema (por ejemplo: Claro / Oscuro / Sistema).
 *
 * Componente de presentación: el estado [selected] viene de fuera y la interacción se delega a [onClick].
 *
 * @param modifier Modificador de Compose para controlar layout.
 * @param title Texto mostrado como etiqueta.
 * @param selected Indica si la opción está seleccionada.
 * @param onClick Acción al pulsar la fila o el radio.
 *
 * Ejemplo de uso:
 * {@code
 * ThemeModeRow(
 *   title = stringResource(R.string.theme_mode_dark),
 *   selected = uiState.themeMode == ThemeMode.DARK,
 *   onClick = { viewModel.onThemeModeSelected(ThemeMode.DARK) }
 * )
 * }
 */
@Composable
fun ThemeModeRow(
    modifier: Modifier = Modifier,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = if (selected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = selected, onClick = onClick)
            Spacer(Modifier.width(10.dp))
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ThemeModeRowPreview() {
    TeamMaravillaAppTheme {
        ThemeModeRow(
            title = "Oscuro",
            selected = true,
            onClick = {}
        )
    }
}
package com.example.teammaravillaapp.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Grid fluido de burbujas de opciones.
 *
 * Renderiza una lista de etiquetas mediante [OptionBubble] distribuidas en filas con un máximo
 * de 3 elementos por fila. La interacción se comunica al exterior mediante [onOptionClick].
 *
 * @param modifier Modificador de Compose para controlar layout, padding y tamaño.
 * @param options Lista de textos a mostrar en cada burbuja.
 * @param onOptionClick Callback invocado al pulsar una opción. Se proporciona el índice de la opción pulsada.
 *
 * @see OptionBubble
 *
 * Ejemplo de uso:
 * {@code
 * OptionsGrid(
 *   options = uiState.profileOptions,
 *   onOptionClick = { index -> viewModel.onProfileOptionClick(index) }
 * )
 * }
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OptionsGrid(
    modifier: Modifier = Modifier,
    options: List<String>,
    onOptionClick: (Int) -> Unit
) {
    FlowRow(
        maxItemsInEachRow = 3,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        options.forEachIndexed { index, text ->
            OptionBubble(
                label = text,
                modifier = Modifier.clickable { onOptionClick(index) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OptionsGridPreview() {
    TeamMaravillaAppTheme {
        OptionsGrid(
            options = listOf("Historial", "Favoritos", "Configuración"),
            onOptionClick = {}
        )
    }
}
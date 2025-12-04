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
 * Grid fluido para **burbujas de opciones** (perfil).
 *
 * Ahora soporta callback de click con índice.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OptionsGrid(
    options: List<String>,
    onOptionClick: (Int) -> Unit = {}
) {
    FlowRow(
        maxItemsInEachRow = 3,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEachIndexed { index, text ->
            OptionBubble(
                label = text,
                modifier = Modifier.clickable { onOptionClick(index) }
            )
        }
    }
}

@Preview
@Composable
fun PreviewOptionsGrid() {
    TeamMaravillaAppTheme {
        OptionsGrid(
            listOf(
                "Historial",
                "Favoritos",
                "Configuración"
            )
        )
    }
}
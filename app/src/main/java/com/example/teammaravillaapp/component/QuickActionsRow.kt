package com.example.teammaravillaapp.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.model.QuickActionData
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Fila horizontal de acciones rápidas en formato chip.
 *
 * Renderiza una lista de [QuickActionData] usando `ElevatedAssistChip` y delega el click
 * al exterior mediante [onClick]. No gestiona estado interno.
 *
 * @param actions Lista de acciones rápidas a mostrar.
 * @param onClick Callback invocado al pulsar un chip, devolviendo la acción asociada.
 * @param modifier Modificador de Compose para controlar layout, padding o tamaño.
 *
 * Ejemplo de uso:
 * {@code
 * QuickActionsRow(
 *   actions = uiState.quickActions,
 *   onClick = { action -> viewModel.onQuickActionClick(action) }
 * )
 * }
 */
@Composable
fun QuickActionsRow(
    actions: List<QuickActionData>,
    onClick: (QuickActionData) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 2.dp)
    ) {
        items(actions) { action ->
            ElevatedAssistChip(
                onClick = { onClick(action) },
                label = { Text(action.label) },
                leadingIcon = {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = action.label
                    )
                },
                colors = AssistChipDefaults.elevatedAssistChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuickActionsRowPreview() {
    TeamMaravillaAppTheme {
        QuickActionsRow(
            actions = listOf(
                QuickActionData(Icons.Default.ShoppingCart, "Nueva lista"),
                QuickActionData(Icons.Default.ShoppingCart, "Añadir producto")
            ),
            onClick = {}
        )
    }
}
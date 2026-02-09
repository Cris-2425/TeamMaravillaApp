package com.example.teammaravillaapp.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.data.seed.SuggestedListData
import com.example.teammaravillaapp.model.SuggestedList
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Sección que muestra listas sugeridas en una rejilla fluida.
 *
 * No incluye scroll por sí misma: si es necesario, debe colocarse dentro de un contenedor con scroll
 * (por ejemplo, `LazyColumn` o `Column.verticalScroll`).
 *
 * @param modifier Modificador de Compose para controlar layout y tamaño.
 * @param items Listas sugeridas a renderizar.
 * @param onPick Callback invocado cuando el usuario selecciona una lista sugerida.
 *
 * Ejemplo de uso:
 * {@code
 * SuggestedListSection(
 *   items = uiState.suggestedLists,
 *   onPick = viewModel::onSuggestedListPicked
 * )
 * }
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SuggestedListSection(
    modifier: Modifier = Modifier,
    items: List<SuggestedList>,
    onPick: (SuggestedList) -> Unit = {}
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.forEach { item ->
            SuggestedListBubble(
                suggested = item,
                onClick = { onPick(item) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SuggestedListSectionPreview() {
    TeamMaravillaAppTheme {
        SuggestedListSection(
            items = SuggestedListData.items,
            onPick = {}
        )
    }
}
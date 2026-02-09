package com.example.teammaravillaapp.page.listdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.model.ListViewType
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.page.listdetail.ListItemUi
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Contenedor visual para renderizar los items de una lista en el detalle.
 *
 * Ajusta padding y separación vertical en función del [viewType] y delega el renderizado
 * de cada fila a [ListItemRow].
 *
 * @param viewType Tipo de vista actual (burbujas, lista o compacto).
 * @param items Lista de items a mostrar.
 * @param onToggleChecked Acción al marcar/desmarcar un item (recibe el id de producto).
 * @param onRemove Acción al eliminar un item (recibe el id de producto).
 * @param onDec Acción al decrementar cantidad (recibe el id de producto).
 * @param onInc Acción al incrementar cantidad (recibe el id de producto).
 * @param modifier Modificador de Compose para controlar layout.
 *
 * Ejemplo de uso:
 * {@code
 * ItemsContainer(
 *   viewType = uiState.viewType,
 *   items = uiState.items,
 *   onToggleChecked = viewModel::onToggleChecked,
 *   onRemove = viewModel::onRemoveItem,
 *   onDec = viewModel::onDecQty,
 *   onInc = viewModel::onIncQty
 * )
 * }
 */
@Composable
internal fun ItemsContainer(
    viewType: ListViewType,
    items: List<ListItemUi>,
    onToggleChecked: (String) -> Unit,
    onRemove: (String) -> Unit,
    onDec: (String) -> Unit,
    onInc: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    when (viewType) {
                        ListViewType.COMPACT -> 10.dp
                        else -> 12.dp
                    }
                ),
            verticalArrangement = Arrangement.spacedBy(
                when (viewType) {
                    ListViewType.COMPACT -> 8.dp
                    else -> 10.dp
                }
            )
        ) {
            items.forEach { item ->
                ListItemRow(
                    viewType = viewType,
                    item = item,
                    onToggleChecked = onToggleChecked,
                    onRemove = onRemove,
                    onDec = onDec,
                    onInc = onInc
                )
            }
        }
    }
}

private fun previewItem(id: String, name: String, checked: Boolean, qty: Int, pos: Int) =
    ListItemUi(
        product = Product(
            id = id,
            name = name,
            imageRes = null,
            category = ProductCategory.OTHER,
            imageUrl = null
        ),
        checked = checked,
        quantity = qty,
        position = pos
    )

@Preview(showBackground = true)
@Composable
private fun ItemsContainerPreviewList() {
    TeamMaravillaAppTheme {
        ItemsContainer(
            viewType = ListViewType.LIST,
            items = listOf(
                previewItem("p1", "Manzana", false, 2, 0),
                previewItem("p2", "Leche", true, 1, 1),
                previewItem("p3", "Pan", false, 3, 2)
            ),
            onToggleChecked = {},
            onRemove = {},
            onDec = {},
            onInc = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemsContainerPreviewCompact() {
    TeamMaravillaAppTheme {
        ItemsContainer(
            viewType = ListViewType.COMPACT,
            items = listOf(
                previewItem("p1", "Manzana", false, 2, 0),
                previewItem("p2", "Leche", false, 1, 1)
            ),
            onToggleChecked = {},
            onRemove = {},
            onDec = {},
            onInc = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
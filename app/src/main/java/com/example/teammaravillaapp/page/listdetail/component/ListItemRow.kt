package com.example.teammaravillaapp.page.listdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.component.ProductBubble
import com.example.teammaravillaapp.model.ListViewType
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.page.listdetail.ListItemUi
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Fila individual de producto dentro del detalle de lista.
 *
 * Renderiza el item con diferentes layouts según [viewType]:
 * - [ListViewType.BUBBLES]: burbuja + checkbox + controles de cantidad.
 * - [ListViewType.LIST]: burbuja + nombre + checkbox + controles.
 * - [ListViewType.COMPACT]: solo nombre + checkbox + controles.
 *
 * @param viewType Tipo de vista actual.
 * @param item Item a renderizar (producto + cantidad + checked).
 * @param onToggleChecked Acción al marcar/desmarcar (recibe id de producto).
 * @param onRemove Acción al eliminar (recibe id de producto).
 * @param onDec Acción al decrementar cantidad (recibe id de producto).
 * @param onInc Acción al incrementar cantidad (recibe id de producto).
 * @param modifier Modificador de Compose para controlar layout.
 */
@Composable
internal fun ListItemRow(
    viewType: ListViewType,
    item: ListItemUi,
    onToggleChecked: (String) -> Unit,
    onRemove: (String) -> Unit,
    onDec: (String) -> Unit,
    onInc: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val pid = item.product.id

    when (viewType) {
        ListViewType.BUBBLES -> {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Checkbox(checked = item.checked, onCheckedChange = { onToggleChecked(pid) })

                ProductBubble(product = item.product, onClick = { onRemove(pid) })

                Spacer(Modifier.weight(1f))

                QuantityControls(
                    quantity = item.quantity,
                    onDec = { onDec(pid) },
                    onInc = { onInc(pid) }
                )
            }
        }

        ListViewType.LIST -> {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = modifier
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Checkbox(checked = item.checked, onCheckedChange = { onToggleChecked(pid) })

                    ProductBubble(
                        product = item.product,
                        modifier = Modifier.width(72.dp),
                        onClick = { onRemove(pid) }
                    )

                    Text(
                        text = item.product.name,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.weight(1f),
                        maxLines = 1
                    )

                    QuantityControls(
                        quantity = item.quantity,
                        onDec = { onDec(pid) },
                        onInc = { onInc(pid) }
                    )
                }
            }
        }

        ListViewType.COMPACT -> {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = modifier
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Checkbox(checked = item.checked, onCheckedChange = { onToggleChecked(pid) })

                    Text(
                        text = item.product.name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f),
                        maxLines = 1
                    )

                    QuantityControls(
                        quantity = item.quantity,
                        onDec = { onDec(pid) },
                        onInc = { onInc(pid) }
                    )
                }
            }
        }
    }
}

private fun previewProduct(id: String, name: String) = Product(
    id = id,
    name = name,
    imageRes = null,
    category = ProductCategory.OTHER,
    imageUrl = null
)

@Preview(showBackground = true)
@Composable
private fun ListItemRowPreviewBubbles() {
    TeamMaravillaAppTheme {
        ListItemRow(
            viewType = ListViewType.BUBBLES,
            item = ListItemUi(
                product = previewProduct("p1", "Manzana"),
                checked = false,
                quantity = 2,
                position = 0
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
private fun ListItemRowPreviewList() {
    TeamMaravillaAppTheme {
        ListItemRow(
            viewType = ListViewType.LIST,
            item = ListItemUi(
                product = previewProduct("p2", "Leche"),
                checked = true,
                quantity = 1,
                position = 1
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
private fun ListItemRowPreviewCompact() {
    TeamMaravillaAppTheme {
        ListItemRow(
            viewType = ListViewType.COMPACT,
            item = ListItemUi(
                product = previewProduct("p3", "Pan"),
                checked = false,
                quantity = 3,
                position = 2
            ),
            onToggleChecked = {},
            onRemove = {},
            onDec = {},
            onInc = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
package com.example.teammaravillaapp.page.listdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.component.ProductBubble
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Sección que muestra productos de una categoría concreta en formato burbuja.
 *
 * Cada producto se renderiza mediante [ProductBubble] y al pulsarlo se invoca [onAdd]
 * con el id del producto.
 *
 * @param products Lista de productos a mostrar.
 * @param onAdd Acción al pulsar un producto (recibe su id).
 * @param modifier Modificador de Compose para controlar layout.
 *
 * Ejemplo de uso:
 * {@code
 * CategorySection(
 *   products = uiState.filteredProducts,
 *   onAdd = viewModel::onAddProduct
 * )
 * }
 */
@Composable
internal fun CategorySection(
    products: List<Product>,
    onAdd: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    ) {
        @OptIn(ExperimentalLayoutApi::class)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            products.forEach { product ->
                ProductBubble(product = product, onClick = { onAdd(product.id) })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CategorySectionPreview() {
    TeamMaravillaAppTheme {
        CategorySection(
            products = listOf(
                Product(id = "p1", name = "Manzana", category = ProductCategory.FRUITS),
                Product(id = "p2", name = "Leche", category = ProductCategory.DAIRY),
                Product(id = "p3", name = "Pan", category = ProductCategory.BAKERY),
            ),
            onAdd = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
package com.example.teammaravillaapp.page.listdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.ProductBubble
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Sección de productos usados recientemente (acceso rápido).
 *
 * - En modo loading muestra un texto de carga.
 * - Si está vacío, muestra un mensaje (error si existe, o “vacío”).
 * - Si hay productos, los pinta como [ProductBubble] clicables.
 *
 * @param isLoadingCatalog Indica si el catálogo está cargando.
 * @param recentAvailable Lista de productos recientes disponibles.
 * @param catalogError Mensaje opcional de error (preferiblemente user-friendly).
 * @param onAdd Acción al pulsar un producto (recibe el id).
 * @param modifier Modificador de Compose para controlar layout.
 */
@Composable
internal fun RecentUsedSection(
    isLoadingCatalog: Boolean,
    recentAvailable: List<Product>,
    catalogError: String?,
    onAdd: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
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
            when {
                isLoadingCatalog -> Text(
                    text = stringResource(R.string.list_detail_catalog_loading),
                    style = MaterialTheme.typography.bodyMedium
                )

                recentAvailable.isEmpty() -> Text(
                    text = catalogError ?: stringResource(R.string.list_detail_catalog_empty),
                    style = MaterialTheme.typography.bodyMedium
                )

                else -> recentAvailable.forEach { product ->
                    ProductBubble(product = product, onClick = { onAdd(product.id) })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecentUsedSectionPreviewLoading() {
    TeamMaravillaAppTheme {
        RecentUsedSection(
            isLoadingCatalog = true,
            recentAvailable = emptyList(),
            catalogError = null,
            onAdd = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecentUsedSectionPreviewEmpty() {
    TeamMaravillaAppTheme {
        RecentUsedSection(
            isLoadingCatalog = false,
            recentAvailable = emptyList(),
            catalogError = null,
            onAdd = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecentUsedSectionPreviewError() {
    TeamMaravillaAppTheme {
        RecentUsedSection(
            isLoadingCatalog = false,
            recentAvailable = emptyList(),
            catalogError = "No se pudo cargar el catálogo.",
            onAdd = {}
        )
    }
}
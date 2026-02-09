package com.example.teammaravillaapp.page.listdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
 * Sección de búsqueda para añadir productos a la lista.
 *
 * - Muestra un campo de texto con botón de limpiar.
 * - Si [query] no está vacío, renderiza un panel con:
 *   - estado cargando,
 *   - “sin resultados”,
 *   - o los resultados como burbujas clicables.
 *
 * @param query Texto actual de búsqueda.
 * @param isLoadingCatalog Indica si el catálogo está cargando.
 * @param searchResults Resultados actuales de búsqueda.
 * @param onQueryChanged Callback al cambiar el texto.
 * @param onAddFromSearch Acción al pulsar un resultado (recibe el id del producto).
 * @param modifier Modificador de Compose para controlar layout.
 *
 * Ejemplo de uso:
 * {@code
 * SearchSection(
 *   query = uiState.query,
 *   isLoadingCatalog = uiState.isLoadingCatalog,
 *   searchResults = uiState.searchResults,
 *   onQueryChanged = viewModel::onQueryChanged,
 *   onAddFromSearch = viewModel::onAddFromSearch
 * )
 * }
 */
@Composable
internal fun SearchSection(
    query: String,
    isLoadingCatalog: Boolean,
    searchResults: List<Product>,
    onQueryChanged: (String) -> Unit,
    onAddFromSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(stringResource(R.string.list_detail_search_placeholder)) },
            trailingIcon = {
                if (query.isNotBlank()) {
                    IconButton(onClick = { onQueryChanged("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.common_clear)
                        )
                    }
                }
            }
        )

        if (query.isNotBlank()) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    when {
                        isLoadingCatalog -> Text(
                            text = stringResource(R.string.list_detail_search_loading),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        searchResults.isEmpty() -> Text(
                            text = stringResource(R.string.list_detail_search_no_results),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        else -> {
                            Text(
                                text = stringResource(R.string.list_detail_search_results_title),
                                style = MaterialTheme.typography.titleSmall
                            )

                            @OptIn(ExperimentalLayoutApi::class)
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                searchResults.forEach { product ->
                                    ProductBubble(
                                        product = product,
                                        onClick = { onAddFromSearch(product.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchSectionPreviewEmptyQuery() {
    TeamMaravillaAppTheme {
        SearchSection(
            query = "",
            isLoadingCatalog = false,
            searchResults = emptyList(),
            onQueryChanged = {},
            onAddFromSearch = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchSectionPreviewLoading() {
    TeamMaravillaAppTheme {
        SearchSection(
            query = "Tomatillos",
            isLoadingCatalog = true,
            searchResults = emptyList(),
            onQueryChanged = {},
            onAddFromSearch = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchSectionPreviewNoResults() {
    TeamMaravillaAppTheme {
        SearchSection(
            query = "Chuletón",
            isLoadingCatalog = false,
            searchResults = emptyList(),
            onQueryChanged = {},
            onAddFromSearch = {}
        )
    }
}
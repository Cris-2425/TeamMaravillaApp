package com.example.teammaravillaapp.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.model.SearchFieldData
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Campo de búsqueda reutilizable.
 *
 * Renderiza un `OutlinedTextField` configurado para búsqueda. Si [onSearch] no es nulo,
 * el teclado mostrará la acción "Search" y la ejecutará al pulsarla.
 *
 * @param searchData Datos del campo (valor actual y placeholder).
 * @param modifier Modificador de Compose para controlar el layout.
 * @param onValueChange Callback que notifica cambios en el texto.
 * @param onSearch Acción opcional al disparar la búsqueda desde el teclado.
 *
 * Ejemplo de uso:
 * {@code
 * SearchField(
 *   searchData = uiState.search,
 *   onValueChange = viewModel::onSearchTextChange,
 *   onSearch = viewModel::onSearch
 * )
 * }
 */
@Composable
fun SearchField(
    searchData: SearchFieldData,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    onSearch: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = searchData.value,
        onValueChange = onValueChange,
        placeholder = { Text(searchData.placeholder) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = if (onSearch != null) ImeAction.Search else ImeAction.Done),
        keyboardActions = KeyboardActions(onSearch = { onSearch?.invoke() })
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchFieldPreviewEmpty() {
    TeamMaravillaAppTheme {
        SearchField(
            searchData = SearchFieldData(
                value = "",
                placeholder = stringResource(R.string.search_placeholder_products_lists)
            ),
            onValueChange = {},
            onSearch = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchFieldPreviewTyped() {
    TeamMaravillaAppTheme {
        SearchField(
            searchData = SearchFieldData(
                value = "pasta",
                placeholder = stringResource(R.string.search_placeholder_products_lists)
            ),
            onValueChange = {},
            onSearch = {}
        )
    }
}
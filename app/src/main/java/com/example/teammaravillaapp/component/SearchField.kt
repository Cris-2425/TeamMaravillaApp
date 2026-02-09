package com.example.teammaravillaapp.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.model.SearchFieldData
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

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
        keyboardActions = KeyboardActions(
            onSearch = { onSearch?.invoke() }
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewSearchFieldEmpty() {
    TeamMaravillaAppTheme {
        SearchField(
            searchData = SearchFieldData(value = "", placeholder = "Buscar producto o lista"),
            onValueChange = {},
            onSearch = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSearchFieldTyped() {
    TeamMaravillaAppTheme {
        SearchField(
            searchData = SearchFieldData(value = "pasta", placeholder = "Buscar producto o lista"),
            onValueChange = {},
            onSearch = {}
        )
    }
}
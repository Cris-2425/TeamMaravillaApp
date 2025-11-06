package com.example.teammaravillaapp.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

data class SearchFieldData(
    val value: String,
    val placeholder: String
)

@Composable
fun SearchField(
    searchData: SearchFieldData,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchData.value,
        onValueChange = onValueChange,
        placeholder = { Text(searchData.placeholder) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Preview
@Composable
fun PreviewSearchField() {
    TeamMaravillaAppTheme {
    }
}
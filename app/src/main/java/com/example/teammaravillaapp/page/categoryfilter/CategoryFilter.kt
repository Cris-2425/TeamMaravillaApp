package com.example.teammaravillaapp.page

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.model.ProductCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFilter(
    selectedCategories: Set<ProductCategory> = emptySet(),
    onSave: (Set<ProductCategory>) -> Unit,
    onCancel: () -> Unit
) {
    var selected by remember { mutableStateOf(selectedCategories) }

    val allCategories = ProductCategory.entries.toSet()
    val allSelected = selected.size == allCategories.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.category_filter_title)) }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onCancel
                ) {
                    Text(stringResource(R.string.category_filter_cancel))
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { onSave(selected) }
                ) {
                    Text(stringResource(R.string.category_filter_save))
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = if (allSelected)
                    stringResource(R.string.category_filter_subtitle_all)
                else
                    stringResource(R.string.category_filter_subtitle_active),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = {
                    selected = if (allSelected) emptySet() else allCategories
                }
            ) {
                Text(stringResource(R.string.category_filter_show_all))
            }

            Divider()

            ProductCategory.entries.forEach { category ->
                FilterChip(
                    selected = selected.contains(category),
                    onClick = {
                        selected =
                            if (selected.contains(category)) selected - category
                            else selected + category
                    },
                    label = { Text(stringResource(id = category.labelRes)) }
                )
            }
        }
    }
}
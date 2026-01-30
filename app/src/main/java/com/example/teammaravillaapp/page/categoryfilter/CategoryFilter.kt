package com.example.teammaravillaapp.page.categoryfilter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.ui.events.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFilter(
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onUiEvent: (UiEvent) -> Unit,
    vm: CategoryFilterViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(vm) {
        vm.events.collect { onUiEvent(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.category_filter_title)) })
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
                    Text(stringResource(R.string.common_cancel))
                }

                Button(
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isLoading,
                    onClick = { vm.save(onSaved = onSave) }
                ) {
                    Text(stringResource(R.string.common_save))
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

            val subtitle = if (uiState.allSelected)
                stringResource(R.string.category_filter_subtitle_all)
            else
                stringResource(R.string.category_filter_subtitle_active)

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(8.dp))

            TextButton(
                enabled = !uiState.isLoading,
                onClick = { vm.toggleAll() },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(stringResource(R.string.category_filter_show_all))
            }

            Divider()

            ProductCategory.entries.forEach { category ->
                FilterChip(
                    selected = category in uiState.selected,
                    onClick = { vm.toggle(category) },
                    label = { Text(stringResource(id = category.labelRes)) }
                )
            }
        }
    }
}
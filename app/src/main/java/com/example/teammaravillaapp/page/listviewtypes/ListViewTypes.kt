package com.example.teammaravillaapp.page.listviewtypes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.teammaravillaapp.component.ViewTypeOption
import com.example.teammaravillaapp.model.ListViewType
import com.example.teammaravillaapp.ui.events.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListViewTypes(
    onCancel: () -> Unit,
    onSaved: () -> Unit,
    onUiEvent: (UiEvent) -> Unit,
    vm: ListViewTypesViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(vm) {
        vm.events.collect { onUiEvent(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.list_view_types_title)) })
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
                ) { Text(stringResource(R.string.common_cancel)) }

                Button(
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isLoading,
                    onClick = { vm.onSave(onSaved) }
                ) { Text(stringResource(R.string.common_save)) }
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
                text = stringResource(R.string.list_view_types_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(6.dp))

            ViewTypeOption(
                title = stringResource(R.string.list_view_types_bubbles),
                desc = stringResource(R.string.list_view_types_bubbles_desc),
                selected = uiState.selected == ListViewType.BUBBLES,
                onClick = { vm.onSelect(ListViewType.BUBBLES) }
            )

            ViewTypeOption(
                title = stringResource(R.string.list_view_types_list),
                desc = stringResource(R.string.list_view_types_list_desc),
                selected = uiState.selected == ListViewType.LIST,
                onClick = { vm.onSelect(ListViewType.LIST) }
            )

            ViewTypeOption(
                title = stringResource(R.string.list_view_types_compact),
                desc = stringResource(R.string.list_view_types_compact_desc),
                selected = uiState.selected == ListViewType.COMPACT,
                onClick = { vm.onSelect(ListViewType.COMPACT) }
            )
        }
    }
}
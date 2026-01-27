package com.example.teammaravillaapp.page.listviewtypes

import ViewTypeOption
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.model.ListViewType
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListViewTypes(
    current: ListViewType = ListViewType.BUBBLES,
    onCancel: () -> Unit,
    onSave: (ListViewType) -> Unit
) {
    var selected by remember { mutableStateOf(current) }

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
                    onClick = { onSave(selected) }
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
                selected = selected == ListViewType.BUBBLES,
                onClick = { selected = ListViewType.BUBBLES }
            )

            ViewTypeOption(
                title = stringResource(R.string.list_view_types_list),
                desc = stringResource(R.string.list_view_types_list_desc),
                selected = selected == ListViewType.LIST,
                onClick = { selected = ListViewType.LIST }
            )

            ViewTypeOption(
                title = stringResource(R.string.list_view_types_compact),
                desc = stringResource(R.string.list_view_types_compact_desc),
                selected = selected == ListViewType.COMPACT,
                onClick = { selected = ListViewType.COMPACT }
            )
        }
    }
}
package com.example.teammaravillaapp.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.model.QuickActionData

@Composable
fun QuickActionsRow(
    actions: List<QuickActionData>,
    onClick: (QuickActionData) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 2.dp)
    ) {
        items(actions) { action ->
            ElevatedAssistChip(
                onClick = { onClick(action) },
                label = { Text(action.label) },
                leadingIcon = {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = action.label
                    )
                },
                colors = androidx.compose.material3.AssistChipDefaults.elevatedAssistChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            )
        }
    }
}
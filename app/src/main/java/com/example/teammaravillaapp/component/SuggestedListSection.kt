package com.example.teammaravillaapp.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.model.SuggestedList
import com.example.teammaravillaapp.model.SuggestedListData

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SuggestedListSection(
    items: List<SuggestedList> = SuggestedListData.items,
    onPick: (SuggestedList) -> Unit = {}
) {

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.forEach {
            SuggestedListBubble(it) { onPick(it) }
        }
    }
}
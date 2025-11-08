package com.example.teammaravillaapp.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.model.SuggestedList
import com.example.teammaravillaapp.model.SuggestedListData
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Sección con varias **listas sugeridas** en una rejilla fluida.
 *
 * No hace scroll por si sola
 */
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
            SuggestedListBubble(it) {
                onPick(it)
            }
        }
    }
}

@Preview
@Composable
fun PreviewSuggestedListSection() {
    TeamMaravillaAppTheme {
        SuggestedListSection()
    }
}
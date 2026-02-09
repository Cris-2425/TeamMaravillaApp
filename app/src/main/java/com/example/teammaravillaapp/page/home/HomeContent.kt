package com.example.teammaravillaapp.page.home

import RenameListDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.ListCard
import com.example.teammaravillaapp.component.QuickActionsRow
import com.example.teammaravillaapp.component.SearchField
import com.example.teammaravillaapp.component.SectionCard
import com.example.teammaravillaapp.component.SwipeRowActions
import com.example.teammaravillaapp.model.QuickActionData
import com.example.teammaravillaapp.model.SearchFieldData
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    requestFocusSearch: Boolean,
    onFocusSearchConsumed: () -> Unit,
    onSearchChange: (String) -> Unit,
    onNavigateCreateList: () -> Unit,
    onNavigateRecipes: () -> Unit,
    onNavigateHistory: () -> Unit,
    onOpenList: (String) -> Unit,
    onDelete: (String) -> Unit,
    onRename: (String, String) -> Unit,
    onUiEvent: (UiEvent) -> Unit
) {
    var renameTarget by remember { mutableStateOf<Pair<String, String>?>(null) }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(requestFocusSearch) {
        if (requestFocusSearch) {
            // Espera mínima a que compose pinte el TextField
            focusRequester.requestFocus()
            onFocusSearchConsumed()
        }
    }

    val labelNewList = stringResource(R.string.home_quick_new_list)
    val labelFavorites = stringResource(R.string.home_quick_favorites)
    val labelHistory = stringResource(R.string.home_quick_history)

    val quickActions = listOf(
        QuickActionData(Icons.Default.ShoppingCart, labelNewList),
        QuickActionData(Icons.Default.Favorite, labelFavorites),
        QuickActionData(Icons.Default.Create, labelHistory)
    )

    GeneralBackground(modifier = modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = stringResource(R.string.home_title),
                style = MaterialTheme.typography.headlineMedium
            )

            SectionCard {
                SearchField(
                    modifier = Modifier.focusRequester(focusRequester),
                    searchData = SearchFieldData(
                        value = uiState.search,
                        placeholder = stringResource(R.string.home_search_placeholder)
                    ),
                    onValueChange = onSearchChange
                )

                Spacer(Modifier.height(12.dp))

                QuickActionsRow(
                    actions = quickActions,
                    onClick = { action ->
                        when (action.label) {
                            labelNewList -> onNavigateCreateList()
                            labelFavorites -> onNavigateRecipes
                            labelHistory -> onNavigateHistory
                        }
                    }
                )
            }

            SectionCard {
                Text(
                    text = stringResource(R.string.home_recent_lists_title),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(10.dp))

                if (uiState.rows.isEmpty()) {
                    Text(
                        text = stringResource(R.string.home_recent_lists_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        uiState.rows.forEach { row ->
                            val id = row.id
                            val list = row.list
                            val p = row.progress

                            val subtitle = if (p.totalCount == 0) {
                                stringResource(R.string.home_list_subtitle_empty)
                            } else {
                                stringResource(
                                    R.string.home_list_subtitle_progress,
                                    p.checkedCount,
                                    p.totalCount
                                )
                            }

                            SwipeRowActions(
                                id = id,
                                onEdit = { renameTarget = id to list.name },
                                onDelete = { onDelete(id) }
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    ListCard(
                                        cardInfo = com.example.teammaravillaapp.model.CardInfo(
                                            imageID = R.drawable.list_supermarket,
                                            imageDescription = list.name,
                                            title = list.name,
                                            subtitle = subtitle
                                        ),
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = { onOpenList(id) }
                                    )

                                    if (p.totalCount > 0) {
                                        LinearProgressIndicator(
                                            progress = { p.checkedCount.toFloat() / p.totalCount.toFloat() },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        renameTarget?.let { (listId, currentName) ->
            RenameListDialog(
                currentName = currentName,
                onDismiss = { renameTarget = null },
                onConfirm = { newName ->
                    renameTarget = null
                    onRename(listId, newName)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHomeContent() {
    TeamMaravillaAppTheme {
        HomeContent(
            uiState = HomeUiState(
                search = "carne",
                rows = emptyList()
            ),
            requestFocusSearch = false,
            onFocusSearchConsumed = {},
            onSearchChange = {},
            onNavigateCreateList = {},
            onNavigateRecipes = {},
            onNavigateHistory = {},
            onOpenList = {},
            onDelete = {},
            onRename = { _, _ -> },
            onUiEvent = {}
        )
    }
}
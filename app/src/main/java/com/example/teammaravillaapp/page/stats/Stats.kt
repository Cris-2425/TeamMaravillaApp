package com.example.teammaravillaapp.page.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.SectionCard
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.ui.events.UiEvent

@Composable
fun Stats(
    onBack: () -> Unit,
    vm: StatsViewModel = hiltViewModel(),
    onUiEvent: (UiEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        vm.events.collect { onUiEvent(it) }
    }

    val uiState by vm.uiState.collectAsStateWithLifecycle()

    Box(Modifier.fillMaxSize()) {
        GeneralBackground(overlayAlpha = 0.20f) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                item {
                    Title(texto = stringResource(R.string.stats_title))
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.stats_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // --- Banner de loading / error ---
                item {
                    when {
                        uiState.isLoading -> {
                            SectionCard {
                                Text(
                                    text = stringResource(R.string.common_loading),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        uiState.error != null -> {
                            SectionCard {
                                Text(
                                    text = stringResource(R.string.stats_error_generic),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    text = uiState.error?.message.orEmpty(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(Modifier.height(12.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    OutlinedButton(onClick = { vm.refresh() }) {
                                        Text(stringResource(R.string.common_retry))
                                    }
                                }
                            }
                        }
                    }
                }

                // --- Totales (2 columnas) ---
                item {
                    SectionCard {
                        Text(
                            text = stringResource(R.string.stats_totals),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(12.dp))

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatTile(
                                title = stringResource(R.string.stats_lists),
                                value = uiState.lists.toString(),
                                modifier = Modifier.weight(1f)
                            )
                            StatTile(
                                title = stringResource(R.string.stats_products),
                                value = uiState.products.toString(),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatTile(
                                title = stringResource(R.string.stats_recipes),
                                value = uiState.recipes.toString(),
                                modifier = Modifier.weight(1f)
                            )
                            StatTile(
                                title = stringResource(R.string.stats_favorites),
                                value = uiState.favorites.toString(),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatTile(
                                title = stringResource(R.string.stats_items_total),
                                value = uiState.totalItems.toString(),
                                modifier = Modifier.weight(1f)
                            )
                            StatTile(
                                title = stringResource(R.string.stats_items_checked),
                                value = uiState.checkedItems.toString(),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // --- Últimos 7 días ---
                item {
                    SectionCard {
                        Text(
                            text = stringResource(R.string.stats_last7_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(10.dp))

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatTile(
                                title = stringResource(R.string.stats_lists_created),
                                value = uiState.listsLast7Days.toString(),
                                modifier = Modifier.weight(1f)
                            )
                            StatTile(
                                title = stringResource(R.string.stats_items_added),
                                value = uiState.itemsLast7Days.toString(),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // --- Top productos ---
                item {
                    SectionCard {
                        Text(
                            text = stringResource(R.string.stats_top_products),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(10.dp))

                        if (uiState.topProducts.isEmpty()) {
                            Text(
                                text = stringResource(R.string.stats_top_products_empty),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                uiState.topProducts.forEachIndexed { index, row ->
                                    TopProductRow(
                                        rank = index + 1,
                                        name = row.name,
                                        times = row.times
                                    )
                                }
                            }
                        }
                    }
                }
            }

            //Box(Modifier.align(Alignment.BottomStart)) {
            //    BackButton(onClick = onBack)
            //}
        }
    }
}
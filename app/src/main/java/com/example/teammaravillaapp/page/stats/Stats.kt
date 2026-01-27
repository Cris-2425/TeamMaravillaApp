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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.SectionCard
import com.example.teammaravillaapp.component.Title

@Composable
fun Stats(
    onBack: () -> Unit,
    vm: StatsViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()

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

                // --- Últimos 7 días (opcional bonito) ---
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

            Box(Modifier.align(Alignment.BottomStart)) {
                BackButton(onClick = onBack)
            }
        }
    }
}

@Composable
private fun StatTile(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun TopProductRow(
    rank: Int,
    name: String,
    times: Int
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        tonalElevation = 0.dp,
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Text(
                    text = "#$rank",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "×$times",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.SectionCard
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.page.stats.component.StatTile
import com.example.teammaravillaapp.page.stats.component.TopProductRow
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * UI pura de la pantalla de Estadísticas.
 *
 * Esta función NO:
 * - Inyecta ViewModels
 * - Ejecuta lógica de carga
 * - Emite eventos one-shot
 *
 * Únicamente renderiza el [StatsUiState] y expone callbacks para acciones de usuario.
 *
 * @param uiState Flujo de estado observable con los datos de estadísticas.
 * Restricciones:
 * - No nulo.
 * - Debe emitir un estado inicial razonable para evitar pantallas vacías.
 * @param onRetry Acción del usuario para reintentar la carga cuando hay error.
 * Restricciones:
 * - No nulo.
 * - Debe ser segura ante múltiples llamadas (idempotente o con control de concurrencia).
 * @param onBack Acción de navegación hacia atrás (opcional si no hay botón visible).
 * Restricciones:
 * - No nulo.
 *
 * @throws IllegalStateException No se lanza directamente. Si [uiState] emite valores inconsistentes
 * (por ejemplo, isLoading=false y error!=null pero sin texto), el UI podría mostrar información incompleta.
 *
 * @see Stats Contenedor.
 * @see StatsViewModel Fuente del estado.
 *
 * Ejemplo de uso:
 * {@code
 * StatsContent(
 *   uiState = vm.uiState,
 *   onRetry = vm::refresh,
 *   onBack = { navController.popBackStack() }
 * )
 * }
 */
@Composable
fun StatsContent(
    uiState: StateFlow<StatsUiState>,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    val state by uiState.collectAsStateWithLifecycle()

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

                // Banner de loading / error (solo si aplica)
                item {
                    when {
                        state.isLoading -> {
                            SectionCard {
                                Text(
                                    text = stringResource(R.string.common_loading),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        state.error != null -> {
                            SectionCard {
                                Text(
                                    text = stringResource(R.string.stats_error_generic),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    text = state.error?.message.orEmpty(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(Modifier.height(12.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    OutlinedButton(onClick = onRetry) {
                                        Text(stringResource(R.string.common_retry))
                                    }
                                }
                            }
                        }
                    }
                }

                // Totales (siempre visibles aunque haya error → así la UI no “salta” tanto)
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
                                value = state.lists.toString(),
                                modifier = Modifier.weight(1f)
                            )
                            StatTile(
                                title = stringResource(R.string.stats_products),
                                value = state.products.toString(),
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
                                value = state.recipes.toString(),
                                modifier = Modifier.weight(1f)
                            )
                            StatTile(
                                title = stringResource(R.string.stats_favorites),
                                value = state.favorites.toString(),
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
                                value = state.totalItems.toString(),
                                modifier = Modifier.weight(1f)
                            )
                            StatTile(
                                title = stringResource(R.string.stats_items_checked),
                                value = state.checkedItems.toString(),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Últimos 7 días
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
                                value = state.listsLast7Days.toString(),
                                modifier = Modifier.weight(1f)
                            )
                            StatTile(
                                title = stringResource(R.string.stats_items_added),
                                value = state.itemsLast7Days.toString(),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Top productos
                item {
                    SectionCard {
                        Text(
                            text = stringResource(R.string.stats_top_products),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(10.dp))

                        if (state.topProducts.isEmpty()) {
                            Text(
                                text = stringResource(R.string.stats_top_products_empty),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                state.topProducts.forEachIndexed { index, row ->
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
        }
    }
}
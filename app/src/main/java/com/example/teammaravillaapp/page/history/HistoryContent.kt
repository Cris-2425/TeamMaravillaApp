package com.example.teammaravillaapp.page.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.ListCard
import com.example.teammaravillaapp.component.SectionCard
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.model.CardInfo

/**
 * Render “UI pura” del historial de listas recientes.
 *
 * Esta función:
 * - No accede a ViewModel.
 * - No lanza efectos (LaunchedEffect / collect).
 * - Solo pinta el estado y expone callbacks.
 *
 * Motivo: cumplir separación “contenedor vs presentación” y habilitar @Preview sin Hilt.
 *
 * @param uiState Estado de pantalla (loading + filas).
 * Restricciones:
 * - No nulo.
 * - Si [HistoryUiState.isLoading] es true, se prioriza la vista de carga.
 * @param onBack Acción de navegación hacia atrás.
 * @param onOpenList Acción de abrir el detalle de una lista por id.
 * Restricciones:
 * - El id recibido debe ser no vacío.
 * @param onClearAll Acción de borrar todo el historial.
 * @param onRemove Acción para eliminar una sola entrada del historial.
 *
 * @throws IllegalArgumentException No se lanza directamente, pero se recomienda que el caller
 * no invoque [onOpenList] con ids vacíos.
 *
 * @see HistoryUiState Modelo de estado de la pantalla.
 * @see HistoryRow Modelo de fila.
 *
 * Ejemplo de uso:
 * {@code
 * HistoryContent(
 *   uiState = uiState,
 *   onOpenList = { id -> navToDetail(id) },
 *   onClearAll = viewModel::onClearAll,
 *   onRemove = viewModel::onRemove
 * )
 * }
 */
@Composable
fun HistoryContent(
    uiState: HistoryUiState,
    onBack: () -> Unit,
    onOpenList: (String) -> Unit,
    onClearAll: () -> Unit,
    onRemove: (String) -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        GeneralBackground(overlayAlpha = 0.20f) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Title(texto = stringResource(R.string.history_title))

                SectionCard {
                    when {
                        uiState.isLoading -> {
                            Text(
                                text = stringResource(R.string.common_loading),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        uiState.rows.isEmpty() -> {
                            Text(
                                text = stringResource(R.string.history_empty),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        else -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = onClearAll) {
                                    Text(stringResource(R.string.history_clear))
                                }
                            }

                            Spacer(Modifier.height(8.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                uiState.rows.forEach { row ->
                                    ListCard(
                                        cardInfo = CardInfo(
                                            imageID = R.drawable.list_supermarket,
                                            imageDescription = row.name,
                                            title = row.name,
                                            subtitle = stringResource(R.string.history_open_again)
                                        ),
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = { onOpenList(row.id) }
                                    )

                                    TextButton(onClick = { onRemove(row.id) }) {
                                        Text(stringResource(R.string.history_remove))
                                    }
                                }
                            }
                        }
                    }
                }
                // TextButton(onClick = onBack) { Text(stringResource(R.string.common_back)) }
            }
        }
    }
}
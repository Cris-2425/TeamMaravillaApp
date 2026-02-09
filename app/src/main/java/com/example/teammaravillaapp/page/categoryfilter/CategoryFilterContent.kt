package com.example.teammaravillaapp.page.categoryfilter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.model.ProductCategory

/**
 * UI pura de la pantalla **CategoryFilter**.
 *
 * Motivo:
 * - Facilita previews.
 * - Evita dependencias a Hilt/Nav.
 * - Separa presentación (Compose) de lógica (ViewModel).
 *
 * @param uiState Estado actual de la pantalla. No debe ser nulo.
 * @param onSave Acción al pulsar Guardar. Se asume que persiste fuera (VM) o navega.
 * @param onCancel Acción al pulsar Cancelar.
 * @param onToggleAll Alterna seleccionar todo / limpiar todo.
 * @param onToggle Alterna una categoría individual.
 *
 * Ejemplo de uso:
 * {@code
 * CategoryFilterContent(
 *   uiState = state,
 *   onSave = { vm.onSave { nav.popBackStack() } },
 *   onCancel = { nav.popBackStack() },
 *   onToggleAll = vm::onToggleAll,
 *   onToggle = vm::onToggle
 * )
 * }
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFilterContent(
    uiState: CategoryFilterUiState,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onToggleAll: () -> Unit,
    onToggle: (ProductCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val allSelected = uiState.allSelected

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.category_filter_title)) })
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
                    onClick = onCancel,
                    enabled = !uiState.isLoading
                ) {
                    Text(stringResource(R.string.category_filter_cancel))
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onSave,
                    enabled = !uiState.isLoading
                ) {
                    Text(stringResource(R.string.category_filter_save))
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (uiState.isLoading) {
                Text(
                    text = stringResource(R.string.common_loading),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                return@Column
            }

            Text(
                text = if (allSelected)
                    stringResource(R.string.category_filter_subtitle_all)
                else
                    stringResource(R.string.category_filter_subtitle_active),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(8.dp))

            TextButton(onClick = onToggleAll) {
                Text(stringResource(R.string.category_filter_show_all))
            }

            Divider()

            ProductCategory.entries.forEach { category ->
                FilterChip(
                    selected = category in uiState.selected,
                    onClick = { onToggle(category) },
                    label = { Text(stringResource(id = category.labelRes)) }
                )
            }
        }
    }
}
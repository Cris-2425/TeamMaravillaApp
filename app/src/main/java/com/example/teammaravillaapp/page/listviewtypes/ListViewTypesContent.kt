package com.example.teammaravillaapp.page.listviewtypes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.ViewTypeOption
import com.example.teammaravillaapp.model.ListViewType

/**
 * Render “UI pura” para la selección del tipo de vista de una lista.
 *
 * Esta función NO:
 * - accede a ViewModel
 * - recolecta Flows
 * - lanza efectos
 *
 * Solo consume [ListViewTypesUiState] y emite acciones mediante callbacks.
 *
 * @param uiState Estado de UI (loading + selección actual).
 * Restricciones:
 * - No nulo.
 * - Si [ListViewTypesUiState.isLoading] es true, el botón Guardar debe deshabilitarse.
 * @param onCancel Acción de cancelar/cerrar sin guardar.
 * @param onSelect Acción para seleccionar un tipo de vista.
 * Restricciones:
 * - Recibe un [ListViewType] válido (enum).
 * @param onSave Acción de guardar cambios.
 * Restricciones:
 * - Debe persistir la preferencia en una capa superior (VM/UseCase).
 *
 * @throws IllegalStateException No se lanza directamente aquí, pero se recomienda que [onSave]
 * no se ejecute cuando [uiState.isLoading] sea true (la UI ya lo evita con `enabled`).
 *
 * @see ListViewTypesUiState Modelo de estado.
 * @see ViewTypeOption Componente de opción seleccionable.
 *
 * Ejemplo de uso:
 * {@code
 * ListViewTypesContent(
 *   uiState = uiState,
 *   onCancel = navController::popBackStack,
 *   onSelect = vm::onSelect,
 *   onSave = { vm.onSave { navController.popBackStack() } }
 * )
 * }
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListViewTypesContent(
    uiState: ListViewTypesUiState,
    onCancel: () -> Unit,
    onSelect: (ListViewType) -> Unit,
    onSave: () -> Unit
) {
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
                    enabled = !uiState.isLoading,
                    onClick = onSave
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
                selected = uiState.selected == ListViewType.BUBBLES,
                onClick = { onSelect(ListViewType.BUBBLES) }
            )

            ViewTypeOption(
                title = stringResource(R.string.list_view_types_list),
                desc = stringResource(R.string.list_view_types_list_desc),
                selected = uiState.selected == ListViewType.LIST,
                onClick = { onSelect(ListViewType.LIST) }
            )

            ViewTypeOption(
                title = stringResource(R.string.list_view_types_compact),
                desc = stringResource(R.string.list_view_types_compact_desc),
                selected = uiState.selected == ListViewType.COMPACT,
                onClick = { onSelect(ListViewType.COMPACT) }
            )
        }
    }
}
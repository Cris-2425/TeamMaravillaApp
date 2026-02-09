package com.example.teammaravillaapp.page.listdetail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Cabecera del detalle de lista: título + menú de opciones (More).
 *
 * Componente de presentación: no gestiona estado interno del menú, lo recibe por parámetro
 * ([menuExpanded]) y lo modifica mediante [onMenuExpandedChange] (state hoisting).
 *
 * @param title Título de la lista.
 * @param isCategoryFilterActive Indica si hay filtro por categorías activo (habilita "limpiar filtro").
 * @param anyChecked Indica si hay productos marcados (habilita "eliminar marcados" y "desmarcar").
 * @param canClear Indica si la lista puede vaciarse (habilita "vaciar lista").
 * @param menuExpanded Estado del desplegable del menú.
 * @param onMenuExpandedChange Callback para abrir/cerrar el menú.
 * @param onOpenCategoryFilter Abre el selector de categorías.
 * @param onClearCategoryFilter Limpia el filtro por categorías.
 * @param onRemoveChecked Elimina los elementos marcados.
 * @param onUncheckAll Desmarca todos los elementos.
 * @param onOpenClearListConfirm Abre el diálogo de confirmación para vaciar la lista.
 * @param onOpenListViewTypes Abre el selector de tipo de vista.
 * @param modifier Modificador de Compose para controlar layout.
 *
 * Ejemplo de uso:
 * {@code
 * ListDetailHeader(
 *   title = uiState.title,
 *   isCategoryFilterActive = uiState.selectedCategories.isNotEmpty(),
 *   anyChecked = uiState.items.any { it.checked },
 *   canClear = uiState.items.isNotEmpty(),
 *   menuExpanded = uiState.menuExpanded,
 *   onMenuExpandedChange = viewModel::onMenuExpandedChange,
 *   onOpenCategoryFilter = viewModel::onOpenCategoryFilter,
 *   onClearCategoryFilter = viewModel::onClearCategoryFilter,
 *   onRemoveChecked = viewModel::onRemoveChecked,
 *   onUncheckAll = viewModel::onUncheckAll,
 *   onOpenClearListConfirm = viewModel::onOpenClearListConfirm,
 *   onOpenListViewTypes = viewModel::onOpenListViewTypes
 * )
 * }
 */
@Composable
internal fun ListDetailHeader(
    title: String,
    isCategoryFilterActive: Boolean,
    anyChecked: Boolean,
    canClear: Boolean,
    menuExpanded: Boolean,
    onMenuExpandedChange: (Boolean) -> Unit,
    onOpenCategoryFilter: () -> Unit,
    onClearCategoryFilter: () -> Unit,
    onRemoveChecked: () -> Unit,
    onUncheckAll: () -> Unit,
    onOpenClearListConfirm: () -> Unit,
    onOpenListViewTypes: () -> Unit,
    modifier: Modifier = Modifier
) {
    Spacer(Modifier.height(8.dp))

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.weight(1f)) { Title(texto = title) }

        Box {
            IconButton(onClick = { onMenuExpandedChange(true) }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.list_detail_menu_cd)
                )
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { onMenuExpandedChange(false) }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.list_detail_menu_category_filter)) },
                    onClick = {
                        onMenuExpandedChange(false)
                        onOpenCategoryFilter()
                    }
                )

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.list_detail_menu_clear_category_filter)) },
                    enabled = isCategoryFilterActive,
                    onClick = {
                        onMenuExpandedChange(false)
                        onClearCategoryFilter()
                    }
                )

                Divider()

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.list_detail_menu_remove_checked)) },
                    enabled = anyChecked,
                    onClick = {
                        onMenuExpandedChange(false)
                        onRemoveChecked()
                    }
                )

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.list_detail_menu_uncheck_all)) },
                    enabled = anyChecked,
                    onClick = {
                        onMenuExpandedChange(false)
                        onUncheckAll()
                    }
                )

                Divider()

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.list_detail_menu_clear_list)) },
                    enabled = canClear,
                    onClick = {
                        onMenuExpandedChange(false)
                        onOpenClearListConfirm()
                    }
                )

                Divider()

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.list_detail_menu_view_type)) },
                    onClick = {
                        onMenuExpandedChange(false)
                        onOpenListViewTypes()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListDetailHeaderPreview() {
    TeamMaravillaAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ListDetailHeader(
                title = "Compra semanal",
                isCategoryFilterActive = true,
                anyChecked = true,
                canClear = true,
                menuExpanded = true, // para ver el menú en preview
                onMenuExpandedChange = {},
                onOpenCategoryFilter = {},
                onClearCategoryFilter = {},
                onRemoveChecked = {},
                onUncheckAll = {},
                onOpenClearListConfirm = {},
                onOpenListViewTypes = {}
            )
        }
    }
}
package com.example.teammaravillaapp.page.listdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.data.seed.ListBackgrounds
import com.example.teammaravillaapp.page.listdetail.component.ActiveFilterChip
import com.example.teammaravillaapp.page.listdetail.component.CategorySection
import com.example.teammaravillaapp.page.listdetail.component.ClearListDialog
import com.example.teammaravillaapp.page.listdetail.component.CurrentProductsTitle
import com.example.teammaravillaapp.page.listdetail.component.EmptyCurrentProductsCard
import com.example.teammaravillaapp.page.listdetail.component.ItemsContainer
import com.example.teammaravillaapp.page.listdetail.component.ListDetailHeader
import com.example.teammaravillaapp.page.listdetail.component.RecentUsedSection
import com.example.teammaravillaapp.page.listdetail.component.RecentUsedTitle
import com.example.teammaravillaapp.page.listdetail.component.SearchSection
import kotlin.collections.component1
import kotlin.collections.component2

/**
 * UI pura de la pantalla ListDetail.
 *
 * **Presentación**:
 * - No conoce ViewModel.
 * - No conoce repositorios.
 * - Pinta únicamente en base a [uiState].
 * - Emite intenciones/acciones mediante callbacks.
 *
 * Estado UI local permitido:
 * - Menús, diálogos, flags visuales (ej. `menuExpanded`, `confirmClearExpanded`).
 *
 * @param uiState Estado completo de la pantalla.
 * @param onOpenCategoryFilter Acción para abrir el selector de categorías.
 * @param onOpenListViewTypes Acción para abrir el selector de tipo de vista.
 * @param onClearCategoryFilter Limpia el filtro de categorías persistido.
 * @param onAction Dispatcher de acciones del usuario (intenciones).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDetailContent(
    uiState: ListDetailUiState,
    onOpenCategoryFilter: () -> Unit = {},
    onOpenListViewTypes: () -> Unit = {},
    onClearCategoryFilter: () -> Unit = {},
    onAction: (ListDetailAction) -> Unit = {}
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var confirmClearExpanded by remember { mutableStateOf(false) }

    if (uiState.isEmptyState) {
        GeneralBackground(bgRes = null, overlayAlpha = 0.18f) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.list_detail_empty_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.list_detail_empty_subtitle),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        return
    }

    val userList = uiState.userList!!
    val bgRes = ListBackgrounds.getBackgroundRes(userList.background)
    val isCategoryFilterActive = uiState.selectedCategories.isNotEmpty()

    GeneralBackground(bgRes = bgRes, overlayAlpha = 0.22f) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            item {
                if (isCategoryFilterActive) {
                    Column {
                        ActiveFilterChip(
                            selectedCategories = uiState.selectedCategories,
                            onChange = onOpenCategoryFilter,
                            onClear = onClearCategoryFilter
                        )
                        Spacer(Modifier.height(4.dp))
                    }
                }
            }

            item {
                ListDetailHeader(
                    title = userList.name,
                    isCategoryFilterActive = isCategoryFilterActive,
                    anyChecked = uiState.anyChecked,
                    canClear = uiState.canClear,
                    menuExpanded = menuExpanded,
                    onMenuExpandedChange = { menuExpanded = it },
                    onOpenCategoryFilter = onOpenCategoryFilter,
                    onClearCategoryFilter = onClearCategoryFilter,
                    onRemoveChecked = { onAction(ListDetailAction.RemoveChecked) },
                    onUncheckAll = { onAction(ListDetailAction.UncheckAll) },
                    onOpenClearListConfirm = { confirmClearExpanded = true },
                    onOpenListViewTypes = onOpenListViewTypes
                )
            }

            item {
                SearchSection(
                    query = uiState.query,
                    isLoadingCatalog = uiState.isLoadingCatalog,
                    searchResults = uiState.searchResults,
                    onQueryChanged = { onAction(ListDetailAction.QueryChanged(it)) },
                    onAddFromSearch = { productId ->
                        onAction(ListDetailAction.AddProduct(productId))
                        onAction(ListDetailAction.QueryChanged(""))
                    }
                )
            }

            item { CurrentProductsTitle() }

            item {
                if (uiState.items.isEmpty()) {
                    EmptyCurrentProductsCard()
                } else {
                    ItemsContainer(
                        viewType = uiState.viewType,
                        items = uiState.items,
                        onToggleChecked = { onAction(ListDetailAction.ToggleChecked(it)) },
                        onRemove = { onAction(ListDetailAction.RemoveProduct(it)) },
                        onDec = { onAction(ListDetailAction.DecQuantity(it)) },
                        onInc = { onAction(ListDetailAction.IncQuantity(it)) }
                    )
                }
            }

            item { RecentUsedTitle() }

            item {
                RecentUsedSection(
                    isLoadingCatalog = uiState.isLoadingCatalog,
                    recentAvailable = uiState.recentAvailable,
                    catalogError = uiState.catalogError,
                    onAdd = { onAction(ListDetailAction.AddProduct(it)) }
                )
            }

            uiState.availableByCategory.forEach { (category, available) ->
                item {
                    Text(
                        text = stringResource(id = category.labelRes),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                item {
                    CategorySection(
                        products = available,
                        onAdd = { onAction(ListDetailAction.AddProduct(it)) }
                    )
                }
            }
        }

        if (confirmClearExpanded) {
            ClearListDialog(
                onDismiss = { confirmClearExpanded = false },
                onConfirm = {
                    confirmClearExpanded = false
                    onAction(ListDetailAction.ClearList)
                }
            )
        }
    }
}
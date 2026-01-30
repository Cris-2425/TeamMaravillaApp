package com.example.teammaravillaapp.page.listdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.data.seed.ListBackgrounds
import com.example.teammaravillaapp.ui.events.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDetail(
    onBack: () -> Unit = {},
    onOpenCategoryFilter: () -> Unit = {},
    onOpenListViewTypes: () -> Unit = {},
    onUiEvent: (UiEvent) -> Unit
) {
    val vm: ListDetailViewModel = hiltViewModel()
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(vm) {
        vm.events.collect { onUiEvent(it) }
    }

    var menuExpanded by remember { mutableStateOf(false) }
    var confirmClearExpanded by remember { mutableStateOf(false) }

    if (uiState.isEmptyState) {
        GeneralBackground(bgRes = null, overlayAlpha = 0.18f) {
            Column(
                Modifier.fillMaxSize().padding(16.dp),
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

            Box(Modifier.align(Alignment.BottomStart)) { BackButton(onClick = onBack) }
        }
        return
    }

    val userList = uiState.userList!!
    val bgRes = ListBackgrounds.getBackgroundRes(userList.background)
    val isCategoryFilterActive = uiState.selectedCategories.isNotEmpty()

    GeneralBackground(bgRes = bgRes, overlayAlpha = 0.22f) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            item {
                if (isCategoryFilterActive) {
                    Column {
                        ActiveFilterChip(
                            selectedCategories = uiState.selectedCategories,
                            onChange = onOpenCategoryFilter,
                            onClear = { vm.clearCategoryFilter() }
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
                    onClearCategoryFilter = { vm.clearCategoryFilter() },
                    onRemoveChecked = { vm.onAction(ListDetailAction.RemoveChecked) },
                    onUncheckAll = { vm.onAction(ListDetailAction.UncheckAll) },
                    onOpenClearListConfirm = { confirmClearExpanded = true },
                    onOpenListViewTypes = onOpenListViewTypes
                )
            }

            item {
                SearchSection(
                    query = uiState.query,
                    isLoadingCatalog = uiState.isLoadingCatalog,
                    searchResults = uiState.searchResults,
                    onQueryChanged = { vm.onAction(ListDetailAction.QueryChanged(it)) },
                    onAddFromSearch = { productId ->
                        vm.onAction(ListDetailAction.AddProduct(productId))
                        vm.onAction(ListDetailAction.QueryChanged(""))
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
                        onToggleChecked = { vm.onAction(ListDetailAction.ToggleChecked(it)) },
                        onRemove = { vm.onAction(ListDetailAction.RemoveProduct(it)) },
                        onDec = { vm.onAction(ListDetailAction.DecQuantity(it)) },
                        onInc = { vm.onAction(ListDetailAction.IncQuantity(it)) }
                    )
                }
            }

            item { RecentUsedTitle() }

            item {
                RecentUsedSection(
                    isLoadingCatalog = uiState.isLoadingCatalog,
                    recentAvailable = uiState.recentAvailable,
                    catalogError = uiState.catalogError,
                    onAdd = { vm.onAction(ListDetailAction.AddProduct(it)) }
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
                        onAdd = { vm.onAction(ListDetailAction.AddProduct(it)) }
                    )
                }
            }
        }

        if (confirmClearExpanded) {
            ClearListDialog(
                onDismiss = { confirmClearExpanded = false },
                onConfirm = {
                    confirmClearExpanded = false
                    vm.onAction(ListDetailAction.ClearList)
                }
            )
        }

        //Box(Modifier.align(Alignment.BottomStart)) {
        //    BackButton(onClick = onBack)
        //}
    }
}
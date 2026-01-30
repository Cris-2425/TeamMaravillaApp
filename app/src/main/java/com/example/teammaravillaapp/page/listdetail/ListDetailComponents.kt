package com.example.teammaravillaapp.page.listdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.ProductBubble
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.model.ListViewType
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory

@Composable
internal fun ActiveFilterChip(
    selectedCategories: Set<ProductCategory>,
    onChange: () -> Unit,
    onClear: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val labels = selectedCategories.map { stringResource(it.labelRes) }

            val text = when {
                labels.isEmpty() -> ""
                labels.size <= 3 -> stringResource(
                    R.string.list_detail_filter_chip,
                    labels.joinToString(", ")
                )
                else -> stringResource(
                    R.string.list_detail_filter_chip_more,
                    labels.take(3).joinToString(", "),
                    labels.size - 3
                )
            }

            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f),
                maxLines = 1
            )

            TextButton(onClick = onChange, contentPadding = PaddingValues(horizontal = 10.dp)) {
                Text(stringResource(R.string.common_change))
            }
            TextButton(onClick = onClear, contentPadding = PaddingValues(horizontal = 10.dp)) {
                Text(stringResource(R.string.common_remove))
            }
        }
    }
}

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
    onOpenListViewTypes: () -> Unit
) {
    Spacer(Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.weight(1f)) { Title(title) }

        Box {
            IconButton(onClick = { onMenuExpandedChange(true) }) {
                Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.list_detail_menu_cd))
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

@Composable
internal fun SearchSection(
    query: String,
    isLoadingCatalog: Boolean,
    searchResults: List<Product>,
    onQueryChanged: (String) -> Unit,
    onAddFromSearch: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(stringResource(R.string.list_detail_search_placeholder)) },
            trailingIcon = {
                if (query.isNotBlank()) {
                    IconButton(onClick = { onQueryChanged("") }) {
                        Icon(Icons.Default.Close, contentDescription = stringResource(R.string.common_clear))
                    }
                }
            }
        )

        if (query.isNotBlank()) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Column(
                    Modifier.fillMaxWidth().padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    when {
                        isLoadingCatalog -> Text(
                            text = stringResource(R.string.list_detail_search_loading),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        searchResults.isEmpty() -> Text(
                            text = stringResource(R.string.list_detail_search_no_results),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        else -> {
                            Text(
                                text = stringResource(R.string.list_detail_search_results_title),
                                style = MaterialTheme.typography.titleSmall
                            )

                            @OptIn(ExperimentalLayoutApi::class)
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                searchResults.forEach { product ->
                                    ProductBubble(
                                        product = product,
                                        onClick = { onAddFromSearch(product.id) }
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

@Composable
internal fun CurrentProductsTitle() {
    Text(
        text = stringResource(R.string.list_detail_current_products_title),
        style = MaterialTheme.typography.titleSmall
    )
}

@Composable
internal fun EmptyCurrentProductsCard() {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Box(
            Modifier.fillMaxWidth().heightIn(min = 80.dp).padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.list_detail_no_products),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
internal fun ItemsContainer(
    viewType: ListViewType,
    items: List<ListItemUi>,
    onToggleChecked: (String) -> Unit,
    onRemove: (String) -> Unit,
    onDec: (String) -> Unit,
    onInc: (String) -> Unit
) {
    // Un solo contenedor (sin duplicación de Surface)
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(
                when (viewType) {
                    ListViewType.COMPACT -> 10.dp
                    else -> 12.dp
                }
            ),
            verticalArrangement = Arrangement.spacedBy(
                when (viewType) {
                    ListViewType.COMPACT -> 8.dp
                    else -> 10.dp
                }
            )
        ) {
            items.forEach { item ->
                ListItemRow(
                    viewType = viewType,
                    item = item,
                    onToggleChecked = onToggleChecked,
                    onRemove = onRemove,
                    onDec = onDec,
                    onInc = onInc
                )
            }
        }
    }
}

@Composable
internal fun ListItemRow(
    viewType: ListViewType,
    item: ListItemUi,
    onToggleChecked: (String) -> Unit,
    onRemove: (String) -> Unit,
    onDec: (String) -> Unit,
    onInc: (String) -> Unit
) {
    val pid = item.product.id

    when (viewType) {
        ListViewType.BUBBLES -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Checkbox(checked = item.checked, onCheckedChange = { onToggleChecked(pid) })

                ProductBubble(product = item.product, onClick = { onRemove(pid) })

                Spacer(Modifier.weight(1f))

                QuantityControls(
                    quantity = item.quantity,
                    onDec = { onDec(pid) },
                    onInc = { onInc(pid) }
                )
            }
        }

        ListViewType.LIST -> {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Checkbox(checked = item.checked, onCheckedChange = { onToggleChecked(pid) })

                    ProductBubble(
                        product = item.product,
                        modifier = Modifier.width(72.dp),
                        onClick = { onRemove(pid) }
                    )

                    Text(
                        text = item.product.name,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.weight(1f),
                        maxLines = 1
                    )

                    QuantityControls(
                        quantity = item.quantity,
                        onDec = { onDec(pid) },
                        onInc = { onInc(pid) }
                    )
                }
            }
        }

        ListViewType.COMPACT -> {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Checkbox(checked = item.checked, onCheckedChange = { onToggleChecked(pid) })

                    Text(
                        text = item.product.name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f),
                        maxLines = 1
                    )

                    QuantityControls(
                        quantity = item.quantity,
                        onDec = { onDec(pid) },
                        onInc = { onInc(pid) }
                    )
                }
            }
        }
    }
}

@Composable
internal fun QuantityControls(
    quantity: Int,
    onDec: () -> Unit,
    onInc: () -> Unit
) {
    IconButton(onClick = onDec) {
        Icon(Icons.Default.Remove, contentDescription = stringResource(R.string.common_minus))
    }
    Text(text = quantity.toString(), style = MaterialTheme.typography.titleMedium)
    IconButton(onClick = onInc) {
        Icon(Icons.Default.Add, contentDescription = stringResource(R.string.common_plus))
    }
}

@Composable
internal fun RecentUsedTitle() {
    Text(
        text = stringResource(R.string.list_detail_recent_used_title),
        style = MaterialTheme.typography.titleSmall
    )
}

@Composable
internal fun RecentUsedSection(
    isLoadingCatalog: Boolean,
    recentAvailable: List<Product>,
    catalogError: String?,
    onAdd: (String) -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        @OptIn(ExperimentalLayoutApi::class)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            when {
                isLoadingCatalog -> Text(
                    text = stringResource(R.string.list_detail_catalog_loading),
                    style = MaterialTheme.typography.bodyMedium
                )

                recentAvailable.isEmpty() -> Text(
                    text = catalogError ?: stringResource(R.string.list_detail_catalog_empty),
                    style = MaterialTheme.typography.bodyMedium
                )

                else -> recentAvailable.forEach { product ->
                    ProductBubble(product = product, onClick = { onAdd(product.id) })
                }
            }
        }
    }
}

@Composable
internal fun CategorySection(
    products: List<Product>,
    onAdd: (String) -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        @OptIn(ExperimentalLayoutApi::class)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            products.forEach { product ->
                ProductBubble(product = product, onClick = { onAdd(product.id) })
            }
        }
    }
}

@Composable
internal fun ClearListDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.list_detail_clear_confirm_title)) },
        text = { Text(stringResource(R.string.list_detail_clear_confirm_body)) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(R.string.list_detail_clear_confirm_yes))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(stringResource(R.string.list_detail_clear_confirm_no))
            }
        }
    )
}
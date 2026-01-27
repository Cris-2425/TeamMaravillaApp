package com.example.teammaravillaapp.page.listdetail

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.ProductBubble
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.data.prefs.CategoryFilterPrefs
import com.example.teammaravillaapp.data.prefs.ListViewTypePrefs
import com.example.teammaravillaapp.data.seed.ListBackgrounds
import com.example.teammaravillaapp.model.ListViewType
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.util.TAG_GLOBAL
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDetail(
    onBack: () -> Unit = {},
    onOpenCategoryFilter: () -> Unit = {},
    onOpenListViewTypes : () -> Unit = {}
) {
    val vm: ListDetailViewModel = hiltViewModel()
    val uiState by vm.uiState.collectAsState()

    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    // ----------- Preferencias: filtro categorías + tipo de vista -----------
    val selectedCategories by CategoryFilterPrefs.observe(ctx).collectAsState(initial = emptySet())
    val isCategoryFilterActive = selectedCategories.isNotEmpty()

    val viewType by ListViewTypePrefs.observe(ctx).collectAsState(initial = ListViewType.BUBBLES)

    fun categoryAllowed(cat: ProductCategory?): Boolean {
        val c = cat ?: ProductCategory.OTHER
        return !isCategoryFilterActive || c in selectedCategories
    }

    fun clearCategoryFilter() {
        scope.launch { CategoryFilterPrefs.clear(ctx) }
    }

    // ----------- UI: búsqueda local -----------
    var query by remember { mutableStateOf("") }

    // ----------- Menú contextual -----------
    var menuExpanded by remember { mutableStateOf(false) }
    var confirmClearExpanded by remember { mutableStateOf(false) }

    when {
        uiState.isEmptyState -> {
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

                Box(Modifier.align(Alignment.BottomStart)) {
                    BackButton(onClick = onBack)
                }
            }
        }

        else -> {
            val userList = uiState.userList!!
            val bgRes = ListBackgrounds.getBackgroundRes(userList.background)
            val inListIds = uiState.inListIds

            val anyChecked = remember(uiState.itemMeta) { uiState.itemMeta.values.any { it.checked } }
            val canClear = remember(uiState.productsInList) { uiState.productsInList.isNotEmpty() }

            // Resultados: catálogo - los que ya están en la lista - respeta filtro categorías - respeta query
            val normalizedQuery = query.trim()
            val searchResults = remember(uiState.catalog, inListIds, normalizedQuery, selectedCategories) {
                if (normalizedQuery.isBlank()) emptyList()
                else {
                    uiState.catalog
                        .asSequence()
                        .filter { it.id !in inListIds }
                        .filter { p -> categoryAllowed(p.category) }
                        .filter { p ->
                            p.name.contains(normalizedQuery, ignoreCase = true) ||
                                    p.id.contains(normalizedQuery, ignoreCase = true)
                        }
                        .take(20)
                        .toList()
                }
            }

            GeneralBackground(bgRes = bgRes, overlayAlpha = 0.22f) {

                // ---- Chip resumen de filtro activo ----
                if (isCategoryFilterActive) {
                    Surface(
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val categoryLabels = selectedCategories.map { stringResource(it.labelRes) }

                            val text = when {
                                categoryLabels.isEmpty() -> ""
                                categoryLabels.size <= 3 -> "Filtro: " + categoryLabels.joinToString(", ")
                                else -> {
                                    val shown = categoryLabels.take(3).joinToString(", ")
                                    val more = categoryLabels.size - 3
                                    "Filtro: $shown +$more"
                                }
                            }

                            Text(
                                text = text,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.weight(1f),
                                maxLines = 1
                            )

                            TextButton(
                                onClick = onOpenCategoryFilter,
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                            ) { Text("Cambiar") }

                            TextButton(
                                onClick = { clearCategoryFilter() },
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                            ) { Text("Quitar") }
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 120.dp)
                ) {

                    // ------------------ Header ------------------
                    item {
                        Spacer(Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(Modifier.weight(1f)) { Title(userList.name) }

                            Box {
                                IconButton(onClick = { menuExpanded = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = stringResource(R.string.list_detail_menu_cd)
                                    )
                                }

                                DropdownMenu(
                                    expanded = menuExpanded,
                                    onDismissRequest = { menuExpanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.list_detail_menu_category_filter)) },
                                        onClick = {
                                            menuExpanded = false
                                            onOpenCategoryFilter()
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.list_detail_menu_clear_category_filter)) },
                                        enabled = isCategoryFilterActive,
                                        onClick = {
                                            menuExpanded = false
                                            clearCategoryFilter()
                                        }
                                    )

                                    Divider()

                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.list_detail_menu_remove_checked)) },
                                        enabled = anyChecked,
                                        onClick = {
                                            menuExpanded = false
                                            vm.removeCheckedItems()
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.list_detail_menu_uncheck_all)) },
                                        enabled = anyChecked,
                                        onClick = {
                                            menuExpanded = false
                                            vm.uncheckAll()
                                        }
                                    )

                                    Divider()

                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.list_detail_menu_clear_list)) },
                                        enabled = canClear,
                                        onClick = {
                                            menuExpanded = false
                                            confirmClearExpanded = true
                                        }
                                    )

                                    Divider()

                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.list_detail_menu_view_type)) },
                                        onClick = {
                                            menuExpanded = false
                                            onOpenListViewTypes()
                                        }
                                    )
                                    Divider()

                                }
                            }
                        }
                    }

                    // ------------------ Buscador ------------------
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = query,
                                onValueChange = { query = it },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                placeholder = { Text(stringResource(R.string.list_detail_search_placeholder)) },
                                trailingIcon = {
                                    if (query.isNotBlank()) {
                                        IconButton(onClick = { query = "" }) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = stringResource(R.string.common_clear)
                                            )
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
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        when {
                                            uiState.isLoadingCatalog -> {
                                                Text(
                                                    text = stringResource(R.string.list_detail_search_loading),
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }

                                            searchResults.isEmpty() -> {
                                                Text(
                                                    text = stringResource(R.string.list_detail_search_no_results),
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }

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
                                                            onClick = {
                                                                vm.addProduct(product)
                                                                query = ""
                                                            }
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

                    // ------------------ Productos actuales ------------------
                    item {
                        Text(
                            text = stringResource(R.string.list_detail_current_products_title),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                    item {
                        if (uiState.productsInList.isEmpty()) {
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            ) {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 80.dp)
                                        .padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = stringResource(R.string.list_detail_no_products),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        } else {

                            // ✅ Punto 5: aplicar el tipo de vista
                            when (viewType) {

                                ListViewType.BUBBLES -> {
                                    Surface(
                                        shape = MaterialTheme.shapes.medium,
                                        color = MaterialTheme.colorScheme.errorContainer,
                                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            verticalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            uiState.productsInList.forEach { product ->
                                                val checked = uiState.isChecked(product.id)
                                                val qty = uiState.quantity(product.id)

                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                ) {
                                                    Checkbox(
                                                        checked = checked,
                                                        onCheckedChange = { vm.toggleChecked(product.id) }
                                                    )

                                                    ProductBubble(
                                                        product = product,
                                                        onClick = {
                                                            vm.removeProduct(product)
                                                            Log.d(TAG_GLOBAL, "ListDetail → Quitar: ${product.id} / ${product.name}")
                                                        }
                                                    )

                                                    Spacer(Modifier.weight(1f))

                                                    IconButton(onClick = { vm.decQuantity(product.id) }) {
                                                        Icon(
                                                            Icons.Default.Remove,
                                                            contentDescription = stringResource(R.string.common_minus)
                                                        )
                                                    }

                                                    Text(text = qty.toString(), style = MaterialTheme.typography.titleMedium)

                                                    IconButton(onClick = { vm.incQuantity(product.id) }) {
                                                        Icon(
                                                            Icons.Default.Add,
                                                            contentDescription = stringResource(R.string.common_plus)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                ListViewType.LIST -> {
                                    Surface(
                                        shape = MaterialTheme.shapes.medium,
                                        color = MaterialTheme.colorScheme.errorContainer,
                                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            verticalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            uiState.productsInList.forEach { product ->
                                                val checked = uiState.isChecked(product.id)
                                                val qty = uiState.quantity(product.id)

                                                Surface(
                                                    shape = MaterialTheme.shapes.large,
                                                    color = MaterialTheme.colorScheme.surface,
                                                    contentColor = MaterialTheme.colorScheme.onSurface
                                                ) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 10.dp, vertical = 8.dp),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                    ) {
                                                        Checkbox(
                                                            checked = checked,
                                                            onCheckedChange = { vm.toggleChecked(product.id) }
                                                        )

                                                        ProductBubble(
                                                            product = product,
                                                            modifier = Modifier.width(72.dp),
                                                            onClick = { vm.removeProduct(product) }
                                                        )

                                                        Text(
                                                            text = product.name,
                                                            style = MaterialTheme.typography.titleSmall,
                                                            modifier = Modifier.weight(1f),
                                                            maxLines = 1
                                                        )

                                                        IconButton(onClick = { vm.decQuantity(product.id) }) {
                                                            Icon(Icons.Default.Remove, contentDescription = stringResource(R.string.common_minus))
                                                        }

                                                        Text(text = qty.toString(), style = MaterialTheme.typography.titleMedium)

                                                        IconButton(onClick = { vm.incQuantity(product.id) }) {
                                                            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.common_plus))
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                ListViewType.COMPACT -> {
                                    Surface(
                                        shape = MaterialTheme.shapes.medium,
                                        color = MaterialTheme.colorScheme.errorContainer,
                                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(10.dp),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            uiState.productsInList.forEach { product ->
                                                val checked = uiState.isChecked(product.id)
                                                val qty = uiState.quantity(product.id)

                                                Surface(
                                                    shape = MaterialTheme.shapes.large,
                                                    color = MaterialTheme.colorScheme.surface,
                                                    contentColor = MaterialTheme.colorScheme.onSurface
                                                ) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 10.dp, vertical = 6.dp),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                    ) {
                                                        Checkbox(
                                                            checked = checked,
                                                            onCheckedChange = { vm.toggleChecked(product.id) }
                                                        )

                                                        Text(
                                                            text = product.name,
                                                            style = MaterialTheme.typography.bodyLarge,
                                                            modifier = Modifier.weight(1f),
                                                            maxLines = 1
                                                        )

                                                        IconButton(onClick = { vm.decQuantity(product.id) }) {
                                                            Icon(Icons.Default.Remove, contentDescription = stringResource(R.string.common_minus))
                                                        }

                                                        Text(text = qty.toString(), style = MaterialTheme.typography.titleMedium)

                                                        IconButton(onClick = { vm.incQuantity(product.id) }) {
                                                            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.common_plus))
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // ------------------ Usados recientemente ------------------
                    item {
                        Text(
                            text = stringResource(R.string.list_detail_recent_used_title),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                    item {
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ) {
                            @OptIn(ExperimentalLayoutApi::class)
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                val recent = uiState.recentCatalog
                                    .filter { it.id !in inListIds }
                                    .filter { categoryAllowed(it.category) }

                                if (uiState.isLoadingCatalog) {
                                    Text(
                                        text = stringResource(R.string.list_detail_catalog_loading),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                } else if (recent.isEmpty()) {
                                    Text(
                                        text = uiState.catalogError ?: stringResource(R.string.list_detail_catalog_empty),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                } else {
                                    recent.forEach { product ->
                                        ProductBubble(product = product, onClick = { vm.addProduct(product) })
                                    }
                                }
                            }
                        }
                    }

                    // ------------------ Categorías ------------------
                    uiState.catalogByCategory
                        .filterKeys { categoryAllowed(it) }
                        .forEach { (category, items) ->

                            val available = items
                                .filter { it.id !in inListIds }
                                .filter { categoryAllowed(it.category) }

                            if (available.isNotEmpty()) {
                                item {
                                    Text(
                                        text = stringResource(id = category.labelRes),
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                                item {
                                    Surface(
                                        shape = MaterialTheme.shapes.medium,
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    ) {
                                        @OptIn(ExperimentalLayoutApi::class)
                                        FlowRow(
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            verticalArrangement = Arrangement.spacedBy(10.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp)
                                        ) {
                                            available.forEach { product ->
                                                ProductBubble(product = product, onClick = { vm.addProduct(product) })
                                            }
                                        }
                                    }
                                }
                            }
                        }
                }

                // ------------------ Dialog: vaciar lista ------------------
                if (confirmClearExpanded) {
                    AlertDialog(
                        onDismissRequest = { confirmClearExpanded = false },
                        title = { Text(stringResource(R.string.list_detail_clear_confirm_title)) },
                        text = { Text(stringResource(R.string.list_detail_clear_confirm_body)) },
                        confirmButton = {
                            Button(
                                onClick = {
                                    confirmClearExpanded = false
                                    vm.clearList()
                                }
                            ) { Text(stringResource(R.string.list_detail_clear_confirm_yes)) }
                        },
                        dismissButton = {
                            OutlinedButton(onClick = { confirmClearExpanded = false }) {
                                Text(stringResource(R.string.list_detail_clear_confirm_no))
                            }
                        }
                    )
                }

                Box(Modifier.align(Alignment.BottomStart)) {
                    BackButton(onClick = onBack)
                }
            }
        }
    }
}
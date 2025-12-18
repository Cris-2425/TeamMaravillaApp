package com.example.teammaravillaapp.page.listdetail

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.ProductBubble
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.data.FakeUserLists
import com.example.teammaravillaapp.model.ListBackgrounds
import com.example.teammaravillaapp.model.ProductData
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

@Composable
fun ListDetail(
    listId: String? = null,
    onBack: () -> Unit = {}
) {
    val vm: ListDetailViewModel = viewModel(factory = ListDetailViewModelFactory(listId))
    val uiState by vm.uiState.collectAsState()

    when {
        uiState.isEmptyState -> {
            Box(Modifier.fillMaxSize()) {
                GeneralBackground()
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
            val inListNames = uiState.inListNames

            Box(Modifier.fillMaxSize()) {
                GeneralBackground(bgRes = bgRes)

                @OptIn(ExperimentalLayoutApi::class)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 120.dp)
                ) {
                    item {
                        Spacer(Modifier.height(8.dp))
                        Title(userList.name)
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
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            ) {
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp)
                                ) {
                                    uiState.productsInList.forEach { product ->
                                        ProductBubble(
                                            product = product,
                                            onClick = {
                                                vm.removeProduct(product)
                                                Log.d(TAG_GLOBAL, "ListDetail → Quitar: ${product.name}")
                                            }
                                        )
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
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                ProductData.recentUsed
                                    .filter { it.name !in inListNames }
                                    .forEach { product ->
                                        ProductBubble(
                                            product = product,
                                            onClick = {
                                                vm.addProduct(product)
                                                Log.d(TAG_GLOBAL, "ListDetail → Añadir reciente: ${product.name}")
                                            }
                                        )
                                    }
                            }
                        }
                    }

                    // ------------------ Categorías ------------------
                    ProductData.byCategory.forEach { (category, items) ->
                        val available = items.filter { it.name !in inListNames }

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
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp)
                                    ) {
                                        available.forEach { product ->
                                            ProductBubble(
                                                product = product,
                                                onClick = {
                                                    vm.addProduct(product)
                                                    Log.d(
                                                        TAG_GLOBAL,
                                                        "ListDetail → Añadir categoría ${category.name}: ${product.name}"
                                                    )
                                                }
                                            )
                                        }
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
}

@Preview
@Composable
fun PreviewListDetailFromSample() {
    TeamMaravillaAppTheme {
        FakeUserLists.seedIfEmpty()
        val id = FakeUserLists.all().last().first
        ListDetail(listId = id)
    }
}
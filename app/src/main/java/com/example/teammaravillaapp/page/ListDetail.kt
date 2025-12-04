package com.example.teammaravillaapp.page

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductData
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

/**
 * # Pantalla: **Detalle de lista**
 *
 * Muestra el contenido de una lista creada por el usuario y permite:
 * - Ver los productos actuales.
 * - Eliminar productos con un toque.
 * - Añadir productos desde secciones de “usados recientemente” o por categoría.
 *
 * Si no se recibe un `listId`, muestra la última lista almacenada en [FakeUserLists].
 * Si no existen listas, muestra un estado vacío con un mensaje informativo.
 *
 * @param listId ID opcional de la lista a visualizar.
 * @param onBack Acción al pulsar el botón de volver.
 */
@Composable
fun ListDetail(
    listId: String? = null,
    onBack: () -> Unit = {}
) {
    val resolved: UserList? = remember(listId) {
        listId?.let(FakeUserLists::get) ?: FakeUserLists.all().lastOrNull()?.second
    }

    when (resolved) {
        null -> {
            // Estado vacío (sin listas registradas)
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
        else -> ListDetailContent(
            userList = resolved,
            onBack = onBack
        )
    }
}

/**
 * Contenido principal del detalle de lista.
 *
 * @param userList Lista del usuario con sus productos, nombre y fondo.
 * @param onBack Acción al pulsar el botón de volver.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListDetailContent(
    userList: UserList,
    onBack: () -> Unit = {}
) {
    val bgRes = ListBackgrounds.getBackgroundRes(userList.background)

    // Estado reactivo de productos en la lista (solo en memoria)
    val productsInList = remember(userList.name) {
        mutableStateListOf<Product>().apply { addAll(userList.products) }
    }

    // Conjunto de nombres de productos ya presentes en la lista (se recalcula en cada recomposición)
    val inListNames = productsInList.map { it.name }.toSet()

    fun syncWithRepo() {
        // Guarda la lista en la "fake DB" para que se mantenga mientras dure la app
        FakeUserLists.updateProductsByName(userList.name, productsInList.toList())
    }

    Box(Modifier.fillMaxSize()) {
        GeneralBackground(bgRes = bgRes)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // Título
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
                if (productsInList.isEmpty()) {
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
                            productsInList.forEach { product ->
                                ProductBubble(
                                    product = product,
                                    onClick = {
                                        productsInList.remove(product)
                                        FakeUserLists.updateProductsByName(
                                            userList.name,
                                            productsInList.toList()
                                        )
                                        Log.e(TAG_GLOBAL, "ListDetail → Quitar: ${product.name}")
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
                                        if (productsInList.none { p -> p.name == product.name }) {
                                            productsInList.add(product)
                                            FakeUserLists.updateProductsByName(
                                                userList.name,
                                                productsInList.toList()
                                            )
                                            Log.e(TAG_GLOBAL, "ListDetail → Añadir reciente: ${product.name}")
                                        }
                                    }
                                )
                            }
                    }
                }
            }

            // ------------------ Categorías ------------------
            ProductData.byCategory.forEach { (category, items) ->
                // Filtramos productos de esta categoría que NO están en la lista
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
                                            if (productsInList.none { p -> p.name == product.name }) {
                                                productsInList.add(product)
                                                FakeUserLists.updateProductsByName(
                                                    userList.name,
                                                    productsInList.toList()
                                                )
                                                Log.e(
                                                    TAG_GLOBAL,
                                                    "ListDetail → Añadir categoría ${category.name}: ${product.name}"
                                                )
                                            }
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

@Preview
@Composable
fun PreviewListDetailFromSample() {
    TeamMaravillaAppTheme {
        val sample = FakeUserLists.sample()
        ListDetailContent(userList = sample)
    }
}

@Preview
@Composable
fun PreviewListDetailEmpty() {
    TeamMaravillaAppTheme {
        ListDetail(listId = null)
    }
}
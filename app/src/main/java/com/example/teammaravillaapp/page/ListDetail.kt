package com.example.teammaravillaapp.page

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
 */
@Composable
fun ListDetail(
    listId: String? = null
) {
    val resolved: UserList? = remember(listId) {
        listId?.let(FakeUserLists::get) ?: FakeUserLists.all().lastOrNull()?.second
    }

    when (resolved) {
        null -> {
            /** Estado vacío (sin listas registradas) */
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
                        "Aún no hay listas creadas",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "Crea una nueva lista",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Box(Modifier.align(Alignment.BottomStart)) {
                    BackButton()
                }
            }
        }
        else -> ListDetailContent(userList = resolved)
    }
}

/**
 * # Contenido principal del detalle de lista
 *
 * Construye la vista interna de la pantalla.
 * Divide la interfaz en cuatro secciones:
 * 1. Nombre de la lista.
 * 2. Productos actuales (clic → eliminar).
 * 3. Usados recientemente (clic → añadir).
 * 4. Categorías y sus productos (clic → añadir).
 *
 * @param userList Lista del usuario con sus productos, nombre y fondo.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListDetailContent(
    userList: UserList
) {
    val bgRes = ListBackgrounds.getBackgroundRes(userList.background)
    val productsInList = remember { mutableStateListOf<Product>().apply { addAll(userList.products) } }

    Box(Modifier.fillMaxSize()) {
        GeneralBackground(bgRes = bgRes)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            /** Título */
            item {
                Spacer(Modifier.height(8.dp))

                Title(userList.name)
            }

            /** Productos actuales */
            item { Text("Productos en la lista", style = MaterialTheme.typography.titleSmall) }

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
                        ) { Text("No hay productos todavía") }
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
                            productsInList.forEach {
                                Box(
                                    Modifier
                                        .wrapContentSize()
                                        .clickable {
                                        productsInList.remove(it)
                                        Log.e(TAG_GLOBAL, "Quitar: ${it.name}")
                                    }
                                ) { ProductBubble(it) }
                            }
                        }
                    }
                }
            }

            /** Usados recientemente */
            item { Text(
                "Usados recientemente",
                style = MaterialTheme.typography.titleSmall) }

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
                        ProductData.recentUsed.forEach {
                            Box(Modifier
                                .wrapContentSize()
                                .clickable {
                                if (productsInList.none { p -> p.name == it.name }) {
                                    productsInList.add(it)
                                    Log.e(TAG_GLOBAL, "Añadir: ${it.name}")
                                }
                            }) { ProductBubble(it) }
                        }
                    }
                }
            }

            /** Categorías */
            ProductData.byCategory.forEach { (category, items) ->
                item { Text(category.label, style = MaterialTheme.typography.titleSmall) }
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
                            items.forEach {
                                Box(Modifier
                                    .wrapContentSize()
                                    .clickable {
                                    if (productsInList.none { p -> p.name == it.name }) {
                                        productsInList.add(it)
                                        Log.e(TAG_GLOBAL, "Añadir${category.name}: ${it.name}")
                                    }
                                }) { ProductBubble(it) }
                            }
                        }
                    }
                }
            }
        }

        Box(Modifier.align(Alignment.BottomStart)) {
            BackButton()
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
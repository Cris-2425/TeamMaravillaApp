package com.example.teammaravillaapp.page

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
 * Pantalla de detalle de lista.
 * - Si [listId] es null, intenta mostrar la última lista creada.
 * - Si no hay listas, muestra un estado vacío.
 */
@Composable
fun ListDetail(
    listId: String? = null
) {
    /** Resuelve la lista: por id, o la última creada si no hay id */
    val resolved = remember(listId) {
        listId?.let(FakeUserLists::get) ?: FakeUserLists.all().lastOrNull()?.second
    }

    when (resolved) {
        null -> {
            Box(
                Modifier
                    .fillMaxSize()
            ) {
                GeneralBackground()
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Aún no hay listas creadas", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text("Crea una nueva lista", style = MaterialTheme.typography.bodyMedium)
                }
                Box(Modifier.align(Alignment.BottomStart)) { BackButton() }
            }
        }
        else -> {
            ListDetailContent(userList = resolved)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListDetailContent(
    userList: UserList
) {
    val bgRes = ListBackgrounds.getBackgroundRes(userList.background)

    /** Estado local editable con los productos actuales */
    val productsInList = remember {
        mutableStateListOf<Product>().apply { addAll(userList.products) }
    }

    /** Helper para sincronizar con el repo tras cada cambio */
    fun syncRepo() {
        FakeUserLists.updateProductsByName(userList.name, productsInList.toList())
        Log.e(
            TAG_GLOBAL,
            "Guardado automático '${userList.name}' (${productsInList.size} productos)"
        )
    }

    Box(Modifier.fillMaxSize()) {

        GeneralBackground(bgRes)

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Title(userList.name)

            // --- Productos en la lista (click = quitar) ---
            Text("Productos en la lista", style = MaterialTheme.typography.titleSmall)

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
                                    syncRepo()
                                }
                        ) { ProductBubble(it) }
                    }
                }
            }

            // --- Usados recientemente (click = añadir) ---
            Text("Usados recientemente", style = MaterialTheme.typography.titleSmall)

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
                        Box(
                            Modifier
                                .wrapContentSize()
                                .clickable {
                                    if (productsInList.none { p -> p.name == it.name }) {
                                        productsInList.add(it)
                                        Log.e(TAG_GLOBAL, "ListDetail → Añadir (recent): ${it.name}")
                                        syncRepo()
                                    } else {
                                        Log.e(TAG_GLOBAL, "ListDetail → Ya estaba (recent): ${it.name}")
                                    }
                                }
                        ) { ProductBubble(it) }
                    }
                }
            }

            // --- Todas las categorías (click = añadir) ---
            ProductData.byCategory.forEach { (category, items) ->
                Text(category.label, style = MaterialTheme.typography.titleSmall)

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
                            Box(
                                Modifier
                                    .wrapContentSize()
                                    .clickable {
                                        if (productsInList.none { p -> p.name == it.name }) {
                                            productsInList.add(it)
                                            Log.e(
                                                TAG_GLOBAL,
                                                "ListDetail → Añadir (cat=${category.name}): ${it.name}"
                                            )
                                            syncRepo()
                                        } else {
                                            Log.e(
                                                TAG_GLOBAL,
                                                "ListDetail → Ya estaba (cat=${category.name}): ${it.name}"
                                            )
                                        }
                                    }
                            ) { ProductBubble(it) }
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))
        }

        Box(Modifier.align(Alignment.BottomStart)) { BackButton() }
    }
}

@Preview
@Composable
private fun PreviewListDetailFromSample() {
    TeamMaravillaAppTheme {
        val sample = FakeUserLists.sample()
        ListDetailContent(userList = sample)
    }
}

@Preview
@Composable
private fun PreviewListDetailEmpty() {
    TeamMaravillaAppTheme {
        ListDetail(listId = null)
    }
}

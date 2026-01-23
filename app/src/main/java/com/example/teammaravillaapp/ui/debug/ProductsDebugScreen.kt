package com.example.teammaravillaapp.ui.debug

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.ProductBubble
import com.example.teammaravillaapp.component.Title

@Composable
fun ProductsDebugScreen(
    onBack: () -> Unit
) {
    val vm: ProductsDebugViewModel = hiltViewModel()
    val uiState by vm.uiState.collectAsState()

    Box(Modifier.fillMaxSize()) {
        GeneralBackground()

        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            Title(texto = "DEBUG · Productos (API)")

            Spacer(Modifier.height(12.dp))

            // --- Botonera ---
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = vm::refresh,
                        enabled = !uiState.isLoading
                    ) { Text("Refrescar") }

                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // ✅ YA NO PASAMOS CONTEXT
                    Button(
                        onClick = vm::seed,
                        enabled = !uiState.isLoading
                    ) { Text("Seed catálogo") }
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = vm::addDummyProduct,
                        enabled = !uiState.isLoading
                    ) { Text("Añadir dummy") }

                    OutlinedButton(
                        onClick = vm::updateFirst,
                        enabled = !uiState.isLoading && uiState.products.isNotEmpty()
                    ) { Text("Update 1º") }

                    OutlinedButton(
                        onClick = vm::deleteFirst,
                        enabled = !uiState.isLoading && uiState.products.isNotEmpty()
                    ) { Text("Borrar 1º") }
                }
            }

            Spacer(Modifier.height(12.dp))

            // --- Error ---
            uiState.error?.let { err ->
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ) {
                    Text(
                        text = err,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(Modifier.height(12.dp))
            }

            // --- Lista ---
            if (!uiState.isLoading && uiState.products.isEmpty() && uiState.error == null) {
                Text(
                    text = "La API devolvió 0 productos.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 120.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.products) { p ->
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProductBubble(product = p)

                            Column(Modifier.weight(1f)) {
                                Text(
                                    text = "${p.name} · id=${p.id}",
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "cat=${p.category} | imageUrl=${p.imageUrl ?: "-"}",
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
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
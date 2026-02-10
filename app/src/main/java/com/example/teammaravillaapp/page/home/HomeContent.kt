package com.example.teammaravillaapp.page.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.ListCard
import com.example.teammaravillaapp.component.QuickActionsRow
import com.example.teammaravillaapp.component.RenameListDialog
import com.example.teammaravillaapp.component.SearchField
import com.example.teammaravillaapp.component.SectionCard
import com.example.teammaravillaapp.component.SwipeRowActions
import com.example.teammaravillaapp.model.CardInfo
import com.example.teammaravillaapp.model.QuickActionData
import com.example.teammaravillaapp.model.SearchFieldData
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Contenido de la pantalla **Home** (presentación pura).
 *
 * Este Composable pinta la UI de Home sin depender de ViewModels ni repositorios.
 * Toda la información entra por parámetros, lo que:
 * - Facilita previews y pruebas.
 * - Permite reutilizar el layout en distintos flujos (por ejemplo, modo demo).
 * - Garantiza separación de responsabilidades (MVVM).
 *
 * Incluye comportamiento de UI local:
 * - Manejo de diálogo de renombrado (estado temporal del texto).
 * - Solicitud de foco al campo de búsqueda (si se solicita desde fuera).
 *
 * @param uiState Estado ya preparado para pintar. No debe ser nulo.
 * @param requestFocusSearch Si es {@code true}, solicita foco en el buscador en cuanto el TextField esté compuesto.
 * @param onFocusSearchConsumed Callback para marcar el foco como consumido y evitar repetición.
 * @param onSearchChange Callback cuando cambia el texto del buscador. Debe aceptar cualquier String (incluido vacío).
 * @param onNavigateCreateList Acción principal para crear una lista.
 * @param onNavigateRecipes Acción rápida para ir a Recetas.
 * @param onNavigateHistory Acción rápida para ir a Historial.
 * @param onOpenList Acción al pulsar una tarjeta de lista. Recibe {@code id} no nulo y no vacío.
 * @param onDelete Acción para solicitar borrado de una lista. Recibe {@code id} no nulo y no vacío.
 * @param onRename Acción para renombrar una lista. Recibe:
 *        - {@code id} no nulo/no vacío
 *        - {@code newName} recomendado ya validado (trim y no blank).
 * @param modifier Modificador Compose para personalizar layout (padding, tamaño, etc.).
 *
 * @throws IllegalArgumentException Puede lanzarse por la capa superior (ViewModel/Repo)
 *         si {@code onRename} recibe un nombre inválido (por ejemplo, vacío o solo espacios).
 *         Este Composable evita confirmación si el texto está en blanco, pero la validación final
 *         debe estar en dominio/datos.
 *
 * @see Home
 * @see HomeUiState
 * @see RenameListDialog
 * @see SwipeRowActions
 *
 * Ejemplo de uso:
 * {@code
 * HomeContent(
 *   uiState = state,
 *   requestFocusSearch = true,
 *   onFocusSearchConsumed = { /* marcar bandera */ },
 *   onSearchChange = { vm.onSearchChange(it) },
 *   onNavigateCreateList = { nav.navigate("createList") },
 *   onOpenList = { id -> nav.navigate("listDetail/$id") },
 *   onDelete = { id -> vm.requestDelete(id) },
 *   onRename = { id, newName -> vm.renameList(id, newName) }
 * )
 * }
 */
@Composable
fun HomeContent(
    uiState: HomeUiState,
    requestFocusSearch: Boolean,
    onFocusSearchConsumed: () -> Unit,
    onSearchChange: (String) -> Unit,
    onNavigateCreateList: () -> Unit,
    onNavigateRecipes: () -> Unit,
    onNavigateHistory: () -> Unit,
    onOpenList: (String) -> Unit,
    onDelete: (String) -> Unit,
    onRename: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var renameTarget by remember { mutableStateOf<Pair<String, String>?>(null) }
    var renameText by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(requestFocusSearch) {
        if (requestFocusSearch) {
            focusRequester.requestFocus()
            onFocusSearchConsumed()
        }
    }

    val labelNewList = stringResource(R.string.home_quick_new_list)
    val labelFavorites = stringResource(R.string.home_quick_favorites)
    val labelHistory = stringResource(R.string.home_quick_history)

    val quickActions = listOf(
        QuickActionData(Icons.Default.ShoppingCart, labelNewList),
        QuickActionData(Icons.Default.Favorite, labelFavorites),
        QuickActionData(Icons.Default.Create, labelHistory)
    )

    GeneralBackground(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.home_title),
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            item {

                // Buscador y acciones rápidas
                SectionCard {
                    SearchField(
                        modifier = Modifier.focusRequester(focusRequester),
                        searchData = SearchFieldData(
                            value = uiState.search,
                            placeholder = stringResource(R.string.home_search_placeholder)
                        ),
                        onValueChange = onSearchChange
                    )

                    Spacer(Modifier.height(12.dp))

                    QuickActionsRow(
                        actions = quickActions,
                        onClick = { action ->
                            when (action.label) {
                                labelNewList -> onNavigateCreateList()
                                labelFavorites -> onNavigateRecipes()
                                labelHistory -> onNavigateHistory()
                            }
                        }
                    )
                }
            }

            item {

                // Listas recientes
                SectionCard {
                    Text(
                        text = stringResource(R.string.home_recent_lists_title),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(10.dp))

                    if (uiState.rows.isEmpty()) {
                        Text(
                            text = stringResource(R.string.home_recent_lists_empty),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            uiState.rows.forEach { row ->
                                val id = row.id
                                val list = row.list
                                val p = row.progress

                                val subtitle = if (p.totalCount == 0) {
                                    stringResource(R.string.home_list_subtitle_empty)
                                } else {
                                    stringResource(
                                        R.string.home_list_subtitle_progress,
                                        p.checkedCount,
                                        p.totalCount
                                    )
                                }

                                SwipeRowActions(
                                    rowKey = id,
                                    onEdit = { renameTarget = id to list.name },
                                    onDelete = { onDelete(id) }
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        ListCard(
                                            cardInfo = CardInfo(
                                                imageID = R.drawable.list_supermarket,
                                                imageDescription = list.name,
                                                title = list.name,
                                                subtitle = subtitle
                                            ),
                                            modifier = Modifier.fillMaxWidth(),
                                            onClick = { onOpenList(id) }
                                        )

                                        if (p.totalCount > 0) {
                                            LinearProgressIndicator(
                                                progress = { p.checkedCount.toFloat() / p.totalCount.toFloat() },
                                                modifier = Modifier.fillMaxWidth()
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

        // Diálogo de renombrado
        renameTarget?.let { (listId, currentName) ->
            LaunchedEffect(currentName) { renameText = currentName }

            RenameListDialog(
                nameValue = renameText,
                onNameChange = { renameText = it },
                onDismiss = { renameTarget = null },
                onConfirm = {
                    val trimmed = renameText.trim()
                    if (trimmed.isNotBlank()) {
                        onRename(listId, trimmed)
                    }
                    renameTarget = null
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHomeContentEmpty() {
    TeamMaravillaAppTheme {
        HomeContent(
            uiState = HomeUiState(search = "", rows = emptyList()),
            requestFocusSearch = false,
            onFocusSearchConsumed = {},
            onSearchChange = {},
            onNavigateCreateList = {},
            onNavigateRecipes = {},
            onNavigateHistory = {},
            onOpenList = {},
            onDelete = {},
            onRename = { _, _ -> }
        )
    }
}
package com.example.teammaravillaapp.page.selectlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.ui.app.AppViewModel

@Composable
fun SelectList(
    recipeId: Int,
    appViewModel: AppViewModel,
    onBack: () -> Unit,
    onCreateList: () -> Unit,
    onListSelected: (String) -> Unit
) {
    val vm: SelectListViewModel = viewModel(factory = SelectListViewModelFactory(recipeId))
    val uiState by vm.uiState.collectAsState()

    Box(Modifier.fillMaxSize()) {
        GeneralBackground()

        when {
            uiState.isLoading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }

            uiState.isRecipeNotFound -> {
                Text(
                    text = stringResource(R.string.recipe_not_found),
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            else -> {
                val recipe = uiState.recipe!!

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(Modifier.height(18.dp))

                    Title(texto = stringResource(R.string.recipe_add_ingredients_button))

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Selecciona una lista para añadir: ${recipe.title}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(Modifier.height(16.dp))

                    if (uiState.lists.isEmpty()) {
                        Text(
                            text = "No tienes listas todavía.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = onCreateList, modifier = Modifier.fillMaxWidth()) {
                            Text("Crear lista")
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 120.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(uiState.lists) { (id, list) ->
                                Surface(
                                    shape = MaterialTheme.shapes.large,
                                    tonalElevation = 2.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            vm.addRecipeIngredientsToList(id)

                                            // Evento UI global (SnackBar)
                                            appViewModel.showSnackbar("Ingredientes añadidos a la lista")

                                            // navegación
                                            onListSelected(id)
                                        }
                                ) {
                                    Column(Modifier.padding(14.dp)) {
                                        Text(
                                            text = list.name,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            text = "${list.products.size} productos",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
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
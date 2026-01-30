package com.example.teammaravillaapp.page.selectlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.ui.events.UiEvent

@Composable
fun SelectList(
    onBack: () -> Unit,
    onCreateList: () -> Unit,
    onListSelected: (String) -> Unit,
    onUiEvent: (UiEvent) -> Unit,
    vm: SelectListViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(vm) {
        vm.events.collect { onUiEvent(it) }
    }

    Box(Modifier.fillMaxSize()) {
        GeneralBackground(overlayAlpha = 0.20f) {

            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                uiState.isRecipeNotFound -> {
                    Surface(
                        shape = MaterialTheme.shapes.extraLarge,
                        tonalElevation = 2.dp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 18.dp)
                    ) {
                        Column(
                            Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.recipe_not_found),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }

                else -> {
                    val recipe = uiState.recipe!!

                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Spacer(Modifier.height(8.dp))

                        Title(texto = stringResource(R.string.recipe_add_ingredients_button))

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = stringResource(R.string.select_list_subtitle, recipe.title),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(14.dp))

                        Surface(
                            shape = MaterialTheme.shapes.extraLarge,
                            tonalElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(14.dp)) {

                                if (uiState.lists.isEmpty()) {
                                    Text(
                                        text = stringResource(R.string.select_list_empty),
                                        style = MaterialTheme.typography.bodyMedium
                                    )

                                    Spacer(Modifier.height(12.dp))

                                    Button(
                                        onClick = onCreateList,
                                        modifier = Modifier.fillMaxWidth(),
                                        contentPadding = PaddingValues(vertical = 14.dp)
                                    ) {
                                        Text(stringResource(R.string.select_list_create))
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
                                                tonalElevation = 1.dp,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable(
                                                        enabled = !uiState.isSaving
                                                    ) {
                                                        vm.addRecipeIngredientsToList(id)
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
                                                        text = stringResource(
                                                            R.string.select_list_products_count,
                                                            list.productIds.size
                                                        ),
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
            }

            //Box(Modifier.align(Alignment.BottomStart)) {
            //    BackButton(onClick = onBack)
            //}
        }
    }
}
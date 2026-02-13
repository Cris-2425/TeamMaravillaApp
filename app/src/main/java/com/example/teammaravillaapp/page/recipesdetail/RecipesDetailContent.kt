package com.example.teammaravillaapp.page.recipesdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.ProductBubble
import com.example.teammaravillaapp.model.Recipe
import androidx.compose.foundation.layout.Row

/**
 * UI pura del detalle de receta.
 *
 * No depende de Hilt, ni de repositorios, ni de navegación. Solo renderiza el [uiState] y expone callbacks.
 *
 * @param uiState Estado ya resuelto para pintar.
 * @param onToggleFavorite Acción de UI para alternar favorito.
 * @param onAddToShoppingList Acción de UI para “Añadir ingredientes” (recibe recipeId).
 * @param onBack Acción de navegación hacia atrás.
 *
 * @throws IllegalStateException No se lanza directamente, pero si [uiState.recipe] es null cuando
 * isNotFound=false e isLoading=false, la UI no puede renderizar correctamente.
 *
 * Ejemplo de uso:
 * {@code
 * RecipesDetailContent(
 *   uiState = RecipesDetailUiState(isLoading=false, recipe=recipe),
 *   onToggleFavorite = {},
 *   onAddToShoppingList = {},
 *   onBack = {}
 * )
 * }
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipesDetailContent(
    uiState: RecipesDetailUiState,
    onToggleFavorite: () -> Unit,
    onAddToShoppingList: (Int) -> Unit,
    onBack: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        GeneralBackground(overlayAlpha = 0.20f) {

            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                uiState.isNotFound -> {
                    Surface(
                        shape = MaterialTheme.shapes.extraLarge,
                        tonalElevation = 2.dp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 18.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.recipe_not_found),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    val recipe: Recipe = uiState.recipe ?: return@GeneralBackground

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {

                        item { Spacer(Modifier.height(10.dp)) }

                        item {
                            Surface(
                                shape = RoundedCornerShape(28.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                tonalElevation = 2.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text(
                                            text = recipe.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier.weight(1f),
                                            maxLines = 1
                                        )

                                        IconButton(onClick = onToggleFavorite) {
                                            Icon(
                                                imageVector = if (uiState.isFavorite)
                                                    Icons.Filled.Favorite
                                                else
                                                    Icons.Outlined.FavoriteBorder,
                                                contentDescription = null,
                                                tint = if (uiState.isFavorite)
                                                    MaterialTheme.colorScheme.primary
                                                else
                                                    MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Surface(
                                shape = MaterialTheme.shapes.extraLarge,
                                tonalElevation = 2.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val img = recipe.imageRes
                                if (img != null && img != 0) {
                                    Image(
                                        painter = painterResource(img),
                                        contentDescription = stringResource(R.string.recipe_image_content_description),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(190.dp)
                                            .clip(MaterialTheme.shapes.extraLarge)
                                            .clickable { },
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(190.dp)
                                            .clip(MaterialTheme.shapes.extraLarge)
                                            .background(MaterialTheme.colorScheme.secondaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = stringResource(R.string.recipe_image_placeholder),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Text(
                                text = stringResource(R.string.recipe_ingredients_title),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start
                            )
                        }

                        item {
                            Surface(
                                shape = MaterialTheme.shapes.extraLarge,
                                tonalElevation = 1.dp,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                FlowRow(
                                    maxItemsInEachRow = 3,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp)
                                ) {
                                    uiState.ingredients.forEach { product ->
                                        ProductBubble(product)
                                    }
                                }
                            }
                        }

                        if (recipe.instructions.isNotBlank()) {
                            item {
                                Text(
                                    text = stringResource(R.string.recipe_instructions_title),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            item {
                                Surface(
                                    shape = MaterialTheme.shapes.extraLarge,
                                    tonalElevation = 1.dp,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = recipe.instructions,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(14.dp)
                                    )
                                }
                            }
                        }

                        item {
                            Button(
                                onClick = { onAddToShoppingList(recipe.id) },
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(vertical = 14.dp)
                            ) {
                                Text(text = stringResource(R.string.recipe_add_ingredients_button))
                            }
                        }

                        item { Spacer(Modifier.height(30.dp)) }
                    }
                }
            }
        }
    }
}
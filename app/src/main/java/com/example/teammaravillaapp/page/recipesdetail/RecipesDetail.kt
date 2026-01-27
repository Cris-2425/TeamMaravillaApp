package com.example.teammaravillaapp.page.recipesdetail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.ProductBubble
import com.example.teammaravillaapp.model.Recipe
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipesDetail(
    onBack: () -> Unit,
    onAddToShoppingList: (Int) -> Unit
) {
    val vm: RecipesDetailViewModel = hiltViewModel()
    val uiState by vm.uiState.collectAsState()

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

                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Spacer(Modifier.height(10.dp))

                        // HEADER
                        Surface(
                            shape = RoundedCornerShape(28.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            tonalElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = recipe.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1
                                )

                                IconButton(onClick = { vm.toggleFavorite() }) {
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

                        Spacer(Modifier.height(16.dp))

                        // IMAGEN (SAFE)
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
                                        .clickable {
                                            Log.d(TAG_GLOBAL, "Imagen pulsada: ${recipe.title}")
                                        },
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

                        Spacer(Modifier.height(18.dp))

                        // INGREDIENTES
                        Text(
                            text = stringResource(R.string.recipe_ingredients_title),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )

                        Spacer(Modifier.height(10.dp))

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

                        Spacer(Modifier.height(18.dp))

                        // PREPARACIÓN
                        if (recipe.instructions.isNotBlank()) {
                            Text(
                                text = stringResource(R.string.recipe_instructions_title),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(Modifier.height(8.dp))

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

                            Spacer(Modifier.height(18.dp))
                        }

                        Button(
                            onClick = { onAddToShoppingList(recipe.id) },
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(vertical = 14.dp)
                        ) {
                            Text(text = stringResource(R.string.recipe_add_ingredients_button))
                        }

                        Spacer(Modifier.height(100.dp))
                    }
                }
            }

            Box(Modifier.align(Alignment.BottomStart)) {
                BackButton(onClick = onBack)
            }
        }
    }
}

@Preview
@Composable
fun PreviewRecipesDetail() {
    TeamMaravillaAppTheme {
        RecipesDetail(onBack = {}, onAddToShoppingList = {})
    }
}
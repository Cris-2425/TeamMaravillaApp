package com.example.teammaravillaapp.page.recipesdetail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.ProductBubble
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL
import com.example.teammaravillaapp.model.Recipe

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipesDetail(
    recipeId: Int,
    onBack: () -> Unit,
    onAddToShoppingList: (Recipe) -> Unit
) {
    val vm: RecipesDetailViewModel = viewModel(
        factory = RecipesDetailViewModelFactory(recipeId)
    )
    val uiState by vm.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize()) {
            GeneralBackground()
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
            Box(Modifier.align(Alignment.BottomStart)) {
                BackButton(onClick = onBack)
            }
        }
        return
    }

    if (uiState.isNotFound) {
        Box(Modifier.fillMaxSize()) {
            GeneralBackground()
            Text(
                text = stringResource(R.string.recipe_not_found),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Center)
            )
            Box(Modifier.align(Alignment.BottomStart)) {
                BackButton(onClick = onBack)
            }
        }
        return
    }

    // ✅ Aquí ya lo convertimos en NO-NULL
    val recipe: Recipe = uiState.recipe ?: run {
        // estado intermedio raro (por seguridad)
        Box(Modifier.fillMaxSize()) {
            GeneralBackground()
            Box(Modifier.align(Alignment.BottomStart)) {
                BackButton(onClick = onBack)
            }
        }
        return
    }

    Box(Modifier.fillMaxSize()) {
        GeneralBackground()

        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // ---------- TÍTULO ----------
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Text(
                    text = recipe.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(24.dp))

            // ---------- IMAGEN ----------
            if (recipe.imageRes != null) {
                Image(
                    painter = painterResource(recipe.imageRes),
                    contentDescription = stringResource(R.string.recipe_image_content_description),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            Log.d(TAG_GLOBAL, "Imagen pulsada: ${recipe.title}")
                        },
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.recipe_image_placeholder),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ---------- INGREDIENTES ----------
            Text(
                text = stringResource(R.string.recipe_ingredients_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            FlowRow(
                maxItemsInEachRow = 3,
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                recipe.products.forEach { product ->
                    ProductBubble(product)
                }
            }

            Spacer(Modifier.height(24.dp))

            // ---------- PREPARACIÓN ----------
            if (recipe.instructions.isNotBlank()) {
                Text(
                    text = stringResource(R.string.recipe_instructions_title),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = recipe.instructions,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(24.dp))
            }

            // ---------- BOTÓN AÑADIR A LISTA ----------
            Button(
                onClick = { onAddToShoppingList(recipe) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.recipe_add_ingredients_button))
            }
        }

        Box(Modifier.align(Alignment.BottomStart)) {
            BackButton(onClick = onBack)
        }
    }
}

@Preview
@Composable
fun PreviewRecipesDetail() {
    TeamMaravillaAppTheme {
        RecipesDetail(
            recipeId = 1,
            onBack = {},
            onAddToShoppingList = {}
        )
    }
}
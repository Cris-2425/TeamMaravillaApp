package com.example.teammaravillaapp.component

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.seed.ProductData
import com.example.teammaravillaapp.data.seed.RecipeData
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.Recipe
import com.example.teammaravillaapp.util.TAG_GLOBAL

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipeCard(
    recipe: Recipe,
    ingredients: List<Product>,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onToggleFavorite: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val context = LocalContext.current

    val ingredientsTitle = stringResource(R.string.recipe_ingredients_title)
    val noIngredientsText = stringResource(R.string.recipe_no_ingredients)
    val favAddCd = stringResource(R.string.recipe_favorite_add_cd)
    val favRemoveCd = stringResource(R.string.recipe_favorite_remove_cd)

    val cs = MaterialTheme.colorScheme
    val imageShape = RoundedCornerShape(18.dp)

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = cs.secondary,
        contentColor = cs.onSecondary,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                Log.d(TAG_GLOBAL, "Click en '${recipe.title}'")
                onClick()
            }
    ) {
        Column(Modifier.padding(14.dp)) {

            // Header
            Surface(
                shape = RoundedCornerShape(50),
                color = cs.surface,
                contentColor = cs.onSurface
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        textAlign = TextAlign.Center
                    )

                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (isFavorite) favRemoveCd else favAddCd,
                            tint = if (isFavorite) cs.primary else cs.onSurface
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Imagen (URL > RES > placeholder)
            when {
                !recipe.imageUrl.isNullOrBlank() -> {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(recipe.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(imageShape),
                        placeholder = painterResource(R.drawable.logo),
                        error = painterResource(R.drawable.logo)
                    )
                }

                recipe.imageRes != null && recipe.imageRes != 0 -> {
                    Image(
                        painter = painterResource(recipe.imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(imageShape),
                        contentScale = ContentScale.Crop
                    )
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(imageShape)
                            .background(cs.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.recipe_image_placeholder),
                            style = MaterialTheme.typography.bodyMedium,
                            color = cs.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = ingredientsTitle,
                style = MaterialTheme.typography.titleSmall,
                color = cs.onSecondary
            )

            Spacer(Modifier.height(6.dp))

            if (ingredients.isEmpty()) {
                Text(
                    text = noIngredientsText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = cs.onSecondary
                )
            } else {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ingredients.forEach { ProductBubble(it) }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewRecipeCard() {
    val r = RecipeData.recipes.first()
    val byId = ProductData.allProducts.associateBy { it.id }
    val ing = r.productIds.mapNotNull { byId[it] }

    MaterialTheme {
        RecipeCard(
            recipe = r,
            ingredients = ing,
            isFavorite = true
        )
    }
}
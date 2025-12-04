package com.example.teammaravillaapp.model

import androidx.annotation.StringRes
import com.example.teammaravillaapp.R

/**
 * Categorías de productos disponibles en la app.
 *
 * @property labelRes recurso de texto para mostrar la categoría en UI.
 */
enum class ProductCategory(@StringRes val labelRes: Int) {
    FRUITS(R.string.category_fruits),
    VEGETABLES(R.string.category_vegetables),
    DAIRY(R.string.category_dairy),
    BAKERY(R.string.category_bakery),
    MEAT(R.string.category_meat),
    FISH(R.string.category_fish),
    DRINKS(R.string.category_drinks),
    PASTA(R.string.category_pasta),
    RICE(R.string.category_rice),
    CLEANING(R.string.category_cleaning),
    OTHER(R.string.category_other)
}

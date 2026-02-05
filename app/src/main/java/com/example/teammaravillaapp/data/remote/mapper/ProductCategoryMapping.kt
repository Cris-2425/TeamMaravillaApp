package com.example.teammaravillaapp.data.remote.mapper

import com.example.teammaravillaapp.model.ProductCategory

fun String?.toProductCategoryOrDefault(): ProductCategory {
    val key = this.normalizeKey() ?: return ProductCategory.OTHER
    return when (key) {
        "fruits", "fruit" -> ProductCategory.FRUITS
        "vegetables", "vegetable", "veggies" -> ProductCategory.VEGETABLES
        "dairy" -> ProductCategory.DAIRY
        "bakery" -> ProductCategory.BAKERY
        "meat" -> ProductCategory.MEAT
        "fish", "seafood" -> ProductCategory.FISH
        "drinks", "drink", "beverages" -> ProductCategory.DRINKS
        "pasta" -> ProductCategory.PASTA
        "rice" -> ProductCategory.RICE
        "cleaning" -> ProductCategory.CLEANING
        "other" -> ProductCategory.OTHER
        else -> ProductCategory.OTHER
    }
}

fun ProductCategory.apiValue(): String = when (this) {
    ProductCategory.FRUITS -> "fruits"
    ProductCategory.VEGETABLES -> "vegetables"
    ProductCategory.DAIRY -> "dairy"
    ProductCategory.BAKERY -> "bakery"
    ProductCategory.MEAT -> "meat"
    ProductCategory.FISH -> "fish"
    ProductCategory.DRINKS -> "drinks"
    ProductCategory.PASTA -> "pasta"
    ProductCategory.RICE -> "rice"
    ProductCategory.CLEANING -> "cleaning"
    ProductCategory.OTHER -> "other"
}


private fun String?.normalizeKey(): String? =
    this?.trim()
        ?.lowercase()
        ?.replace(" ", "_")
        ?.replace("-", "_")
        ?.takeIf { it.isNotBlank() }
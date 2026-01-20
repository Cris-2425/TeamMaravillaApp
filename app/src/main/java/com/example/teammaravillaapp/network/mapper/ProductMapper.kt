package com.example.teammaravillaapp.network.mapper

import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.network.dto.ProductDto

// -------- DTO -> DOMAIN --------

fun ProductDto.toDomain(): Product = Product(
    id = id,
    name = name,
    category = category?.toProductCategory() ?: ProductCategory.OTHER,
    imageUrl = imageUrl,
    imageRes = null // en remoto no mandamos imageRes; es local
)

// -------- DOMAIN -> DTO --------

fun Product.toDto(): ProductDto = ProductDto(
    id = id,
    name = name,
    category = category?.toApiString(),
    imageUrl = imageUrl
)

// -------- helpers --------

private fun String.toProductCategory(): ProductCategory {
    val key = trim()
        .lowercase()
        .replace(" ", "_")
        .replace("-", "_")

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
        else -> ProductCategory.OTHER
    }
}

private fun ProductCategory.toApiString(): String = when (this) {
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

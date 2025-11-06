package com.example.teammaravillaapp.model

import com.example.teammaravillaapp.R

object RecipeData {
    val recipes = listOf(
        Recipe(
            title = "Tortilla de Patata",
            imageRes = R.drawable.tortilla_de_patata,
            products = listOf(
                Product("Patata", category = ProductCategory.VEGETABLES),
                Product("Huevo", category = ProductCategory.DAIRY),
                Product("Cebolla", category = ProductCategory.VEGETABLES),
                Product("Aceite", category = ProductCategory.OTHER)
            )
        ),
        Recipe(
            title = "Ensalada",
            imageRes = R.drawable.ensalada,
            products = listOf(
                Product("Lechuga", category = ProductCategory.VEGETABLES),
                Product("Tomate", category = ProductCategory.VEGETABLES),
                Product("Cebolla", category = ProductCategory.VEGETABLES),
                Product("Aceitunas", category = ProductCategory.OTHER)
            )
        ),
        Recipe(
            title = "Pasta con pesto",
            imageRes = R.drawable.pasta_con_pesto,
            products = listOf(
                Product("Pasta", category = ProductCategory.PASTA),
                Product("Pesto", category = ProductCategory.OTHER),
                Product("Queso", category = ProductCategory.DAIRY)
            )
        ),
        Recipe(
            title = "Bizcocho",
            imageRes = R.drawable.bizcocho,
            products = listOf(
                Product("Harina", category = ProductCategory.OTHER),
                Product("Huevo", category = ProductCategory.DAIRY),
                Product("Azúcar", category = ProductCategory.OTHER),
                Product("Leche", category = ProductCategory.DAIRY)
            )
        ),
        Recipe(
            title = "Smoothie de plátano y mango",
            imageRes = R.drawable.smoothie_platano_mango,
            products = listOf(
                Product("Plátano", category = ProductCategory.FRUITS),
                Product("Mango", category = ProductCategory.FRUITS),
                Product("Yogur", category = ProductCategory.DAIRY)
            )
        )
    )
}
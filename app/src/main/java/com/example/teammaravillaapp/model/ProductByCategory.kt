package com.example.teammaravillaapp.model

import com.example.teammaravillaapp.R

object ProductByCategory {

    val currentList: List<Product> = listOf(
        Product("Leche", category = ProductCategory.DAIRY),
        Product("Huevos", category = ProductCategory.DAIRY),
        Product("Pan", category = ProductCategory.BAKERY),
        Product("Arroz", category = ProductCategory.RICE),
        Product("Café", category = ProductCategory.OTHER)
    )

    val recentUsed: List<Product> = listOf(
        Product("Harina", category = ProductCategory.PASTA),
        Product("Yogur", category = ProductCategory.DAIRY),
        Product("Atún", category = ProductCategory.FISH),
        Product("Queso", category = ProductCategory.DAIRY)
    )

    val byCategory: Map<ProductCategory, List<Product>> = mapOf(
        ProductCategory.FRUITS to listOf(
            Product("Manzana", imageRes = R.drawable.manzana, category = ProductCategory.FRUITS),
            Product("Plátano", category = ProductCategory.FRUITS),
            Product("Naranja", category = ProductCategory.FRUITS),
            Product("Pera", category = ProductCategory.FRUITS),
            Product("Fresa", category = ProductCategory.FRUITS),
            Product("Kiwi", category = ProductCategory.FRUITS),
            Product("Mango", category = ProductCategory.FRUITS),
            Product("Uva", category = ProductCategory.FRUITS),
            Product("Melón", category = ProductCategory.FRUITS),
            Product("Sandía", category = ProductCategory.FRUITS),
        ),
        ProductCategory.VEGETABLES to listOf(
            Product("Zanahoria", category = ProductCategory.VEGETABLES),
            Product("Lechuga", category = ProductCategory.VEGETABLES),
            Product("Tomate", category = ProductCategory.VEGETABLES),
            Product("Cebolla", category = ProductCategory.VEGETABLES),
            Product("Pimiento", category = ProductCategory.VEGETABLES),
            Product("Patata", category = ProductCategory.VEGETABLES),
            Product("Calabacín", category = ProductCategory.VEGETABLES),
            Product("Pepino", category = ProductCategory.VEGETABLES),
            Product("Ajo", category = ProductCategory.VEGETABLES),
            Product("Brócoli", category = ProductCategory.VEGETABLES),
        ),
        ProductCategory.MEAT to listOf(
            Product("Pollo", category = ProductCategory.MEAT),
            Product("Ternera", category = ProductCategory.MEAT),
            Product("Cerdo", category = ProductCategory.MEAT),
            Product("Pavo", category = ProductCategory.MEAT),
            Product("Cordero", category = ProductCategory.MEAT),
            Product("Jamón", category = ProductCategory.MEAT),
            Product("Chorizo", category = ProductCategory.MEAT),
            Product("Salchicha", category = ProductCategory.MEAT),
            Product("Albóndigas", category = ProductCategory.MEAT),
            Product("Carne picada", category = ProductCategory.MEAT),
        ),
        ProductCategory.FISH to listOf(
            Product("Atún", category = ProductCategory.FISH),
            Product("Salmón", category = ProductCategory.FISH),
            Product("Merluza", category = ProductCategory.FISH),
            Product("Bacalao", category = ProductCategory.FISH),
            Product("Sardina", category = ProductCategory.FISH),
            Product("Calamares", category = ProductCategory.FISH),
            Product("Gambas", category = ProductCategory.FISH),
            Product("Mejillón", category = ProductCategory.FISH),
            Product("Bonito", category = ProductCategory.FISH),
            Product("Lubina", category = ProductCategory.FISH),
        ),
        ProductCategory.DAIRY to listOf(
            Product("Leche", category = ProductCategory.DAIRY),
            Product("Yogur", category = ProductCategory.DAIRY),
            Product("Queso", category = ProductCategory.DAIRY),
            Product("Mantequilla", category = ProductCategory.DAIRY),
            Product("Kéfir", category = ProductCategory.DAIRY),
            Product("Nata", category = ProductCategory.DAIRY),
            Product("Requesón", category = ProductCategory.DAIRY),
            Product("Cuajada", category = ProductCategory.DAIRY),
            Product("Batido", category = ProductCategory.DAIRY),
            Product("Petit", category = ProductCategory.DAIRY),
        ),
        ProductCategory.BAKERY to listOf(
            Product("Pan", category = ProductCategory.BAKERY),
            Product("Bollo", category = ProductCategory.BAKERY),
            Product("Barra", category = ProductCategory.BAKERY),
            Product("Baguette", category = ProductCategory.BAKERY),
            Product("Pan de molde", category = ProductCategory.BAKERY),
            Product("Croissant", category = ProductCategory.BAKERY),
            Product("Donut", category = ProductCategory.BAKERY),
            Product("Napolitana", category = ProductCategory.BAKERY),
            Product("Torta", category = ProductCategory.BAKERY),
            Product("Empanada", category = ProductCategory.BAKERY),
        ),
        ProductCategory.CLEANING to listOf(
            Product("Detergente", category = ProductCategory.CLEANING),
            Product("Suavizante", category = ProductCategory.CLEANING),
            Product("Lejía", category = ProductCategory.CLEANING),
            Product("Lavavajillas", category = ProductCategory.CLEANING),
            Product("Multiusos", category = ProductCategory.CLEANING),
            Product("Bayetas", category = ProductCategory.CLEANING),
            Product("Estropajo", category = ProductCategory.CLEANING),
            Product("Ambientador", category = ProductCategory.CLEANING),
            Product("Papel cocina", category = ProductCategory.CLEANING),
            Product("Papel higiénico", category = ProductCategory.CLEANING),
        ),
    )
}
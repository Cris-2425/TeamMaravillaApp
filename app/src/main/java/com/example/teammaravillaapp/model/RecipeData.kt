package com.example.teammaravillaapp.model

import com.example.teammaravillaapp.R

/**
 * Datos de ejemplo para la sección de Recetas.
 */
object RecipeData {

    /** Listado de recetas de muestra para UI. */
    val recipes = listOf(
        Recipe(
            id = 1,
            title = "Tortilla de Patata",
            imageRes = R.drawable.tortilla_de_patata,
            products = listOf(Patata, Huevo, Cebolla, Aceite),
            instructions = """
                1. Pela y corta las patatas en láminas finas.
                2. Fríe las patatas con la cebolla hasta que estén tiernas.
                3. Bate los huevos y mézclalos con las patatas escurridas.
                4. Cuaja la mezcla en una sartén por ambos lados.
            """.trimIndent()
        ),

        Recipe(
            id = 2,
            title = "Ensalada Mixta",
            imageRes = R.drawable.ensalada,
            products = listOf(Lechuga, Tomate, Cebolla, Aceitunas),
            instructions = """
                1. Lava y corta todos los ingredientes.
                2. Mézclalos en un bol amplio.
                3. Aliña con aceite, vinagre y sal al gusto.
            """.trimIndent()
        ),

        Recipe(
            id = 3,
            title = "Pasta al Pesto",
            imageRes = R.drawable.pasta_con_pesto,
            products = listOf(Pasta, Pesto, Queso),
            instructions = """
                1. Cuece la pasta hasta que esté al dente.
                2. Mezcla la pasta caliente con el pesto.
                3. Añade queso rallado por encima antes de servir.
            """.trimIndent()
        ),

        Recipe(
            id = 4,
            title = "Bizcocho Casero",
            imageRes = R.drawable.bizcocho,
            products = listOf(Harina, Huevo, Azucar, Leche),
            instructions = """
                1. Mezcla todos los ingredientes en un bol.
                2. Vierte la mezcla en un molde engrasado.
                3. Hornea a 180ºC durante 35-40 minutos.
            """.trimIndent()
        ),

        Recipe(
            id = 5,
            title = "Smoothie de Plátano y Mango",
            imageRes = R.drawable.smoothie_platano_mango,
            products = listOf(Platano, Mango, Yogur),
            instructions = """
                1. Pela el plátano y el mango.
                2. Mézclalos con el yogur en una batidora.
                3. Bate hasta obtener una textura homogénea.
            """.trimIndent()
        )
    )

    /** Devuelve una receta específica por ID */
    fun getRecipeById(id: Int): Recipe? =
        recipes.firstOrNull { it.id == id }
}
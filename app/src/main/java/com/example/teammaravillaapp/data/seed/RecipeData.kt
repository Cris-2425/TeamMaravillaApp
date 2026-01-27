package com.example.teammaravillaapp.data.seed

import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.model.Aceite
import com.example.teammaravillaapp.model.Aceitunas
import com.example.teammaravillaapp.model.Azucar
import com.example.teammaravillaapp.model.Cebolla
import com.example.teammaravillaapp.model.Harina
import com.example.teammaravillaapp.model.Huevo
import com.example.teammaravillaapp.model.Leche
import com.example.teammaravillaapp.model.Lechuga
import com.example.teammaravillaapp.model.Mango
import com.example.teammaravillaapp.model.Pasta
import com.example.teammaravillaapp.model.Patata
import com.example.teammaravillaapp.model.Pesto
import com.example.teammaravillaapp.model.Platano
import com.example.teammaravillaapp.model.Queso
import com.example.teammaravillaapp.model.Recipe
import com.example.teammaravillaapp.model.Tomate
import com.example.teammaravillaapp.model.Yogur

/**
 * SOLO usado como seed inicial de Room.
 * No usar directamente desde UI.
 */
object RecipeData {

    val recipes = listOf(
        Recipe(
            id = 1,
            title = "Tortilla de Patata",
            imageRes = R.drawable.tortilla_de_patata,
            productIds = listOf(Patata.id, Huevo.id, Cebolla.id, Aceite.id),
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
            productIds = listOf(Lechuga.id, Tomate.id, Cebolla.id, Aceitunas.id),
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
            productIds = listOf(Pasta.id, Pesto.id, Queso.id),
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
            productIds = listOf(Harina.id, Huevo.id, Azucar.id, Leche.id),
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
            productIds = listOf(Platano.id, Mango.id, Yogur.id),
            instructions = """
                1. Pela el plátano y el mango.
                2. Mézclalos con el yogur en una batidora.
                3. Bate hasta obtener una textura homogénea.
            """.trimIndent()
        )
    )

    fun getRecipeById(id: Int): Recipe? =
        recipes.firstOrNull { it.id == id }
}
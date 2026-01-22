package com.example.teammaravillaapp.model

import com.example.teammaravillaapp.R

/**
 * Datos de ejemplo de listas sugeridas para el flujo de creación.
 *
 * Se usan nombres de texto directos para evitar depender de contexto
 * en inicialización estática (funciona bien en Preview).
 */
object SuggestedListData {
    val items: List<SuggestedList> = listOf(
        SuggestedList(
            name = "Compra semanal",
            imageRes = R.drawable.fondo_farmacia,
            productIds = listOf("leche", "pan", "huevo", "tomate")
        ),
        SuggestedList(
            name = "BBQ sábado",
            imageRes = R.drawable.fondo_bbq,
            productIds = listOf("carne_picada", "chorizo", "pan", "cocacola")
        )
    )
}
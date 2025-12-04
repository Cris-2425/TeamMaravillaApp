package com.example.teammaravillaapp.model

import com.example.teammaravillaapp.R

/**
 * Datos de ejemplo para las listas sugeridas que aparecen en CreateList.
 *
 * Usa [SuggestedListName] para obtener los textos desde strings.xml,
 * garantizando localización y consistencia.
 */
object SuggestedListData {

    val items: List<SuggestedList> =
        listOf(
            SuggestedList(
                name = SuggestedListName.WEEKLY_SHOP.label,
                imageRes = R.drawable.fondo_farmacia
            ),
            SuggestedList(
                name = SuggestedListName.BBQ.label,
                imageRes = R.drawable.fondo_bbq
            ),
            SuggestedList(
                name = SuggestedListName.BREAKFAST.label,
                imageRes = R.drawable.fondo_desayuno
            ),
            SuggestedList(
                name = SuggestedListName.CLEANING.label,
                imageRes = R.drawable.fondo_limpieza
            )
        )
}
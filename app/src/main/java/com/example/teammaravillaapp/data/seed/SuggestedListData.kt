package com.example.teammaravillaapp.data.seed

import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.model.SuggestedList

/**
 * Listas sugeridas (plantillas) para el flujo de creación de listas.
 *
 * Motivo:
 * - Ofrecer al usuario plantillas rápidas (ej. "Compra semanal", "BBQ sábado").
 * - Evitar depender de repositorios/Room para mostrar sugerencias iniciales.
 *
 * Importante:
 * - [SuggestedList.productIds] debe contener IDs válidos del catálogo (los mismos que [ProductData.byId]).
 * - Si cambias el normalizador de IDs o el catálogo, revisa estas plantillas.
 *
 * Nota de implementación:
 * - Se usan strings directos (no `stringResource`) para permitir inicialización estática y previews.
 */
object SuggestedListData {

    /** Plantillas disponibles en la app. */
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
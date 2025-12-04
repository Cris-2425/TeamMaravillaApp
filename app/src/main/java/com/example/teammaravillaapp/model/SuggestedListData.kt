package com.example.teammaravillaapp.model

import com.example.teammaravillaapp.R

/**
 * Datos de ejemplo de listas sugeridas para el flujo de creación.
 *
 * Se usan nombres de texto directos para evitar depender de contexto
 * en inicialización estática (funciona bien en Preview).
 */
object SuggestedListData {
    /** Conjunto fijo de sugerencias básicas. */
    val items: List<SuggestedList> =
        listOf(
            SuggestedList("Compra semanal", R.drawable.fondo_farmacia),
            SuggestedList("BBQ sábado", R.drawable.fondo_bbq),
            SuggestedList("Desayunos", R.drawable.fondo_desayuno),
            SuggestedList("Limpieza", R.drawable.fondo_limpieza)
        )
}
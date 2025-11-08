package com.example.teammaravillaapp.model

import androidx.annotation.DrawableRes

/**
 * Plantillas de listas sugeridas en la creación de lista.
 *
 * @property name título visible (se rellena como nombre por defecto).
 * @property imageRes imagen de fondo de la “burbuja” sugerida.
 */
data class SuggestedList(
    val name: String,
    @DrawableRes val imageRes: Int
)
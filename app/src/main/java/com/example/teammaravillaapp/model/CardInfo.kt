package com.example.teammaravillaapp.model

import androidx.annotation.DrawableRes

/**
 * Información mostrada en una tarjeta (por ejemplo, en la Home) para representar una lista.
 *
 * Este modelo está pensado para capas de presentación: agrupa los datos mínimos necesarios
 * para renderizar una tarjeta con icono/imagen, título y subtítulo.
 *
 * @property imageResId Recurso drawable que se muestra como icono/imagen de la tarjeta.
 * Debe ser un ID válido de drawable (`R.drawable.*`).
 * @property imageDescription Descripción accesible de la imagen. Útil para TalkBack.
 * Si no se desea accesibilidad específica, puede ser `null`.
 * @property title Título principal de la tarjeta (por ejemplo, nombre de la lista). No debería ser vacío.
 * @property subtitle Texto secundario de contexto (por ejemplo, “2/12 comprados”). Puede ser vacío si no aplica.
 */
data class CardInfo(
    @DrawableRes val imageID: Int,
    val imageDescription: String? = null,
    val title: String,
    val subtitle: String
)

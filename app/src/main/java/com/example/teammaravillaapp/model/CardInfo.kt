package com.example.teammaravillaapp.model

import androidx.annotation.DrawableRes

/**
 * Información mostrada en una tarjeta (por ejemplo, en la Home) para representar una lista.
 *
 * Este modelo está pensado para la **capa de presentación**, ya que agrupa los datos mínimos
 * necesarios para renderizar una tarjeta con icono/imagen, título y subtítulo.
 *
 * Uso típico:
 * ```kotlin
 * val card = CardInfo(
 *     imageID = R.drawable.ic_list_icon,
 *     imageDescription = "Icono de lista de compras",
 *     title = "Lista semanal",
 *     subtitle = "2/12 comprados"
 * )
 * ```
 *
 * @property imageID Recurso drawable que se muestra como icono/imagen de la tarjeta.
 * Debe ser un ID válido de drawable (`R.drawable.*`).
 * @property imageDescription Descripción accesible de la imagen. Útil para TalkBack.
 * Puede ser `null` si no se requiere accesibilidad específica.
 * @property title Título principal de la tarjeta (por ejemplo, nombre de la lista). No debería estar vacío.
 * @property subtitle Texto secundario de contexto (por ejemplo, “2/12 comprados”). Puede ser vacío si no aplica.
 */
data class CardInfo(
    @DrawableRes val imageID: Int,
    val imageDescription: String? = null,
    val title: String,
    val subtitle: String
)
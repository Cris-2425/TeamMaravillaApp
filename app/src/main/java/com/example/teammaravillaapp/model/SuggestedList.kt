package com.example.teammaravillaapp.model

import androidx.annotation.DrawableRes

/**
 * Plantillas de listas sugeridas para la creación rápida de nuevas listas.
 *
 * Este modelo se utiliza en la pantalla de **creación de listas** para
 * mostrar burbujas o tarjetas de listas predefinidas con un fondo y
 * productos recomendados.
 *
 * Ejemplo de uso:
 * ```kotlin
 * val sugerida = SuggestedList(
 *     name = "Lista de frutas",
 *     imageRes = R.drawable.bg_fruits,
 *     productIds = listOf("Manzana", "Plátano", "Kiwi")
 * )
 * ```
 *
 * @property name Título visible de la lista sugerida.
 * @property imageRes Recurso drawable de imagen/fondo de la burbuja sugerida.
 * @property productIds Lista de IDs de productos preseleccionados para la plantilla.
 */
data class SuggestedList(
    val name: String,
    @DrawableRes val imageRes: Int,
    val productIds: List<String> = emptyList()
)
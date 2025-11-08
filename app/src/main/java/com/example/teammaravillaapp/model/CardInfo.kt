package com.example.teammaravillaapp.model

/**
 * Información mostrada en una tarjeta de lista (ListCard).
 *
 * Se usa principalmente en la pantalla Home para representar listas recientes o guardadas.
 *
 * @property imageID Recurso drawable de la imagen que acompaña a la lista.
 * @property imageDescription Descripción opcional de la imagen (para accesibilidad y logs).
 * @property title Nombre principal de la lista o elemento.
 * @property subtitle Descripción corta o contexto adicional (ej. “Supermercado”, “Carnicería”).
 */
data class CardInfo(
    val imageID: Int,
    val imageDescription: String = "",
    val title: String,
    val subtitle: String
)
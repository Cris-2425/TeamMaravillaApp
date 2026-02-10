package com.example.teammaravillaapp.model

/**
 * Estilos de visualización posibles para una lista.
 *
 * Este enum define cómo se puede renderizar una lista en la UI.
 * Actualmente no se aplican en todas las pantallas, pero quedan definidos
 * para futuras vistas o ajustes de preferencia del usuario.
 *
 * Ejemplo de uso:
 * ```kotlin
 * val style: ListStyle = ListStyle.MOSAIC
 * ```
 */
enum class ListStyle {
    LISTA,
    MOSAIC,
    ETC
}
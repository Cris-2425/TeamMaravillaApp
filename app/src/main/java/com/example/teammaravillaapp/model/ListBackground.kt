package com.example.teammaravillaapp.model

/**
 * Fondos disponibles para las listas del usuario.
 *
 * Estos valores se usan principalmente en la **capa de datos de seed** y en la
 * **presentación** para asignar fondos a las listas de forma consistente.
 *
 * Uso:
 * ```kotlin
 * val fondo = ListBackground.FONDO1
 * val resId = ListBackgrounds.getBackgroundRes(fondo)
 * ```
 */
enum class ListBackground {
    FONDO1,
    FONDO2,
    FONDO3,
    FONDO4
}
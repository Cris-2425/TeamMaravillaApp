package com.example.teammaravillaapp.model

/**
 * Modelo simple que representa el estado de un campo de búsqueda (**SearchField**).
 *
 * Este modelo se utiliza en pantallas como **Home** o **ListDetail** para vincular
 * el texto introducido por el usuario y el placeholder sin declarar variables separadas.
 *
 * Ejemplo de uso:
 * ```kotlin
 * var searchField = SearchFieldData(value = "", placeholder = "Buscar productos...")
 * ```
 *
 * @property value Texto actual introducido por el usuario.
 * @property placeholder Texto gris que se muestra cuando el campo está vacío.
 */
data class SearchFieldData(
    val value: String,
    val placeholder: String
)
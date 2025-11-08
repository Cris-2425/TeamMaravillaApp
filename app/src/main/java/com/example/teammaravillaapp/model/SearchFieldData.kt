package com.example.teammaravillaapp.model

/**
 * Modelo simple que representa el estado de un campo de búsqueda (SearchField).
 *
 * Usado en pantallas como Home o ListDetail para vincular texto y placeholder
 * sin tener que declarar variables por separado.
 *
 * @property value Texto actual introducido por el usuario.
 * @property placeholder Texto gris que se muestra cuando el campo está vacío.
 */
data class SearchFieldData(
    val value: String,
    val placeholder: String
)
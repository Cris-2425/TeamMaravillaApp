package com.example.teammaravillaapp.model

/**
 * Lista creada por el usuario.
 *
 * @property name nombre visible de la lista.
 * @property background fondo elegido (enum).
 * @property products productos actualmente incluidos en la lista.
 */
data class UserList(
    val id: String,
    val name: String,
    val background: ListBackground,
    val products: List<Product> = emptyList()
)
package com.example.teammaravillaapp.model

/**
 * Lista creada por el usuario.
 *
 * @property name Nombre visible de la lista.
 * @property background Fondo elegido.
 * @property products Productos incluidos en la lista.
 */
data class UserList(
    val name: String,
    val background: ListBackground,
    val products: List<Product> = emptyList()
)

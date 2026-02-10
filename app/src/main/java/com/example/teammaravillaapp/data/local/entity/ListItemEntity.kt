package com.example.teammaravillaapp.data.local.entity

import androidx.room.Entity

/**
 * Representa un item dentro de una lista de usuario.
 *
 * La clave primaria es compuesta: [listId] + [productId].
 *
 * Tabla: list_items
 *
 * @property listId ID de la lista a la que pertenece el item.
 * @property productId ID del producto.
 * @property addedAt Timestamp de cuando se añadió el producto a la lista.
 * @property position Posición del item en la lista (para ordenar).
 * @property checked Indica si el item ha sido marcado como comprado.
 * @property quantity Cantidad del producto en la lista (mínimo 1).
 */
@Entity(
    tableName = "list_items",
    primaryKeys = ["listId", "productId"]
)
data class ListItemEntity(
    val listId: String,
    val productId: String,
    val addedAt: Long,
    val position: Int,
    val checked: Boolean = false,
    val quantity: Int = 1
)
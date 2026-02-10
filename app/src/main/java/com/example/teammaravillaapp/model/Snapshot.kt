package com.example.teammaravillaapp.model

/**
 * Instantánea de una lista de usuario (**UserListSnapshot**) para persistencia o sincronización.
 *
 * Este modelo se usa para **almacenamiento local** (Room, DataStore) o para
 * sincronización remota, manteniendo los datos de la lista y sus elementos.
 *
 * Ejemplo de uso:
 * ```kotlin
 * val snapshot = UserListSnapshot(
 *     id = "123",
 *     name = "Lista de la compra",
 *     background = ListBackground.FONDO1,
 *     createdAt = System.currentTimeMillis(),
 *     items = listOf(
 *         ListItemSnapshot("Manzana", System.currentTimeMillis(), 0, false, 2)
 *     )
 * )
 * ```
 *
 * @property id Identificador único de la lista.
 * @property name Nombre visible de la lista.
 * @property background Fondo asociado a la lista (enum [ListBackground]).
 * @property createdAt Timestamp de creación en milisegundos.
 * @property items Lista de elementos contenidos en la lista ([ListItemSnapshot]).
 */
data class UserListSnapshot(
    val id: String,
    val name: String,
    val background: ListBackground,
    val createdAt: Long,
    val items: List<ListItemSnapshot>
)

/**
 * Representa un elemento individual de una lista de usuario (**ListItemSnapshot**).
 *
 * Este modelo mantiene la referencia al producto por ID y datos de estado
 * como posición, cantidad y si está marcado como comprado.
 *
 * @property productId ID del producto asociado ([Product.id]).
 * @property addedAt Timestamp de cuándo se añadió el producto a la lista.
 * @property position Posición del elemento en la lista (para ordenamiento en UI).
 * @property checked Indica si el producto fue marcado como comprado.
 * @property quantity Cantidad del producto en la lista (entero, opcionalmente 0 si no aplica).
 */
data class ListItemSnapshot(
    val productId: String,
    val addedAt: Long,
    val position: Int,
    val checked: Boolean,
    val quantity: Int
)
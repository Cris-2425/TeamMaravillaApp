package com.example.teammaravillaapp.model

/**
 * Representa una lista creada por el usuario (**UserList**).
 *
 * Este modelo se utiliza en la capa de dominio y UI para gestionar
 * listas activas, sus productos y apariencia visual.
 *
 * Ejemplo de uso:
 * ```kotlin
 * val lista = UserList(
 *     name = "Lista semanal",
 *     background = ListBackground.FONDO1,
 *     productIds = listOf("Manzana", "Leche", "Pan")
 * )
 * ```
 *
 * @property id Identificador único de la lista. Se genera automáticamente por defecto.
 * @property name Nombre visible de la lista.
 * @property background Fondo elegido para la lista (enum [ListBackground]).
 * @property productIds Lista de IDs de productos incluidos en la lista ([Product.id]).
 */
data class UserList(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val background: ListBackground,
    val productIds: List<String> = emptyList()
)
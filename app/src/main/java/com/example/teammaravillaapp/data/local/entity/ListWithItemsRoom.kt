package com.example.teammaravillaapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Representa una lista con todos sus elementos asociados en la base de datos local.
 *
 * Este data class se utiliza para obtener una relación 1:N entre una [ListEntity]
 * y sus [ListItemEntity] correspondientes usando Room.
 *
 * @property list La entidad de la lista principal.
 * @property items Lista de elementos asociados a la lista.
 *
 * Ejemplo de uso con Room DAO:
 * ```
 * @Transaction
 * @Query("SELECT * FROM lists WHERE id = :listId")
 * suspend fun getListWithItems(listId: String): ListWithItemsRoom
 * ```
 */
data class ListWithItemsRoom(
    @Embedded val list: ListEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "listId"
    )
    val items: List<ListItemEntity>
)
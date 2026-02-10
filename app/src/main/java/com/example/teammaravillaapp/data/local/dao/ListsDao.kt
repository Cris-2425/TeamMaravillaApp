package com.example.teammaravillaapp.data.local.dao

import androidx.room.*
import com.example.teammaravillaapp.data.local.entity.ListEntity
import com.example.teammaravillaapp.data.local.entity.ListItemEntity
import com.example.teammaravillaapp.data.local.entity.ListWithItemsRoom
import kotlinx.coroutines.flow.Flow

/**
 * DAO para la gestión de listas de usuario y sus items.
 *
 * Incluye operaciones CRUD, consultas de progreso y métodos de conveniencia
 * para actualizar cantidades, checked y orden de items.
 */
@Dao
interface ListsDao {

    /** Observa todas las listas con sus items, ordenadas por fecha de creación. */
    @Transaction
    @Query("SELECT * FROM user_lists ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<ListWithItemsRoom>>

    /** Obtiene una lista específica con sus items (snapshot único). */
    @Transaction
    @Query("SELECT * FROM user_lists WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ListWithItemsRoom?

    /** Cantidad total de listas almacenadas. */
    @Query("SELECT COUNT(*) FROM user_lists")
    suspend fun count(): Int

    /** Inserta o reemplaza una lista. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(list: ListEntity)

    /** Elimina todas las listas. */
    @Query("DELETE FROM user_lists")
    suspend fun clear()

    /** Elimina una lista específica. */
    @Query("DELETE FROM user_lists WHERE id = :id")
    suspend fun deleteById(id: String)

    /**
     * Elimina una lista y sus items asociados (cascade manual).
     */
    @Transaction
    suspend fun deleteListCascade(id: String) {
        deleteItemsForList(id)
        deleteById(id)
    }

    /** Observa los items de una lista en tiempo real, ordenados por posición. */
    @Query("SELECT * FROM list_items WHERE listId = :listId ORDER BY position ASC")
    fun observeItems(listId: String): Flow<List<ListItemEntity>>

    /** Obtiene todos los items de una lista (snapshot). */
    @Query("SELECT * FROM list_items WHERE listId = :listId ORDER BY position ASC")
    suspend fun getItems(listId: String): List<ListItemEntity>

    /** Obtiene un item concreto de una lista por productId. */
    @Query("SELECT * FROM list_items WHERE listId = :listId AND productId = :productId LIMIT 1")
    suspend fun getItem(listId: String, productId: String): ListItemEntity?

    /** Obtiene la posición de un item en la lista. */
    @Query("SELECT position FROM list_items WHERE listId = :listId AND productId = :productId")
    suspend fun getItemPosition(listId: String, productId: String): Int?

    /** Elimina un item específico de la lista. */
    @Query("DELETE FROM list_items WHERE listId = :listId AND productId = :productId")
    suspend fun deleteItem(listId: String, productId: String)

    /** Ajusta posiciones de items tras eliminar uno para mantener orden. */
    @Query("""
        UPDATE list_items
        SET position = position - 1
        WHERE listId = :listId AND position > :fromPosition
    """)
    suspend fun shiftPositionsAfter(listId: String, fromPosition: Int)

    /** Elimina todos los items de una lista. */
    @Query("DELETE FROM list_items WHERE listId = :listId")
    suspend fun deleteItemsForList(listId: String)

    /** Inserta o reemplaza múltiples items. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertItems(items: List<ListItemEntity>)

    /** Inserta o reemplaza un solo item. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertItem(item: ListItemEntity)

    /** Cambia el estado `checked` de un item (toggle). */
    @Query("""
        UPDATE list_items
        SET checked = CASE checked WHEN 1 THEN 0 ELSE 1 END
        WHERE listId = :listId AND productId = :productId
    """)
    suspend fun toggleChecked(listId: String, productId: String)

    /** Actualiza la cantidad de un item. */
    @Query("""
        UPDATE list_items
        SET quantity = :quantity
        WHERE listId = :listId AND productId = :productId
    """)
    suspend fun setQuantity(listId: String, productId: String, quantity: Int)

    /** Marca o desmarca todos los items de una lista. */
    @Query("""
        UPDATE list_items
        SET checked = :checked
        WHERE listId = :listId
    """)
    suspend fun setAllChecked(listId: String, checked: Boolean)

    /** Elimina todos los items marcados como `checked`. */
    @Query("""
        DELETE FROM list_items
        WHERE listId = :listId AND checked = 1
    """)
    suspend fun deleteChecked(listId: String)

    /**
     * Observa el progreso de las listas para Home (checked/total).
     */
    @Query("""
        SELECT
            listId AS listId,
            COALESCE(SUM(CASE WHEN checked = 1 THEN 1 ELSE 0 END), 0) AS checkedCount,
            COUNT(*) AS totalCount
        FROM list_items
        GROUP BY listId
    """)
    fun observeProgress(): Flow<List<ListProgressRow>>

    /**
     * Reemplaza items de una lista preservando metadatos (`checked` y `quantity`).
     */
    @Transaction
    suspend fun replaceItemsPreserveMeta(
        listId: String,
        productIds: List<String>,
        baseTime: Long
    ) {
        val existing = getItems(listId).associateBy { it.productId }
        val distinct = productIds.distinct()
        val rebuilt = distinct.mapIndexed { index, pid ->
            val old = existing[pid]
            ListItemEntity(
                listId = listId,
                productId = pid,
                addedAt = old?.addedAt ?: (baseTime + index),
                position = index,
                checked = old?.checked ?: false,
                quantity = old?.quantity ?: 1
            )
        }
        deleteItemsForList(listId)
        if (rebuilt.isNotEmpty()) upsertItems(rebuilt)
    }

    /** Incrementa la cantidad de un item en 1. */
    @Query("UPDATE list_items SET quantity = quantity + 1 WHERE listId = :listId AND productId = :productId")
    suspend fun incQuantity(listId: String, productId: String)

    /** Decrementa la cantidad de un item, mínimo 1. */
    @Query("""
        UPDATE list_items 
        SET quantity = CASE WHEN quantity > 1 THEN quantity - 1 ELSE 1 END
        WHERE listId = :listId AND productId = :productId
    """)
    suspend fun decQuantityMin1(listId: String, productId: String)

    /** Snapshot de todas las listas con sus items (transacción). */
    @Transaction
    @Query("SELECT * FROM user_lists ORDER BY createdAt DESC")
    suspend fun getAllSnapshot(): List<ListWithItemsRoom>

    /** Borra todos los items. */
    @Query("DELETE FROM list_items")
    suspend fun clearItems()

    /** Borra todas las listas. */
    @Query("DELETE FROM user_lists")
    suspend fun clearLists()
}

/**
 * DTO de query agregada para progreso de listas.
 *
 * Este objeto no es una entidad, se usa solo para representar
 * checkedCount/totalCount de cada lista.
 *
 * @property listId ID de la lista.
 * @property checkedCount Cantidad de items marcados como comprados.
 * @property totalCount Total de items en la lista.
 */
data class ListProgressRow(
    val listId: String,
    val checkedCount: Int,
    val totalCount: Int
)
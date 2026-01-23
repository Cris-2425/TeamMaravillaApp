package com.example.teammaravillaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.teammaravillaapp.data.local.entity.ListEntity
import com.example.teammaravillaapp.data.local.entity.ListItemEntity
import com.example.teammaravillaapp.data.local.entity.ListWithItemsRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface ListsDao {

    // ---- LISTAS ----

    @Transaction
    @Query("SELECT * FROM user_lists ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<ListWithItemsRoom>>

    @Transaction
    @Query("SELECT * FROM user_lists WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ListWithItemsRoom?

    @Query("SELECT COUNT(*) FROM user_lists")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(list: ListEntity)

    @Query("DELETE FROM user_lists")
    suspend fun clear()

    // ---- ITEMS ----

    @Query("SELECT * FROM list_items WHERE listId = :listId ORDER BY position ASC")
    fun observeItems(listId: String): Flow<List<ListItemEntity>>

    @Query("SELECT * FROM list_items WHERE listId = :listId ORDER BY position ASC")
    suspend fun getItems(listId: String): List<ListItemEntity>

    @Query("SELECT position FROM list_items WHERE listId = :listId AND productId = :productId")
    suspend fun getItemPosition(listId: String, productId: String): Int?

    @Query("DELETE FROM list_items WHERE listId = :listId AND productId = :productId")
    suspend fun deleteItem(listId: String, productId: String)

    @Query(
        """
        UPDATE list_items
        SET position = position - 1
        WHERE listId = :listId AND position > :fromPosition
        """
    )
    suspend fun shiftPositionsAfter(listId: String, fromPosition: Int)

    @Query("DELETE FROM list_items WHERE listId = :listId")
    suspend fun deleteItemsForList(listId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertItems(items: List<ListItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertItem(item: ListItemEntity)

    @Query(
        """
        UPDATE list_items
        SET checked = CASE checked WHEN 1 THEN 0 ELSE 1 END
        WHERE listId = :listId AND productId = :productId
        """
    )
    suspend fun toggleChecked(listId: String, productId: String)

    @Query(
        """
        UPDATE list_items
        SET quantity = :quantity
        WHERE listId = :listId AND productId = :productId
        """
    )
    suspend fun setQuantity(listId: String, productId: String, quantity: Int)

    // ✅ marcar todos / desmarcar todos
    @Query(
        """
        UPDATE list_items
        SET checked = :checked
        WHERE listId = :listId
        """
    )
    suspend fun setAllChecked(listId: String, checked: Boolean)

    // ✅ borrar comprados
    @Query(
        """
        DELETE FROM list_items
        WHERE listId = :listId AND checked = 1
        """
    )
    suspend fun deleteChecked(listId: String)

    /**
     * ✅ NUEVO: progreso por lista (checked/total) para pintar en Home.
     * Devuelve 1 fila por listId.
     *
     * Nota: SUM() puede devolver null si no hay filas, por eso el COALESCE.
     */
    @Query(
        """
    SELECT
        listId AS listId,
        COALESCE(SUM(CASE WHEN checked = 1 THEN 1 ELSE 0 END), 0) AS checkedCount,
        COUNT(*) AS totalCount
    FROM list_items
    GROUP BY listId
    """
    )
    fun observeProgress(): Flow<List<ListProgressRow>>


    /**
     * Reemplazo completo de items, PRESERVANDO checked/quantity.
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
}

/**
 * DTO de query agregada (no es Entity).
 */
data class ListProgressRow(
    val listId: String,
    val checkedCount: Int,
    val totalCount: Int
)
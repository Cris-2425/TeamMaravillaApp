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

/**
 * Acceso a datos de **listas de usuario** y sus **items**.
 *
 * Modela dos tablas:
 * - `user_lists` (cabecera / metadatos de lista)
 * - `list_items` (líneas / items asociados)
 *
 * Expone lecturas reactivas mediante `Flow` y operaciones de actualización enfocadas a mantener:
 * - **Consistencia** (lecturas completas con `@Transaction`)
 * - **Orden estable** (`position` para render determinista)
 * - **Idempotencia** en sincronización (upserts y reemplazos completos)
 *
 * ## Concurrencia
 * - Los métodos `suspend` se ejecutan en el *dispatcher* de Room (no requieren `Dispatchers.IO`).
 * - Los `Flow` son **cold** y re-emiten al invalidarse las tablas consultadas.
 *
 * @see ListEntity
 * @see ListItemEntity
 * @see ListWithItemsRoom
 */
@Dao
interface ListsDao {

    /**
     * Observa todas las listas con sus items, ordenadas por fecha de creación descendente.
     *
     * Se usa `@Transaction` para evitar estados inconsistentes al resolver la relación lista-items.
     *
     * @return `Flow` con el conjunto de listas y sus items.
     */
    @Transaction
    @Query("SELECT * FROM user_lists ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<ListWithItemsRoom>>

    /**
     * Recupera una lista concreta con sus items (*snapshot*).
     *
     * @param id Identificador de la lista.
     * @return La lista con items o `null` si no existe.
     */
    @Transaction
    @Query("SELECT * FROM user_lists WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ListWithItemsRoom?

    /**
     * Cuenta cuántas listas existen en `user_lists`.
     *
     * @return Total de listas almacenadas.
     */
    @Query("SELECT COUNT(*) FROM user_lists")
    suspend fun count(): Int

    /**
     * Inserta o actualiza una lista.
     *
     * Se usa **REPLACE** para tratar la tabla como caché local: si existe el mismo `id`,
     * se sobrescribe el registro.
     *
     * @param list Entidad de lista a persistir.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(list: ListEntity)

    /**
     * Elimina todas las listas.
     *
     * Nota: no elimina items salvo que exista una FK con cascada en el esquema. Para borrado completo
     * controlado, usa [clearLists] + [clearItems] o [replaceAllFromRemote].
     */
    @Query("DELETE FROM user_lists")
    suspend fun clear()

    /**
     * Elimina una lista por ID.
     *
     * @param id Identificador de la lista.
     */
    @Query("DELETE FROM user_lists WHERE id = :id")
    suspend fun deleteById(id: String)

    /**
     * Elimina una lista y sus items asociados mediante **cascade manual**.
     *
     * ### Por qué “manual”
     * Permite mantener el comportamiento aunque el esquema no tenga FK con `ON DELETE CASCADE`,
     * y evita depender de la configuración de Room/SQLite para borrados en cadena.
     *
     * @param id Identificador de la lista a eliminar.
     *
     */
    @Transaction
    suspend fun deleteListCascade(id: String) {
        deleteItemsForList(id)
        deleteById(id)
    }

    /**
     * Observa los items de una lista, ordenados por `position`.
     *
     * @param listId ID de la lista.
     * @return `Flow` reactivo con los items de la lista.
     */
    @Query("SELECT * FROM list_items WHERE listId = :listId ORDER BY position ASC")
    fun observeItems(listId: String): Flow<List<ListItemEntity>>

    /**
     * Recupera los items de una lista (*snapshot*), ordenados por `position`.
     *
     * @param listId ID de la lista.
     * @return Lista de items (puede estar vacía).
     */
    @Query("SELECT * FROM list_items WHERE listId = :listId ORDER BY position ASC")
    suspend fun getItems(listId: String): List<ListItemEntity>

    /**
     * Recupera un item por `productId` dentro de una lista.
     *
     * @param listId ID de la lista.
     * @param productId ID del producto.
     * @return El item si existe, o `null` si no está en la lista.
     */
    @Query("SELECT * FROM list_items WHERE listId = :listId AND productId = :productId LIMIT 1")
    suspend fun getItem(listId: String, productId: String): ListItemEntity?

    /**
     * Recupera la posición (`position`) de un item dentro de la lista.
     *
     * Útil para operaciones de reordenado o para calcular desplazamientos tras borrado.
     *
     * @param listId ID de la lista.
     * @param productId ID del producto.
     * @return La posición o `null` si el item no existe.
     */
    @Query("SELECT position FROM list_items WHERE listId = :listId AND productId = :productId")
    suspend fun getItemPosition(listId: String, productId: String): Int?

    /**
     * Elimina un item de una lista.
     *
     * @param listId ID de la lista.
     * @param productId ID del producto.
     */
    @Query("DELETE FROM list_items WHERE listId = :listId AND productId = :productId")
    suspend fun deleteItem(listId: String, productId: String)

    /**
     * Recompacta posiciones tras eliminar un item, decrementando `position` a los items posteriores.
     *
     * ### Por qué existe
     * Mantiene un orden estable (sin “huecos”) y simplifica renderizado/drag&drop, evitando lógica de
     * normalización en la capa de UI.
     *
     * @param listId ID de la lista.
     * @param fromPosition Posición eliminada; se ajustan los items con `position > fromPosition`.
     */
    @Query(
        """
        UPDATE list_items
        SET position = position - 1
        WHERE listId = :listId AND position > :fromPosition
    """
    )
    suspend fun shiftPositionsAfter(listId: String, fromPosition: Int)

    /**
     * Elimina todos los items asociados a una lista.
     *
     * @param listId ID de la lista.
     */
    @Query("DELETE FROM list_items WHERE listId = :listId")
    suspend fun deleteItemsForList(listId: String)

    /**
     * Inserta o actualiza múltiples items.
     *
     * Se usa **REPLACE** para que actualizaciones de estado (checked/cantidad/posición) se apliquen sin
     * necesidad de una operación `UPDATE` específica por campo.
     *
     * @param items Items a persistir.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertItems(items: List<ListItemEntity>)

    /**
     * Inserta o actualiza un item.
     *
     * @param item Item a persistir.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertItem(item: ListItemEntity)

    /**
     * Alterna el flag `checked` de un item (toggle) de forma atómica en BD.
     *
     * ### Por qué en SQL
     * Evita lecturas previas y condiciones de carrera entre “leer estado” y “escribir estado” cuando
     * hay actualizaciones concurrentes.
     *
     * @param listId ID de la lista.
     * @param productId ID del producto.
     */
    @Query(
        """
        UPDATE list_items
        SET checked = CASE checked WHEN 1 THEN 0 ELSE 1 END
        WHERE listId = :listId AND productId = :productId
    """
    )
    suspend fun toggleChecked(listId: String, productId: String)

    /**
     * Establece la cantidad de un item.
     *
     * @param listId ID de la lista.
     * @param productId ID del producto.
     * @param quantity Cantidad a persistir.
     */
    @Query(
        """
        UPDATE list_items
        SET quantity = :quantity
        WHERE listId = :listId AND productId = :productId
    """
    )
    suspend fun setQuantity(listId: String, productId: String, quantity: Int)

    /**
     * Marca o desmarca todos los items de una lista.
     *
     * Útil para acciones masivas (“marcar todo como comprado / no comprado”).
     *
     * @param listId ID de la lista.
     * @param checked Nuevo estado para todos los items.
     */
    @Query(
        """
        UPDATE list_items
        SET checked = :checked
        WHERE listId = :listId
    """
    )
    suspend fun setAllChecked(listId: String, checked: Boolean)

    /**
     * Elimina todos los items marcados como `checked` en una lista.
     *
     * @param listId ID de la lista.
     */
    @Query(
        """
        DELETE FROM list_items
        WHERE listId = :listId AND checked = 1
    """
    )
    suspend fun deleteChecked(listId: String)

    /**
     * Observa el progreso agregado por lista: `checkedCount / totalCount`.
     *
     * Diseñado para Home: permite pintar progreso sin cargar toda la relación lista-items.
     *
     * ### Notas de consistencia
     * - Solo emite filas para listas que tengan al menos un item (por el `GROUP BY` sobre `list_items`).
     * - `checkedCount` usa `COALESCE` para proteger ante resultados nulos.
     *
     * @return `Flow` con filas de progreso por `listId`.
     *
     * @see ListProgressRow
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
     * Reemplaza los items de una lista preservando metadatos locales (`checked`, `quantity`, `addedAt`).
     *
     * ### Por qué existe
     * En flujos donde el usuario selecciona productos (o se sincroniza desde remoto) es común reconstruir
     * el set de items. Este método evita perder estado local importante (comprado/cantidad) y garantiza:
     * - Un orden estable basado en `productIds` (`position = index`)
     * - `addedAt` conservado si el item ya existía; si no, se deriva de `baseTime + index`
     *
     * Se aplica un `distinct()` para prevenir duplicados, manteniendo el primer orden de aparición.
     *
     * @param listId ID de la lista.
     * @param productIds IDs de producto que deben quedar en la lista como estado final.
     * @param baseTime Base temporal para `addedAt` de items nuevos (se incrementa por índice).
     *
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

    /**
     * Incrementa la cantidad de un item en `1`.
     *
     * @param listId ID de la lista.
     * @param productId ID del producto.
     */
    @Query("UPDATE list_items SET quantity = quantity + 1 WHERE listId = :listId AND productId = :productId")
    suspend fun incQuantity(listId: String, productId: String)

    /**
     * Decrementa la cantidad de un item con un mínimo de `1`.
     *
     * ### Por qué el mínimo se aplica en SQL
     * Evita lecturas previas y condiciones de carrera en actualizaciones concurrentes.
     *
     * @param listId ID de la lista.
     * @param productId ID del producto.
     */
    @Query(
        """
        UPDATE list_items 
        SET quantity = CASE WHEN quantity > 1 THEN quantity - 1 ELSE 1 END
        WHERE listId = :listId AND productId = :productId
    """
    )
    suspend fun decQuantityMin1(listId: String, productId: String)

    /**
     * Recupera todas las listas con sus items (*snapshot*) en una transacción.
     *
     * Pensado para exportación, precarga o sincronizaciones que requieran el estado completo en memoria.
     *
     * @return Listas con sus items, ordenadas por `createdAt` descendente.
     */
    @Transaction
    @Query("SELECT * FROM user_lists ORDER BY createdAt DESC")
    suspend fun getAllSnapshot(): List<ListWithItemsRoom>

    /**
     * Elimina todos los items de todas las listas.
     *
     * Usado típicamente en reemplazos completos o reseteos locales.
     */
    @Query("DELETE FROM list_items")
    suspend fun clearItems()

    /**
     * Elimina todas las listas.
     *
     * Usado típicamente en reemplazos completos o reseteos locales.
     */
    @Query("DELETE FROM user_lists")
    suspend fun clearLists()

    /**
     * Inserta o actualiza múltiples listas.
     *
     * @param lists Listas a persistir.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLists(lists: List<ListEntity>)

    /**
     * Reemplaza por completo el estado local con datos provenientes de remoto (listas + items).
     *
     * ### Por qué se borra primero
     * Trata la BD local como caché: el estado final debe reflejar exactamente la fuente remota,
     * evitando elementos obsoletos (listas/items eliminados en servidor).
     *
     * ## Concurrencia
     * `@Transaction` garantiza atomicidad: observadores no verán estados intermedios (parciales).
     *
     * @param lists Listas remotas a persistir.
     * @param items Items remotos a persistir.
     */
    @Transaction
    suspend fun replaceAllFromRemote(
        lists: List<ListEntity>,
        items: List<ListItemEntity>
    ) {
        clearItems()
        clearLists()
        if (lists.isNotEmpty()) upsertLists(lists)
        if (items.isNotEmpty()) upsertItems(items)
    }
}

/**
 * Proyección de consulta agregada para progreso de listas.
 *
 * No representa una tabla y **no** se persiste: se construye como resultado de una query que agrega
 * sobre `list_items` para obtener métricas de UI (comprados/total).
 *
 * @property listId ID de la lista.
 * @property checkedCount Número de items marcados como comprados.
 * @property totalCount Número total de items en la lista.
 */
data class ListProgressRow(
    val listId: String,
    val checkedCount: Int,
    val totalCount: Int
)
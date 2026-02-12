package com.example.teammaravillaapp.data.local.repository.lists

import com.example.teammaravillaapp.data.local.dao.ListsDao
import com.example.teammaravillaapp.data.local.entity.ListEntity
import com.example.teammaravillaapp.data.local.entity.ListItemEntity
import com.example.teammaravillaapp.data.local.mapper.toDomain
import com.example.teammaravillaapp.data.local.mapper.toEntity
import com.example.teammaravillaapp.data.local.mapper.toSnapshot
import com.example.teammaravillaapp.data.repository.lists.ListProgress
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.model.UserListSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación Room de la persistencia de **listas de usuario**.
 *
 * Actúa como adaptador entre:
 * - **Dominio** (`UserList`, `UserListSnapshot`)
 * - **Persistencia** (Room: entidades y DAO)
 *
 * ### Alcance
 * - CRUD de listas e items
 * - Operaciones atómicas sobre items (toggle, cantidades, reordenado)
 * - Construcción de *snapshots* para sincronización
 * - Rehidratación desde remoto reemplazando el estado local
 *
 * ## Concurrencia
 * - Seguro para uso concurrente: Room serializa escrituras y gestiona el dispatcher.
 * - Los métodos que reordenan o reconstruyen items se basan en queries SQL atómicas (vía DAO)
 *   para minimizar condiciones de carrera.
 *
 * @property dao DAO de listas, inyectado por Hilt.
 *
 * @see ListsDao
 */
@Singleton
class RoomListsRepository @Inject constructor(
    private val dao: ListsDao
) {

    /**
     * Observa todas las listas del usuario en forma de dominio.
     *
     * Esta transformación es **pura** y mantiene la UI desacoplada de Room.
     *
     * @return `Flow` que emite listas de dominio cada vez que cambia la BD.
     */
    fun observeLists(): Flow<List<UserList>> =
        dao.observeAll().map { roomLists -> roomLists.map { it.toDomain() } }

    /**
     * Observa los items de una lista específica.
     *
     * Se expone como entidades porque suelen consumirse tal cual para acciones inmediatas
     * (toggle/cantidad) y ya contienen lo necesario para persistencia.
     *
     * @param listId ID de la lista.
     * @return `Flow` con los items, ordenados por `position`.
     */
    fun observeItems(listId: String): Flow<List<ListItemEntity>> =
        dao.observeItems(listId)

    /**
     * Recupera un item concreto por `productId` dentro de una lista.
     *
     * @param listId ID de la lista.
     * @param productId ID del producto.
     * @return El item si existe, o `null` si no está presente.
     */
    suspend fun getItem(listId: String, productId: String): ListItemEntity? =
        dao.getItem(listId, productId)

    /**
     * Recupera una lista por ID en modelo de dominio.
     *
     * @param id ID de la lista.
     * @return `UserList` o `null` si no existe.
     */
    suspend fun get(id: String): UserList? =
        dao.getById(id)?.toDomain()

    /**
     * Inserta datos de demostración si la BD está vacía.
     *
     * ### Por qué aquí
     * Mantiene la lógica de inicialización encapsulada en la capa de datos y evita
     * dependencias de UI para poblar la app en primera ejecución.
     */
    suspend fun seedIfEmpty() {
        if (dao.count() > 0) return

        val id = UUID.randomUUID().toString()
        val demo = UserList(
            id = id,
            name = "Compra semanal",
            background = ListBackground.FONDO2,
            productIds = emptyList()
        )

        val createdAt = System.currentTimeMillis()
        dao.upsert(demo.toEntity(createdAt = createdAt))
        dao.replaceItemsPreserveMeta(id, demo.productIds, baseTime = createdAt)
    }

    /**
     * Crea una lista nueva asignándole un ID único.
     *
     * Se persiste la cabecera y se materializan los items a partir de `productIds`,
     * preservando metadatos cuando aplique (en este caso, lista nueva → defaults).
     *
     * @param list Lista de dominio (el `id` de entrada no se reutiliza).
     * @return ID generado para la nueva lista.
     */
    suspend fun add(list: UserList): String {
        val id = UUID.randomUUID().toString()
        val normalized = list.copy(id = id)
        val createdAt = System.currentTimeMillis()

        dao.upsert(normalized.toEntity(createdAt = createdAt))
        dao.replaceItemsPreserveMeta(id, normalized.productIds, baseTime = createdAt)

        return id
    }

    /**
     * Sustituye los productos de una lista preservando `checked` y `quantity` cuando sea posible.
     *
     * Se utiliza `createdAt` como referencia estable para `baseTime` de items nuevos, evitando que
     * un simple cambio de composición “recree” tiempos artificiales.
     *
     * @param id ID de la lista.
     * @param newProductIds IDs de productos que deben quedar en la lista.
     */
    suspend fun updateProductIds(id: String, newProductIds: List<String>) {
        val current = dao.getById(id) ?: return
        dao.replaceItemsPreserveMeta(id, newProductIds, baseTime = current.list.createdAt)
    }

    /**
     * Elimina una lista y todos sus ítems asociados.
     *
     * @param id ID de la lista.
     */
    suspend fun deleteById(id: String) {
        dao.deleteItemsForList(id)
        dao.deleteById(id)
    }

    /**
     * Renombra una lista manteniendo el resto de metadatos.
     *
     * @param id ID de la lista.
     * @param newName Nuevo nombre.
     */
    suspend fun rename(id: String, newName: String) {
        val current = dao.getById(id) ?: return
        dao.upsert(current.list.copy(name = newName))
    }

    /**
     * Añade un producto a una lista si todavía no existe como item.
     *
     * ### Por qué se evita duplicado aquí
     * Se mantiene una UX consistente (una línea por producto) sin depender de restricciones
     * de BD; además permite definir `position` como `existing.size` de forma determinista.
     *
     * @param listId ID de la lista.
     * @param productId ID del producto a añadir.
     */
    suspend fun addItem(listId: String, productId: String) {
        val existing = dao.getItems(listId)
        if (existing.any { it.productId == productId }) return

        val now = System.currentTimeMillis()
        val pos = existing.size
        dao.upsertItem(
            ListItemEntity(
                listId = listId,
                productId = productId,
                addedAt = now,
                position = pos,
                checked = false,
                quantity = 1
            )
        )
    }

    /**
     * Elimina un item y recompone el orden (`position`) del resto.
     *
     * Esta operación mantiene un orden sin huecos, lo que simplifica renderizado
     * y futuras inserciones/reordenados.
     *
     * @param listId ID de la lista.
     * @param productId ID del producto a eliminar.
     */
    suspend fun removeItem(listId: String, productId: String) {
        val removedPosition = dao.getItemPosition(listId, productId) ?: return
        dao.deleteItem(listId, productId)
        dao.shiftPositionsAfter(listId, removedPosition)
    }

    /**
     * Alterna el estado `checked` de un item.
     *
     * @param listId ID de la lista.
     * @param productId ID del producto.
     */
    suspend fun toggleChecked(listId: String, productId: String) =
        dao.toggleChecked(listId, productId)

    /**
     * Establece la cantidad de un item, aplicando un mínimo de `1`.
     *
     * @param listId ID de la lista.
     * @param productId ID del producto.
     * @param quantity Cantidad deseada (se normaliza a `>= 1`).
     */
    suspend fun setQuantity(listId: String, productId: String, quantity: Int) =
        dao.setQuantity(listId, productId, quantity.coerceAtLeast(1))

    /**
     * Marca o desmarca todos los items de una lista.
     *
     * @param listId ID de la lista.
     * @param checked Estado objetivo.
     */
    suspend fun setAllChecked(listId: String, checked: Boolean) =
        dao.setAllChecked(listId, checked)

    /**
     * Elimina items `checked` y renormaliza posiciones.
     *
     * ### Por qué renormaliza
     * `DELETE` puede dejar huecos en `position`. Esta rutina reconstruye el orden en memoria
     * y lo persiste como estado final determinista.
     *
     * @param listId ID de la lista.
     */
    suspend fun clearChecked(listId: String) {
        dao.deleteChecked(listId)

        val remaining = dao.getItems(listId)
            .sortedBy { it.position }
            .mapIndexed { idx, it -> it.copy(position = idx) }

        dao.deleteItemsForList(listId)
        if (remaining.isNotEmpty()) dao.upsertItems(remaining)
    }

    /**
     * Observa el progreso por lista como `Map<listId, ListProgress>`.
     *
     * @return `Flow` que emite el mapa de progreso cada vez que cambian los items.
     *
     * @see ListProgress
     */
    fun observeProgress(): Flow<Map<String, ListProgress>> =
        dao.observeProgress().map { rows ->
            rows.associate { r -> r.listId to ListProgress(r.checkedCount, r.totalCount) }
        }

    /**
     * Incrementa la cantidad de un item.
     *
     * @param listId ID de la lista.
     * @param productId ID del producto.
     */
    suspend fun incQuantity(listId: String, productId: String) =
        dao.incQuantity(listId, productId)

    /**
     * Decrementa la cantidad de un item con mínimo `1`.
     *
     * @param listId ID de la lista.
     * @param productId ID del producto.
     */
    suspend fun decQuantityMin1(listId: String, productId: String) =
        dao.decQuantityMin1(listId, productId)

    // ----------------- Sincronización remota -----------------

    /**
     * Construye *snapshots* completos (lista + items) para sincronización.
     *
     * @return Estado actual como lista de [UserListSnapshot].
     */
    suspend fun snapshotWithItems(): List<UserListSnapshot> =
        dao.getAllSnapshot().map { it.toSnapshot() }

    /**
     * Persiste masivamente listas e items provenientes de remoto, reemplazando el estado local.
     *
     * ### Por qué reemplazo total
     * En sincronización “fuente de verdad” se necesita eliminar también lo que ya no existe en remoto.
     * Este método evita merges complejos a costa de sobrescribir el caché local.
     *
     * @param remote Snapshots remotos con `createdAt`, `items` y metadatos.
     *
     * @see ListsDao.replaceAllFromRemote
     */
    suspend fun saveAllFromRemote(remote: List<UserListSnapshot>) {
        val lists = remote.map { snap ->
            ListEntity(
                id = snap.id,
                name = snap.name,
                background = snap.background.name,
                createdAt = snap.createdAt
            )
        }

        val items = remote.flatMap { snap ->
            snap.items.map { it ->
                ListItemEntity(
                    listId = snap.id,
                    productId = it.productId,
                    addedAt = it.addedAt,
                    position = it.position,
                    checked = it.checked,
                    quantity = it.quantity
                )
            }
        }

        dao.replaceAllFromRemote(lists, items)
    }
}
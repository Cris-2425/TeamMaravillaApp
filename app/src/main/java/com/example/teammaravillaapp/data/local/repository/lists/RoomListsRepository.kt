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
 * Repositorio que gestiona la persistencia de listas de usuario y sus ítems utilizando Room.
 *
 * Esta clase implementa la capa de datos del patrón Clean Architecture.
 * Proporciona operaciones CRUD sobre listas y sus ítems, observables mediante [Flow].
 * También contiene métodos auxiliares para sincronización con fuentes remotas y manejo de snapshots.
 *
 * @property dao Instancia de [ListsDao] proporcionada por Hilt para acceso a la base de datos local.
 */
@Singleton
class RoomListsRepository @Inject constructor(
    private val dao: ListsDao
) {

    /**
     * Observa todas las listas del usuario como un flujo reactivo.
     *
     * Convierte las entidades de Room ([ListEntity]) a objetos de dominio ([UserList]).
     *
     * @return [Flow] que emite la lista actualizada de [UserList] cada vez que cambia la base de datos.
     */
    fun observeLists(): Flow<List<UserList>> =
        dao.observeAll().map { roomLists -> roomLists.map { it.toDomain() } }

    /**
     * Observa los ítems de una lista específica.
     *
     * @param listId ID de la lista cuyos ítems se desean observar.
     * @return [Flow] que emite los [ListItemEntity] de la lista cada vez que cambian.
     */
    fun observeItems(listId: String): Flow<List<ListItemEntity>> =
        dao.observeItems(listId)

    /**
     * Obtiene un ítem específico de una lista.
     *
     * @param listId ID de la lista.
     * @param productId ID del producto.
     * @return [ListItemEntity] si existe, null si no se encuentra.
     */
    suspend fun getItem(listId: String, productId: String): ListItemEntity? =
        dao.getItem(listId, productId)

    /**
     * Obtiene una lista por su ID.
     *
     * @param id ID de la lista.
     * @return [UserList] si existe, null si no se encuentra.
     */
    suspend fun get(id: String): UserList? =
        dao.getById(id)?.toDomain()

    /**
     * Inserta una lista de demostración si la base de datos está vacía.
     *
     * Este método es útil para inicializar la app con datos de ejemplo.
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
     * Agrega una nueva lista.
     *
     * @param list [UserList] a agregar.
     * @return ID generado de la nueva lista.
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
     * Actualiza los IDs de productos de una lista existente.
     *
     * @param id ID de la lista.
     * @param newProductIds Nueva lista de IDs de productos.
     */
    suspend fun updateProductIds(id: String, newProductIds: List<String>) {
        val current = dao.getById(id) ?: return
        dao.replaceItemsPreserveMeta(id, newProductIds, baseTime = current.list.createdAt)
    }

    /**
     * Elimina una lista y todos sus ítems asociados.
     *
     * @param id ID de la lista a eliminar.
     */
    suspend fun deleteById(id: String) {
        dao.deleteItemsForList(id)
        dao.deleteById(id)
    }

    /**
     * Renombra una lista existente.
     *
     * @param id ID de la lista.
     * @param newName Nuevo nombre para la lista.
     */
    suspend fun rename(id: String, newName: String) {
        val current = dao.getById(id) ?: return
        dao.upsert(current.list.copy(name = newName))
    }

    /**
     * Agrega un ítem a una lista si no existe previamente.
     *
     * @param listId ID de la lista.
     * @param productId ID del producto a agregar.
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
     * Elimina un ítem de una lista y ajusta las posiciones de los restantes.
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
     * Cambia el estado de completado de un ítem.
     *
     * @param listId ID de la lista.
     * @param productId ID del producto.
     */
    suspend fun toggleChecked(listId: String, productId: String) =
        dao.toggleChecked(listId, productId)

    /**
     * Establece la cantidad de un ítem, garantizando un mínimo de 1.
     *
     * @param listId ID de la lista.
     * @param productId ID del producto.
     * @param quantity Cantidad deseada (mínimo 1).
     */
    suspend fun setQuantity(listId: String, productId: String, quantity: Int) =
        dao.setQuantity(listId, productId, quantity.coerceAtLeast(1))

    /**
     * Marca o desmarca todos los ítems de una lista.
     *
     * @param listId ID de la lista.
     * @param checked Estado deseado.
     */
    suspend fun setAllChecked(listId: String, checked: Boolean) = dao.setAllChecked(listId, checked)

    /**
     * Elimina todos los ítems marcados como completados y reorganiza posiciones.
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
     * Observa el progreso de todas las listas (ítems completados / total).
     *
     * @return [Flow] que emite un [Map] de ID de lista a [ListProgress].
     */
    fun observeProgress(): Flow<Map<String, ListProgress>> =
        dao.observeProgress().map { rows ->
            rows.associate { r -> r.listId to ListProgress(r.checkedCount, r.totalCount) }
        }

    /** Incrementa la cantidad de un ítem. */
    suspend fun incQuantity(listId: String, productId: String) = dao.incQuantity(listId, productId)

    /** Decrementa la cantidad de un ítem, mínimo 1. */
    suspend fun decQuantityMin1(listId: String, productId: String) =
        dao.decQuantityMin1(listId, productId)

    // ----------------- Sincronización remota -----------------

    /**
     * Obtiene snapshots completos de todas las listas y sus ítems para sincronización.
     *
     * @return Lista de [UserListSnapshot] representando el estado actual.
     */
    suspend fun snapshotWithItems(): List<UserListSnapshot> =
        dao.getAllSnapshot().map { it.toSnapshot() }

    /**
     * Guarda masivamente listas y ítems provenientes de un origen remoto.
     *
     * Este método reemplaza completamente los datos locales.
     *
     * @param remote Lista de [UserListSnapshot] obtenida de remoto.
     */
    suspend fun saveAllFromRemote(remote: List<UserListSnapshot>) {
        dao.clearItems()
        dao.clearLists()

        remote.forEach { snap ->
            dao.upsert(
                ListEntity(
                    id = snap.id,
                    name = snap.name,
                    background = snap.background.name,
                    createdAt = snap.createdAt
                )
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

        if (items.isNotEmpty()) dao.upsertItems(items)
    }
}
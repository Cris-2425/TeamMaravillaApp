package com.example.teammaravillaapp.data.repository.lists

import com.example.teammaravillaapp.data.local.entity.ListItemEntity
import com.example.teammaravillaapp.model.UserList
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio de listas de usuario.
 *
 * Centraliza la gestión de listas y sus ítems,
 * actuando como **Single Source of Truth** para la UI.
 *
 * ### Estrategia de datos
 * - Room como fuente principal.
 * - Exposición reactiva mediante `Flow`.
 * - Operaciones atómicas y masivas para mantener consistencia.
 *
 * Diseñado para soportar:
 * - Listas de compra
 * - Progreso en tiempo real
 * - Sincronización remota (best-effort)
 */
interface ListsRepository {

    /**
     * Flujo reactivo de listas de usuario.
     *
     * Emite automáticamente ante cualquier cambio local.
     */
    fun observeLists(): Flow<List<UserList>>

    /**
     * Alias de compatibilidad para código legacy.
     *
     * Preferir [observeLists] en nuevo código.
     */
    val lists: Flow<List<UserList>>
        get() = observeLists()

    /**
     * Inicializa la base de datos con listas seed si está vacía.
     *
     * No sobrescribe datos existentes.
     */
    suspend fun seedIfEmpty()

    /**
     * Crea una nueva lista.
     *
     * @param list modelo de lista a persistir.
     * @return ID generado de la lista.
     */
    suspend fun add(list: UserList): String

    /**
     * Obtiene una lista por ID de forma no reactiva.
     */
    suspend fun get(id: String): UserList?

    /**
     * Reemplaza el conjunto de productos de una lista.
     *
     * ### Uso principal
     * - Merges masivos desde backend o seed.
     *
     * Preserva estado local como:
     * - `checked`
     * - `quantity`
     *
     * @param id ID de la lista.
     * @param newProductIds nuevo conjunto de productos.
     */
    suspend fun updateProductIds(
        id: String,
        newProductIds: List<String>
    )

    /**
     * Observa los ítems de una lista.
     *
     * @param listId ID de la lista.
     */
    fun observeItems(listId: String): Flow<List<ListItemEntity>>

    /**
     * Obtiene un ítem específico de una lista.
     */
    suspend fun getItem(
        listId: String,
        productId: String
    ): ListItemEntity?

    /**
     * Añade un producto a una lista.
     */
    suspend fun addItem(
        listId: String,
        productId: String
    )

    /**
     * Elimina un producto de una lista.
     */
    suspend fun removeItem(
        listId: String,
        productId: String
    )

    /**
     * Alterna el estado checked de un ítem.
     */
    suspend fun toggleChecked(
        listId: String,
        productId: String
    )

    /**
     * Establece explícitamente la cantidad de un ítem.
     */
    suspend fun setQuantity(
        listId: String,
        productId: String,
        quantity: Int
    )

    /**
     * Marca o desmarca todos los ítems de una lista.
     */
    suspend fun setAllChecked(
        listId: String,
        checked: Boolean
    )

    /**
     * Elimina todos los ítems marcados como checked.
     */
    suspend fun clearChecked(listId: String)

    /**
     * Observa el progreso de todas las listas.
     *
     * El progreso se expresa como:
     * - cantidad marcada
     * - total de ítems
     */
    fun observeProgress(): Flow<Map<String, ListProgress>>

    /**
     * Elimina una lista completa por ID.
     */
    suspend fun deleteById(id: String)

    /**
     * Renombra una lista.
     */
    suspend fun rename(
        id: String,
        newName: String
    )

    /**
     * Incrementa la cantidad de un ítem.
     */
    suspend fun incQuantity(
        listId: String,
        productId: String
    )

    /**
     * Decrementa la cantidad de un ítem con mínimo 1.
     */
    suspend fun decQuantityMin1(
        listId: String,
        productId: String
    )

    /**
     * Sincroniza listas desde el backend hacia el estado local.
     *
     * Operación best-effort:
     * - Un fallo remoto no rompe la UI.
     */
    suspend fun refreshLists(): Result<Unit>

    /**
     * Fuerza la carga de datos seed sobrescribiendo el estado actual.
     *
     * Útil para debug, QA o modo demo.
     */
    suspend fun forceSeed(): Result<Unit>
}

/**
 * Modelo de progreso de una lista.
 *
 * Usado principalmente en pantallas de Home o Dashboard.
 */
data class ListProgress(
    val checkedCount: Int,
    val totalCount: Int
) {

    /**
     * Progreso normalizado entre 0 y 1.
     */
    val percent: Float
        get() =
            if (totalCount <= 0) 0f
            else checkedCount.toFloat() / totalCount.toFloat()
}
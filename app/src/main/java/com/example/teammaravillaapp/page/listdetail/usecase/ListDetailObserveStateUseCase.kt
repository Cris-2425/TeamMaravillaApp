package com.example.teammaravillaapp.page.listdetail.usecase

import com.example.teammaravillaapp.data.local.entity.ListItemEntity
import com.example.teammaravillaapp.data.repository.lists.ListsRepository
import com.example.teammaravillaapp.data.repository.products.ProductRepository
import com.example.teammaravillaapp.model.ListViewType
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.data.local.prefs.listdetail.ListDetailPrefs
import com.example.teammaravillaapp.page.listdetail.ListDetailUiState
import com.example.teammaravillaapp.page.listdetail.ListItemUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * Caso de uso que construye el estado observable de la pantalla de detalle de lista.
 *
 * Su responsabilidad es producir un [Flow] de [ListDetailUiState] combinando:
 * - Lista actual seleccionada (desde [ListsRepository.lists])
 * - Items de la lista (Room / persistencia local)
 * - Catálogo de productos (Room “source of truth”)
 * - Preferencias de UI (tipo de vista y categorías seleccionadas) desde [ListDetailPrefs]
 * - Query de búsqueda (aportada por el ViewModel mediante [StateFlow])
 *
 * Ventajas:
 * - La Screen no “calcula” nada: solo pinta el [ListDetailUiState].
 * - La composición de estado es reactiva: cualquier cambio en DB/prefs/query actualiza la UI.
 *
 * @property listsRepository Repositorio de listas.
 * @property productRepository Repositorio de productos/catálogo.
 * @property prefs Preferencias específicas de ListDetail (viewType, categorías seleccionadas).
 *
 * Ejemplo de uso:
 * {@code
 * val uiStateFlow = observeStateUseCase.execute(navListId, queryFlow)
 *   .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ListDetailUiState())
 * }
 */
class ListDetailObserveStateUseCase @Inject constructor(
    private val listsRepository: ListsRepository,
    private val productRepository: ProductRepository,
    private val prefs: ListDetailPrefs
) {

    /**
     * Devuelve un flujo reactivo de [ListDetailUiState] para pintar la pantalla.
     *
     * @param navListId Id de lista recibido desde navegación. Si es nulo, se usa la última lista.
     * @param queryFlow Flow de texto de búsqueda controlado por ViewModel (StateFlow).
     *
     * @return Un [Flow] que emite estados completos listos para UI.
     *
     * @throws Exception En caso de que alguno de los repositorios lance excepciones no controladas.
     */
    fun execute(
        navListId: String?,
        queryFlow: StateFlow<String>
    ): Flow<ListDetailUiState> {

        val currentListFlow: Flow<UserList?> =
            listsRepository.lists
                .map { lists -> resolveFrom(lists, navListId) }
                .distinctUntilChanged()

        val listIdFlow: Flow<String?> =
            currentListFlow.map { it?.id }.distinctUntilChanged()

        val headerFlow: Flow<UserList?> =
            currentListFlow.distinctUntilChanged()

        val itemsMetaFlow: Flow<List<ListItemEntity>> =
            listIdFlow
                .filterNotNull()
                .flatMapLatest { id -> listsRepository.observeItems(id) }
                .map { items -> items.sortedBy { it.position } }
                .onStart { emit(emptyList()) }

        val catalogFlow: Flow<List<Product>> =
            productRepository.observeProducts()
                .onStart { emit(emptyList()) }

        val viewTypeFlow: Flow<ListViewType> = prefs.observeViewType()
        val selectedCategoriesFlow: Flow<Set<ProductCategory>> = prefs.observeSelectedCategories()

        val base5: Flow<Base5> =
            combine(
                listIdFlow,
                headerFlow,
                itemsMetaFlow,
                catalogFlow,
                viewTypeFlow
            ) { listId, header, itemsMeta, catalog, viewType ->
                Base5(
                    listId = listId,
                    header = header,
                    itemsMeta = itemsMeta,
                    catalog = catalog,
                    viewType = viewType
                )
            }

        return combine(
            base5,
            selectedCategoriesFlow,
            queryFlow
        ) { base, selectedCats, query ->
            composeUiState(
                listId = base.listId,
                header = base.header,
                itemsMeta = base.itemsMeta,
                catalog = base.catalog,
                viewType = base.viewType,
                selectedCats = selectedCats,
                query = query
            )
        }
    }

    /**
     * Resuelve qué lista es la “actual” en base al id de navegación.
     *
     * - Si hay [navListId], devuelve la lista con ese id (si existe).
     * - Si no hay id, devuelve la última lista (caso típico: “lista reciente”).
     *
     * @param lists Listas existentes del usuario.
     * @param navListId Id de navegación opcional.
     * @return La lista seleccionada o null si no hay ninguna.
     */
    private fun resolveFrom(
        lists: List<UserList>,
        navListId: String?
    ): UserList? =
        when {
            !navListId.isNullOrBlank() -> lists.firstOrNull { it.id == navListId }
            else -> lists.lastOrNull()
        }

    /**
     * Compone un [ListDetailUiState] completo a partir de fuentes de datos y filtros.
     *
     * Reglas principales:
     * - Los items de la lista se construyen haciendo join (itemsMeta.productId ↔ catálogo.id).
     * - La búsqueda excluye productos ya presentes en la lista.
     * - El filtro por categoría aplica tanto a búsqueda como a secciones de sugerencias.
     *
     * @param listId Id actual de lista (puede ser null si no existe lista seleccionada).
     * @param header Modelo de la lista (nombre, fondo, etc).
     * @param itemsMeta Items persistidos (id producto, checked, qty, position).
     * @param catalog Catálogo de productos (source of truth local).
     * @param viewType Preferencia de tipo de vista.
     * @param selectedCats Categorías seleccionadas para filtrar (puede estar vacío).
     * @param query Texto de búsqueda actual.
     *
     * @return Estado final listo para UI.
     */
    private fun composeUiState(
        listId: String?,
        header: UserList?,
        itemsMeta: List<ListItemEntity>,
        catalog: List<Product>,
        viewType: ListViewType,
        selectedCats: Set<ProductCategory>,
        query: String
    ): ListDetailUiState {

        val isFilterActive = selectedCats.isNotEmpty()
        fun categoryAllowed(cat: ProductCategory): Boolean =
            !isFilterActive || cat in selectedCats

        val byId = catalog.associateBy { it.id }

        val itemsUi: List<ListItemUi> =
            itemsMeta.mapNotNull { it ->
                val p = byId[it.productId] ?: return@mapNotNull null
                ListItemUi(
                    product = p,
                    checked = it.checked,
                    quantity = it.quantity,
                    position = it.position
                )
            }

        val inListIds = itemsUi.asSequence().map { it.product.id }.toSet()

        val normalizedQuery = query.trim()
        val searchResults =
            if (normalizedQuery.isBlank()) emptyList()
            else catalog.asSequence()
                .filter { it.id !in inListIds }
                .filter { categoryAllowed(it.category) }
                .filter { p ->
                    p.name.contains(normalizedQuery, ignoreCase = true) ||
                            p.id.contains(normalizedQuery, ignoreCase = true)
                }
                .take(20)
                .toList()

        val recentAvailable =
            catalog.asSequence()
                .take(18)
                .filter { it.id !in inListIds }
                .filter { categoryAllowed(it.category) }
                .toList()

        val availableByCategory =
            catalog.groupBy { it.category }
                .mapValues { (_, list) ->
                    list.filter { it.id !in inListIds }
                        .filter { categoryAllowed(it.category) }
                }
                .filterValues { it.isNotEmpty() }

        return ListDetailUiState(
            listId = listId,
            userList = header,
            isLoadingCatalog = catalog.isEmpty(),
            catalogError = null,
            viewType = viewType,
            selectedCategories = selectedCats,
            query = query,
            searchResults = searchResults,
            items = itemsUi,
            recentAvailable = recentAvailable,
            availableByCategory = availableByCategory
        )
    }

    /**
     * Contenedor interno para agrupar 5 flujos base y evitar un combine enorme.
     * Es que no aceptaba los 8, básicamente.
     */
    private data class Base5(
        val listId: String?,
        val header: UserList?,
        val itemsMeta: List<ListItemEntity>,
        val catalog: List<Product>,
        val viewType: ListViewType
    )
}
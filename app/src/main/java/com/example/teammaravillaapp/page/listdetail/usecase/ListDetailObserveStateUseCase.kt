package com.example.teammaravillaapp.page.listdetail.usecase

import com.example.teammaravillaapp.data.local.entity.ListItemEntity
import com.example.teammaravillaapp.data.repository.lists.ListsRepository
import com.example.teammaravillaapp.data.repository.products.ProductRepository
import com.example.teammaravillaapp.model.ListViewType
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.page.listdetail.ListDetailPrefs
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

class ListDetailObserveStateUseCase @Inject constructor(
    private val listsRepository: ListsRepository,
    private val productRepository: ProductRepository,
    private val prefs: ListDetailPrefs
) {

    fun execute(
        navListId: String?,
        queryFlow: StateFlow<String>
    ): Flow<ListDetailUiState> {

        // ✅ ahora listsRepository.lists = Flow<List<UserList>>
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

    private fun resolveFrom(
        lists: List<UserList>,
        navListId: String?
    ): UserList? =
        when {
            !navListId.isNullOrBlank() -> lists.firstOrNull { it.id == navListId }
            else -> lists.lastOrNull()
        }

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
        fun categoryAllowed(cat: ProductCategory?): Boolean {
            val c = cat ?: ProductCategory.OTHER
            return !isFilterActive || c in selectedCats
        }

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
            catalog.groupBy { it.category ?: ProductCategory.OTHER }
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

    private data class Base5(
        val listId: String?,
        val header: UserList?,
        val itemsMeta: List<ListItemEntity>,
        val catalog: List<Product>,
        val viewType: ListViewType
    )
}
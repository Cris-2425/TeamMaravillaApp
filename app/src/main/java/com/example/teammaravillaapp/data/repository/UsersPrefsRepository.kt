package com.example.teammaravillaapp.repository

import com.example.teammaravillaapp.model.ListStyle
import com.example.teammaravillaapp.model.ProductCategory
import kotlinx.coroutines.flow.Flow

interface UserPrefsRepository {

    /** Estilo actual de listas (LISTA, etc.) */
    val listStyle: Flow<ListStyle>

    /**
     * Categorías ocultas. Si está vacío => todas visibles.
     * (Guardamos solo lo mínimo)
     */
    val hiddenCategories: Flow<Set<ProductCategory>>

    /** Mapa derivado listo para UI */
    val categoryVisibility: Flow<Map<ProductCategory, Boolean>>

    suspend fun setListStyle(style: ListStyle)

    /** Set completo de ocultas (lo guardamos tal cual) */
    suspend fun setHiddenCategories(hidden: Set<ProductCategory>)

    /** Atajo: toggle de una categoría */
    suspend fun toggleCategory(category: ProductCategory)
}
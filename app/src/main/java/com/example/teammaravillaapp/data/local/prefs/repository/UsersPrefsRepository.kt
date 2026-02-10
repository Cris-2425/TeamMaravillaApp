package com.example.teammaravillaapp.data.local.prefs.repository

import com.example.teammaravillaapp.model.ListStyle
import com.example.teammaravillaapp.model.ProductCategory
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de repositorio para las preferencias de usuario relacionadas con listas y categorías.
 *
 * Define las operaciones de lectura/escritura en DataStore de forma desacoplada,
 * permitiendo un acceso limpio desde la capa de dominio o ViewModel.
 */
interface UserPrefsRepository {

    /** Estilo de listas actual (ej: lista simple, lista compacta, etc.). */
    val listStyle: Flow<ListStyle>

    /**
     * Conjunto de categorías ocultas.
     * Si está vacío, todas las categorías se consideran visibles.
     */
    val hiddenCategories: Flow<Set<ProductCategory>>

    /**
     * Mapa derivado listo para UI indicando visibilidad de cada categoría.
     *
     * Ejemplo: { MILK: true, BREAD: false, FRUIT: true }
     */
    val categoryVisibility: Flow<Map<ProductCategory, Boolean>>

    /** Actualiza el estilo de listas. */
    suspend fun setListStyle(style: ListStyle)

    /** Guarda el set completo de categorías ocultas. */
    suspend fun setHiddenCategories(hidden: Set<ProductCategory>)

    /** Alterna la visibilidad de una categoría individual. */
    suspend fun toggleCategory(category: ProductCategory)
}
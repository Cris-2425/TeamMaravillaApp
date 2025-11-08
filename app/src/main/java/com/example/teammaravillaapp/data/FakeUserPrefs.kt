package com.example.teammaravillaapp.data

import android.util.Log
import com.example.teammaravillaapp.model.ListStyle
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.util.TAG_GLOBAL

/**
 * Preferencias de usuario **en memoria** (no persistentes).
 *
 * - [listStyle]: estilo visual de las listas.
 * - [categoryVisibility]: visibilidad por categoría para ListDetail.
 *
 * Pensado para pruebas rápidas de UI. Sustituible por DataStore/Room más adelante.
 */
object FakeUserPrefs {

    /** Estilo actual de visualización de listas (por defecto, LISTA). */
    private var listStyle: ListStyle = ListStyle.LISTA

    /** Visibilidad por categoría (todas visibles por defecto). */
    private val categoryVisibility: MutableMap<ProductCategory, Boolean> =
        ProductCategory.entries.associateWith { true }.toMutableMap()

    /**
     * Obtiene el estilo de lista actual.
     */
    fun getListStyle(): ListStyle = listStyle

    /**
     * Actualiza el estilo de lista actual.
     *
     * @param style nuevo estilo (LISTA/MOSAIC/ETC).
     */
    fun setListStyle(style: ListStyle) {
        listStyle = style
        Log.e(TAG_GLOBAL, "Prefs → listStyle = $style")
    }

    /**
     * Devuelve un **snapshot inmutable** de la visibilidad por categoría.
     */
    fun getCategoryVisibility(): Map<ProductCategory, Boolean> = categoryVisibility.toMap()

    /**
     * Reemplaza la visibilidad por categoría con un nuevo mapa.
     *
     * @param newMap mapa completo de visibilidad.
     */
    fun setCategoryVisibility(newMap: Map<ProductCategory, Boolean>) {
        categoryVisibility.clear()
        categoryVisibility.putAll(newMap)
        Log.e(TAG_GLOBAL, "Prefs → categoryVisibility = $categoryVisibility")
    }
}
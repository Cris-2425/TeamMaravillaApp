// com.example.teammaravillaapp.data.FakeUserPrefs
package com.example.teammaravillaapp.data

import android.util.Log
import com.example.teammaravillaapp.model.ListStyle
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.util.TAG_GLOBAL

object FakeUserPrefs {
    private var listStyle: ListStyle = ListStyle.LISTA
    private val categoryVisibility: MutableMap<ProductCategory, Boolean> =
        ProductCategory.entries.associateWith { true }.toMutableMap()

    fun getListStyle(): ListStyle = listStyle

    fun setListStyle(style: ListStyle) {
        listStyle = style
        Log.e(TAG_GLOBAL, "Prefs → listStyle = $style")
    }

    fun getCategoryVisibility(): Map<ProductCategory, Boolean> = categoryVisibility.toMap()

    fun setCategoryVisibility(newMap: Map<ProductCategory, Boolean>) {
        categoryVisibility.clear()
        categoryVisibility.putAll(newMap)
        Log.e(TAG_GLOBAL, "Prefs → categoryVisibility = $categoryVisibility")
    }
}
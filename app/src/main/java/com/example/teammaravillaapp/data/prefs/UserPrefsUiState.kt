package com.example.teammaravillaapp.data.prefs

import com.example.teammaravillaapp.model.ListStyle
import com.example.teammaravillaapp.model.ProductCategory

data class UserPrefsUiState(
    val listStyle: ListStyle = ListStyle.LISTA,
    val categoryVisibility: Map<ProductCategory, Boolean> =
        ProductCategory.entries.associateWith { true }
)
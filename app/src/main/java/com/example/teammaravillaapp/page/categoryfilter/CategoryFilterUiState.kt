package com.example.teammaravillaapp.page.categoryfilter

import com.example.teammaravillaapp.model.ProductCategory

data class CategoryFilterUiState(
    val isLoading: Boolean = true,
    val selected: Set<ProductCategory> = emptySet(),
    val all: Set<ProductCategory> = ProductCategory.entries.toSet()
) {
    val allSelected: Boolean get() = selected.size == all.size
}
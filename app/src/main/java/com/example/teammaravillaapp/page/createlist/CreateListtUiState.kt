package com.example.teammaravillaapp.page.createlist

import com.example.teammaravillaapp.model.ListBackground

data class CreateListUiState(
    val name: String = "",
    val selectedBackground: ListBackground = ListBackground.FONDO1
) {
    val finalName: String get() = name.trim().ifBlank { "Nueva lista" }
}
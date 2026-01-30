package com.example.teammaravillaapp.page.selectlist

import com.example.teammaravillaapp.model.Recipe
import com.example.teammaravillaapp.model.UserList

data class SelectListUiState(
    val isLoading: Boolean = true,
    val isRecipeNotFound: Boolean = false,
    val recipe: Recipe? = null,
    val lists: List<Pair<String, UserList>> = emptyList(),
    val isSaving: Boolean = false
)
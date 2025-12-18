package com.example.teammaravillaapp.page.recipesdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecipesDetailViewModelFactory(
    private val recipeId: Int
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipesDetailViewModel::class.java)) {
            return RecipesDetailViewModel(recipeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
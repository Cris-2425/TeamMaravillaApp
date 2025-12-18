package com.example.teammaravillaapp.page.selectlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SelectListViewModelFactory(
    private val recipeId: Int
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SelectListViewModel::class.java)) {
            return SelectListViewModel(recipeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
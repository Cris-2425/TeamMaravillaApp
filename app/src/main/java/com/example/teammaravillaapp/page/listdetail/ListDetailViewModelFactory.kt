package com.example.teammaravillaapp.page.listdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListDetailViewModelFactory(
    private val listId: String?
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListDetailViewModel::class.java)) {
            return ListDetailViewModel(listId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
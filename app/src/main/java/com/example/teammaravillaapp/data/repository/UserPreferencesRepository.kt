package com.example.teammaravillaapp.data.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val listStyle: Flow<String>          // o tu enum serializado
    val categoryFilter: Flow<String?>    // ejemplo
    suspend fun setListStyle(value: String)
    suspend fun setCategoryFilter(value: String?)
}
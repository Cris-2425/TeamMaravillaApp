package com.example.teammaravillaapp.page.recipesdetail.usecase

import com.example.teammaravillaapp.data.repository.FavoritesRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repo: FavoritesRepository
) {
    suspend fun run(id: Int) = repo.toggle(id)
}
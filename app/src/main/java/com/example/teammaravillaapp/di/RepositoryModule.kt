package com.example.teammaravillaapp.di

import com.example.teammaravillaapp.data.local.prefs.repository.UserPrefsRepository
import com.example.teammaravillaapp.data.local.prefs.repository.UserPrefsRepositoryImpl
import com.example.teammaravillaapp.data.repository.users.UsersRepository
import com.example.teammaravillaapp.data.repository.users.DefaultUsersRepository
import com.example.teammaravillaapp.data.repository.favorites.DefaultFavoritesRepository
import com.example.teammaravillaapp.data.repository.lists.DefaultListsRepository
import com.example.teammaravillaapp.data.repository.products.DefaultProductRepository
import com.example.teammaravillaapp.data.repository.recipes.DefaultRecipesRepository
import com.example.teammaravillaapp.data.repository.favorites.FavoritesRepository
import com.example.teammaravillaapp.data.repository.lists.ListsRepository
import com.example.teammaravillaapp.data.repository.products.ProductRepository
import com.example.teammaravillaapp.data.repository.recipes.RecipesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: DefaultProductRepository
    ): ProductRepository

    @Binds
    @Singleton
    abstract  fun bindListsRepository(
        impl: DefaultListsRepository
    ): ListsRepository

    @Binds
    @Singleton
    abstract fun bindRecipesRepository(
        impl: DefaultRecipesRepository
    ): RecipesRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: DefaultUsersRepository
    ): UsersRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(
        impl: DefaultFavoritesRepository
    ): FavoritesRepository

    @Binds
    @Singleton
    abstract fun bindUserPrefsRepository(
        impl: UserPrefsRepositoryImpl
    ): UserPrefsRepository
}
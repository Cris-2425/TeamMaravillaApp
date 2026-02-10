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

/**
 * Módulo de Hilt que vincula las interfaces de repositorios con sus implementaciones concretas.
 *
 * Este módulo usa `@Binds` para que Hilt pueda inyectar automáticamente las dependencias
 * de repositorios en toda la aplicación, siguiendo los principios de Clean Architecture:
 *
 *  - Las capas superiores dependen de interfaces (abstracciones) y no de implementaciones concretas.
 *  - Cada repositorio se declara como singleton para asegurar consistencia de datos y eficiencia.
 *
 * Repositorios incluidos:
 *  - `ProductRepository` -> `DefaultProductRepository`
 *  - `ListsRepository` -> `DefaultListsRepository`
 *  - `RecipesRepository` -> `DefaultRecipesRepository`
 *  - `UsersRepository` -> `DefaultUsersRepository`
 *  - `FavoritesRepository` -> `DefaultFavoritesRepository`
 *  - `UserPrefsRepository` -> `UserPrefsRepositoryImpl`
 *
 * Uso:
 *  - Los repositorios se inyectan en los casos de uso o ViewModels mediante `@Inject`.
 */
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
    abstract fun bindListsRepository(
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
package com.example.teammaravillaapp.di

import com.example.teammaravillaapp.data.auth.AuthRepository
import com.example.teammaravillaapp.data.auth.FakeAuthRepository
import com.example.teammaravillaapp.data.repository.CachedProductRepository
import com.example.teammaravillaapp.data.repository.RoomFavoritesRepository
import com.example.teammaravillaapp.data.repository.RoomListsRepository
import com.example.teammaravillaapp.data.repository.RoomRecipesRepository
import com.example.teammaravillaapp.data.repository.FavoritesRepository
import com.example.teammaravillaapp.data.repository.ListsRepository
import com.example.teammaravillaapp.data.repository.ProductRepository
import com.example.teammaravillaapp.data.repository.RecipesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideProductRepository(
        impl: CachedProductRepository
    ): ProductRepository

    @Binds
    @Singleton
    abstract  fun provideListsRepository(
        impl: RoomListsRepository
    ): ListsRepository

    @Binds
    @Singleton
    abstract fun provideAuthRepository(
        impl: FakeAuthRepository
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun provideFavoritesRepository(
        impl: RoomFavoritesRepository
    ): FavoritesRepository

    @Binds
    @Singleton
    abstract fun provideRecipesRepository(
        impl: RoomRecipesRepository
    ): RecipesRepository
}
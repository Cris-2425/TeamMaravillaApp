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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(
        impl: CachedProductRepository
    ): ProductRepository = impl

    @Provides
    @Singleton
    fun provideListsRepository(
        impl: RoomListsRepository
    ): ListsRepository = impl

    @Provides
    @Singleton
    fun provideAuthRepository(
        impl: FakeAuthRepository
    ): AuthRepository = impl

    @Provides
    @Singleton
    fun provideFavoritesRepository(
        impl: RoomFavoritesRepository
    ): FavoritesRepository = impl

    @Provides
    @Singleton
    fun provideRecipesRepository(
        impl: RoomRecipesRepository
    ): RecipesRepository = impl
}
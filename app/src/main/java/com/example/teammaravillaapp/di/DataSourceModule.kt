package com.example.teammaravillaapp.di

import com.example.teammaravillaapp.data.remote.datasource.favorites.RemoteFavoritesDataSource
import com.example.teammaravillaapp.data.remote.datasource.favorites.RemoteFavoritesDataSourceImpl
import com.example.teammaravillaapp.data.remote.datasource.images.RemoteImageDataSource
import com.example.teammaravillaapp.data.remote.datasource.images.RemoteImageDataSourceImpl
import com.example.teammaravillaapp.data.remote.datasource.lists.RemoteListsDataSource
import com.example.teammaravillaapp.data.remote.datasource.lists.RemoteListsDataSourceImpl
import com.example.teammaravillaapp.data.remote.datasource.products.RemoteProductDataSource
import com.example.teammaravillaapp.data.remote.datasource.products.RemoteProductDataSourceImpl
import com.example.teammaravillaapp.data.remote.datasource.recipes.RemoteRecipesDataSource
import com.example.teammaravillaapp.data.remote.datasource.recipes.RemoteRecipesDataSourceImpl
import com.example.teammaravillaapp.data.remote.datasource.users.RemoteUsersDataSource
import com.example.teammaravillaapp.data.remote.datasource.users.RemoteUsersDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindRemoteProductDataSource(
        impl: RemoteProductDataSourceImpl
    ): RemoteProductDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteListsDataSource(
        impl: RemoteListsDataSourceImpl
    ): RemoteListsDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteRecipesDataSource(
        impl: RemoteRecipesDataSourceImpl
    ): RemoteRecipesDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteUsersDataSource(
        impl: RemoteUsersDataSourceImpl
    ): RemoteUsersDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteFavoritesDataSource(
        impl: RemoteFavoritesDataSourceImpl
    ): RemoteFavoritesDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteImageDataSource(
        impl: RemoteImageDataSourceImpl
    ): RemoteImageDataSource
}
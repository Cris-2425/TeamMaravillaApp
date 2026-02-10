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
import com.example.teammaravillaapp.data.remote.datasource.users.RemoteAuthDataSource
import com.example.teammaravillaapp.data.remote.datasource.users.RemoteAuthDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para proveer las implementaciones de DataSource remotas.
 *
 * Responsibilities:
 *  - Abstraer la capa de data remota detrás de interfaces.
 *  - Permitir el reemplazo de implementaciones (mock o real) para testing.
 *  - Garantizar que cada DataSource remoto sea singleton para optimizar recursos.
 *
 * Nota:
 *  Usamos @Binds porque cada DataSource tiene una implementación concreta ya definida.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    /** Provee la implementación de [RemoteProductDataSource] */
    @Binds
    @Singleton
    abstract fun bindRemoteProductDataSource(
        impl: RemoteProductDataSourceImpl
    ): RemoteProductDataSource

    /** Provee la implementación de [RemoteListsDataSource] */
    @Binds
    @Singleton
    abstract fun bindRemoteListsDataSource(
        impl: RemoteListsDataSourceImpl
    ): RemoteListsDataSource

    /** Provee la implementación de [RemoteRecipesDataSource] */
    @Binds
    @Singleton
    abstract fun bindRemoteRecipesDataSource(
        impl: RemoteRecipesDataSourceImpl
    ): RemoteRecipesDataSource

    /** Provee la implementación de [RemoteFavoritesDataSource] */
    @Binds
    @Singleton
    abstract fun bindRemoteFavoritesDataSource(
        impl: RemoteFavoritesDataSourceImpl
    ): RemoteFavoritesDataSource

    /** Provee la implementación de [RemoteImageDataSource] */
    @Binds
    @Singleton
    abstract fun bindRemoteImageDataSource(
        impl: RemoteImageDataSourceImpl
    ): RemoteImageDataSource

    /** Provee la implementación de [RemoteAuthDataSource] */
    @Binds
    @Singleton
    abstract fun bindRemoteAuthDataSource(
        impl: RemoteAuthDataSourceImpl
    ): RemoteAuthDataSource
}
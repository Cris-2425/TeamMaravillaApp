package com.example.teammaravillaapp.network

import com.example.teammaravillaapp.BuildConfig
import com.example.teammaravillaapp.data.auth.AuthRepository
import com.example.teammaravillaapp.data.auth.FakeAuthRepository
import com.example.teammaravillaapp.data.repository.FakeListsRepository
import com.example.teammaravillaapp.data.repository.FakeProductRepository
import com.example.teammaravillaapp.data.repository.RemoteProductRepository
import com.example.teammaravillaapp.repository.ListsRepository
import com.example.teammaravillaapp.repository.ProductRepository
import dagger.Provides
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides @Singleton @FakeRepo
    fun provideFakeProductRepo(impl: FakeProductRepository): ProductRepository = impl

    @Provides @Singleton @RemoteRepo
    fun provideRemoteProductRepo(impl: RemoteProductRepository): ProductRepository = impl

    @Provides
    @Singleton
    fun provideProductRepository(
        fake: FakeProductRepository,
        remote: RemoteProductRepository
    ): ProductRepository = if (BuildConfig.DEBUG) fake else remote

    @Provides
    @Singleton
    fun provideListsRepository(impl: FakeListsRepository): ListsRepository = impl

    @Provides
    @Singleton
    fun provideAuthRepository(impl: FakeAuthRepository): AuthRepository = impl
}
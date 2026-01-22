package com.example.teammaravillaapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.teammaravillaapp.data.prefs.DataStoreUserPrefsRepository
import com.example.teammaravillaapp.data.prefs.userPrefsDataStore
import com.example.teammaravillaapp.repository.UserPrefsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrefsModule {

    @Provides
    @Singleton
    fun providePrefsDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.userPrefsDataStore

    @Provides
    @Singleton
    fun provideUserPrefsRepository(
        impl: DataStoreUserPrefsRepository
    ): UserPrefsRepository = impl
}

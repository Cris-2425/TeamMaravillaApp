package com.example.teammaravillaapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.teammaravillaapp.data.local.prefs.datastore.userPrefsDataStore
import com.example.teammaravillaapp.data.local.prefs.listdetail.DataStoreListDetailPrefs
import com.example.teammaravillaapp.data.local.prefs.listdetail.ListDetailPrefs
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
    fun provideUserPrefsDataStore(
        @ApplicationContext ctx: Context
    ): DataStore<Preferences> = ctx.userPrefsDataStore

    @Provides
    @Singleton
    fun provideListDetailPrefs(
        impl: DataStoreListDetailPrefs
    ): ListDetailPrefs = impl
}
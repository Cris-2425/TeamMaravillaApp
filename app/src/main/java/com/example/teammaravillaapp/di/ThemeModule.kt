package com.example.teammaravillaapp.di

import android.content.Context
import com.example.teammaravillaapp.data.local.prefs.ThemePrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ThemeModule {

    @Provides
    @Singleton
    fun provideThemePrefs(@ApplicationContext ctx: Context): ThemePrefs = ThemePrefs(ctx)
}
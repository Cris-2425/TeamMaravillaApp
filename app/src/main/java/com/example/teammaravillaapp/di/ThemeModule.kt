package com.example.teammaravillaapp.di

import android.content.Context
import com.example.teammaravillaapp.data.local.prefs.user.ThemePrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt que provee dependencias relacionadas con las preferencias de tema de la aplicación.
 *
 * Este módulo encapsula la creación de `ThemePrefs`, permitiendo:
 *  - Acceder a configuraciones de tema (modo claro/oscuro, colores, etc.).
 *  - Mantener un singleton durante todo el ciclo de vida de la app.
 *
 * Uso:
 *  - Se inyecta en ViewModels, casos de uso o cualquier clase que necesite leer o actualizar
 *    la preferencia de tema.
 *
 * @param ctx Context de la aplicación para inicializar ThemePrefs.
 */
@Module
@InstallIn(SingletonComponent::class)
object ThemeModule {

    @Provides
    @Singleton
    fun provideThemePrefs(@ApplicationContext ctx: Context): ThemePrefs = ThemePrefs(ctx)
}
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

/**
 * Módulo de Hilt para proveer dependencias relacionadas con DataStore y preferencias de usuario.
 *
 * Este módulo centraliza la creación de instancias de DataStore y adaptadores de preferencias,
 * garantizando una sola fuente de verdad para el almacenamiento local de configuraciones y detalles
 * de listas.
 *
 * Responsabilidades:
 *  - Proveer DataStore<Preferences> para preferencias de usuario.
 *  - Proveer implementaciones de ListDetailPrefs para almacenamiento y lectura de detalles de listas.
 *
 * Uso:
 *  - Se inyectan en repositorios o capas de datos mediante `@Inject`.
 */
@Module
@InstallIn(SingletonComponent::class)
object PrefsModule {

    /**
     * Provee la instancia de DataStore<Preferences> para las preferencias de usuario.
     *
     * @param ctx Context de la aplicación.
     */
    @Provides
    @Singleton
    fun provideUserPrefsDataStore(
        @ApplicationContext ctx: Context
    ): DataStore<Preferences> = ctx.userPrefsDataStore

    /**
     * Provee la implementación de ListDetailPrefs usando DataStoreListDetailPrefs.
     *
     * Esto permite desacoplar la interfaz de la implementación concreta, facilitando pruebas
     * unitarias y cambios futuros en el almacenamiento de detalles de listas.
     *
     * @param impl Implementación concreta de ListDetailPrefs.
     */
    @Provides
    @Singleton
    fun provideListDetailPrefs(
        impl: DataStoreListDetailPrefs
    ): ListDetailPrefs = impl
}
package com.example.teammaravillaapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

/**
 * Qualifier para identificar el [CoroutineDispatcher] de IO.
 *
 * Uso: operaciones de lectura/escritura en base de datos, red, archivos, etc.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

/**
 * Qualifier para identificar el [CoroutineDispatcher] por defecto.
 *
 * Uso: cálculos intensivos de CPU que no deben bloquear el hilo principal.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

/**
 * Módulo de Hilt que provee [CoroutineDispatcher] específicos para distintos tipos de trabajo.
 *
 * Responsibilities:
 *  - Permite inyectar dispatchers de forma desacoplada, facilitando testing.
 *  - Centraliza la configuración de dispatchers para toda la app.
 */
@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    /**
     * Provee [Dispatchers.IO] para operaciones de entrada/salida.
     */
    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    /**
     * Provee [Dispatchers.Default] para trabajos de CPU intensivos.
     */
    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
package com.example.teammaravillaapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Anotación personalizada para identificar el [CoroutineScope] de aplicación.
 *
 * Este scope está destinado para operaciones de larga duración que viven
 * durante toda la vida de la app, evitando que errores de un hijo cancelen otros coroutines.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope

/**
 * Módulo de Hilt para proveer dependencias relacionadas con Coroutines.
 *
 * Responsibilities:
 *  - Crear un [CoroutineScope] singleton para toda la aplicación.
 *  - Garantizar que los coroutines se ejecuten en [Dispatchers.IO] por defecto.
 *  - Utiliza [SupervisorJob] para que fallos individuales no cancelen todo el scope.
 */
@Module
@InstallIn(SingletonComponent::class)
object CoroutinesModule {

    /**
     * Provee un [CoroutineScope] global de aplicación.
     *
     * @return [CoroutineScope] con [SupervisorJob] + [Dispatchers.IO]
     */
    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
}
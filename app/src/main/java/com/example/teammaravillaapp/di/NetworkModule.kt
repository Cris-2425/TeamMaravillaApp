package com.example.teammaravillaapp.di

import com.example.teammaravillaapp.data.remote.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Módulo de Hilt que provee todas las dependencias relacionadas con la red.
 *
 * Este módulo centraliza la configuración de Retrofit y OkHttp, y la creación de instancias
 * de las APIs remotas de la aplicación. Se utiliza en todo el singleton component, garantizando
 * que las mismas instancias se compartan durante todo el ciclo de vida de la app.
 *
 * Responsabilidades:
 *  - Configurar OkHttpClient con interceptor de logging y timeouts.
 *  - Crear Retrofit con base URL y convertidor Gson.
 *  - Proveer instancias singleton de las APIs: ProductApi, ImageApi, ListsApi, RecipesApi, AuthApi, JsonStorageApi.
 *
 * Uso:
 *  - Se inyectan automáticamente en repositorios o capas de datos mediante `@Inject`.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /** URL base para la API (usando host del emulador Android). */
    private const val BASE_URL = "http://10.0.2.2:5131/"

    /**
     * Provee un HttpLoggingInterceptor para depuración de requests/responses HTTP.
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    /**
     * Provee un OkHttpClient configurado con logging y timeouts.
     */
    @Provides
    @Singleton
    fun provideOkHttp(logging: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
            .build()

    /**
     * Provee Retrofit configurado con base URL y GsonConverterFactory.
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /** Provee instancia de ProductApi usando Retrofit. */
    @Provides
    @Singleton
    fun provideProductApi(retrofit: Retrofit): ProductApi =
        retrofit.create(ProductApi::class.java)

    /** Provee instancia de ImageApi usando Retrofit. */
    @Provides
    @Singleton
    fun provideImageApi(retrofit: Retrofit): ImageApi =
        retrofit.create(ImageApi::class.java)

    /** Provee instancia de ListsApi usando Retrofit. */
    @Provides
    @Singleton
    fun provideListsApi(retrofit: Retrofit): ListsApi =
        retrofit.create(ListsApi::class.java)

    /** Provee instancia de RecipesApi usando Retrofit. */
    @Provides
    @Singleton
    fun provideRecipesApi(retrofit: Retrofit): RecipesApi =
        retrofit.create(RecipesApi::class.java)

    /** Provee instancia de AuthApi usando Retrofit. */
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    /** Provee instancia de JsonStorageApi usando Retrofit. */
    @Provides
    @Singleton
    fun provideJsonStorageApi(retrofit: Retrofit): JsonStorageApi =
        retrofit.create(JsonStorageApi::class.java)
}
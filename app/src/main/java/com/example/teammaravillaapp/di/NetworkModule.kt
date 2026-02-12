package com.example.teammaravillaapp.di

import com.example.teammaravillaapp.BuildConfig
import com.example.teammaravillaapp.data.remote.api.AuthApi
import com.example.teammaravillaapp.data.remote.api.FavoritesApi
import com.example.teammaravillaapp.data.remote.api.ImageApi
import com.example.teammaravillaapp.data.remote.api.JsonStorageApi
import com.example.teammaravillaapp.data.remote.api.ListsApi
import com.example.teammaravillaapp.data.remote.api.ProductApi
import com.example.teammaravillaapp.data.remote.api.RecipesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Módulo Hilt para dependencias de red (Retrofit/OkHttp y APIs).
 *
 * Centraliza la construcción del stack HTTP para:
 * - asegurar configuración uniforme (timeouts, logging)
 * - facilitar *swap* de base URL (vía [BuildConfig.BASE_URL])
 * - exponer APIs como singletons reutilizables durante todo el ciclo de vida de la app
 *
 * ### Logging
 * El nivel se controla por [BuildConfig.DEBUG] para evitar exponer payloads en builds de release.
 *
 * ## Concurrencia
 * Las instancias provistas son **thread-safe**:
 * - `OkHttpClient` está diseñado para reutilización concurrente.
 * - `Retrofit` y los proxies generados son seguros para uso concurrente.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Interceptor de logging HTTP para depuración.
     *
     * @return Interceptor configurado con nivel `BODY` en debug y `NONE` en release.
     *
     * @see HttpLoggingInterceptor
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
        }

    /**
     * Cliente OkHttp compartido por Retrofit.
     *
     * Incluye:
     * - logging condicional
     * - timeouts conservadores para evitar esperas indefinidas en redes inestables
     *
     * @param logging Interceptor provisto por [provideLoggingInterceptor].
     * @return Instancia singleton de [OkHttpClient].
     */
    @Provides
    @Singleton
    fun provideOkHttp(logging: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()

    /**
     * Instancia base de Retrofit.
     *
     * Se configura con:
     * - `baseUrl` desde [BuildConfig.BASE_URL]
     * - [GsonConverterFactory] para serialización/deserialización
     *
     * @param okHttp Cliente HTTP compartido.
     * @return Instancia singleton de [Retrofit].
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /**
     * Crea el proxy Retrofit para [ProductApi].
     *
     * @param retrofit Instancia base.
     * @return API de productos.
     */
    @Provides
    @Singleton
    fun provideProductApi(retrofit: Retrofit): ProductApi =
        retrofit.create(ProductApi::class.java)

    /**
     * Crea el proxy Retrofit para [ImageApi].
     *
     * @param retrofit Instancia base.
     * @return API de imágenes.
     */
    @Provides
    @Singleton
    fun provideImageApi(retrofit: Retrofit): ImageApi =
        retrofit.create(ImageApi::class.java)

    /**
     * Crea el proxy Retrofit para [ListsApi].
     *
     * @param retrofit Instancia base.
     * @return API de listas.
     */
    @Provides
    @Singleton
    fun provideListsApi(retrofit: Retrofit): ListsApi =
        retrofit.create(ListsApi::class.java)

    /**
     * Crea el proxy Retrofit para [RecipesApi].
     *
     * @param retrofit Instancia base.
     * @return API de recetas.
     */
    @Provides
    @Singleton
    fun provideRecipesApi(retrofit: Retrofit): RecipesApi =
        retrofit.create(RecipesApi::class.java)

    /**
     * Crea el proxy Retrofit para [AuthApi].
     *
     * @param retrofit Instancia base.
     * @return API de autenticación.
     */
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    /**
     * Crea el proxy Retrofit para [JsonStorageApi].
     *
     * @param retrofit Instancia base.
     * @return API genérica de almacenamiento JSON.
     */
    @Provides
    @Singleton
    fun provideJsonStorageApi(retrofit: Retrofit): JsonStorageApi =
        retrofit.create(JsonStorageApi::class.java)

    /**
     * Crea el proxy Retrofit para [FavoritesApi].
     *
     * @param retrofit Instancia base.
     * @return API específica de favoritos.
     */
    @Provides
    @Singleton
    fun provideFavoritesApi(retrofit: Retrofit): FavoritesApi =
        retrofit.create(FavoritesApi::class.java)
}
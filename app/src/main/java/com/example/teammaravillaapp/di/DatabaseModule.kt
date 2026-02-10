package com.example.teammaravillaapp.di

import android.content.Context
import androidx.room.Room
import com.example.teammaravillaapp.data.local.dao.FavoritesDao
import com.example.teammaravillaapp.data.local.dao.ListsDao
import com.example.teammaravillaapp.data.local.dao.ProductDao
import com.example.teammaravillaapp.data.local.dao.RecipesDao
import com.example.teammaravillaapp.data.local.dao.StatsDao
import com.example.teammaravillaapp.data.local.db.AppDatabase
import com.example.teammaravillaapp.data.local.db.RoomMigrations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para proveer la base de datos local (Room) y DAOs.
 *
 * Responsibilities:
 *  - Configurar y construir la base de datos Room con migraciones.
 *  - Proveer DAOs individuales como dependencias singleton.
 *  - Permitir que otros componentes inyecten la base de datos o DAOs sin acoplarse a la implementación.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provee una instancia singleton de [AppDatabase] configurada con migraciones.
     *
     * @param ctx Contexto de la aplicación, usado por Room para crear la base de datos.
     * @return Instancia de [AppDatabase].
     */
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "teammaravilla.db")
            .addMigrations(
                RoomMigrations.MIGRATION_3_4,
                RoomMigrations.MIGRATION_4_5,
                RoomMigrations.MIGRATION_5_6,
                RoomMigrations.MIGRATION_6_7,
                RoomMigrations.MIGRATION_7_8,
                RoomMigrations.MIGRATION_8_9
            )
            .build()

    /**
     * Provee un [RecipesDao] desde la base de datos.
     */
    @Provides fun provideRecipesDao(db: AppDatabase): RecipesDao = db.recipesDao()

    /**
     * Provee un [ProductDao] desde la base de datos.
     */
    @Provides fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()

    /**
     * Provee un [ListsDao] desde la base de datos.
     */
    @Provides fun provideListsDao(db: AppDatabase): ListsDao = db.listsDao()

    /**
     * Provee un [FavoritesDao] desde la base de datos.
     */
    @Provides fun provideFavoritesDao(db: AppDatabase): FavoritesDao = db.favoritesDao()

    /**
     * Provee un [StatsDao] desde la base de datos.
     */
    @Provides fun provideStatsDao(db: AppDatabase): StatsDao = db.statsDao()
}
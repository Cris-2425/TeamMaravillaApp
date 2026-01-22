package com.example.teammaravillaapp.di

import android.content.Context
import androidx.room.Room
import com.example.teammaravillaapp.data.local.dao.FavoritesDao
import com.example.teammaravillaapp.data.local.dao.ListsDao
import com.example.teammaravillaapp.data.local.dao.ProductDao
import com.example.teammaravillaapp.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "teammaravilla.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideRecipesDao(db: AppDatabase) = db.recipesDao()

    @Provides fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()
    @Provides fun provideListsDao(db: AppDatabase): ListsDao = db.listsDao()
    @Provides fun provideFavoritesDao(db: AppDatabase): FavoritesDao = db.favoritesDao()
}
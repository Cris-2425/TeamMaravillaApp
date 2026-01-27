package com.example.teammaravillaapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.teammaravillaapp.data.local.dao.FavoritesDao
import com.example.teammaravillaapp.data.local.dao.ListsDao
import com.example.teammaravillaapp.data.local.dao.ProductDao
import com.example.teammaravillaapp.data.local.dao.RecipesDao
import com.example.teammaravillaapp.data.local.dao.StatsDao
import com.example.teammaravillaapp.data.local.entity.*
import com.example.teammaravillaapp.data.local.mapper.Converters

@Database(
    entities = [
        ProductEntity::class,
        ListEntity::class,
        ListItemEntity::class,
        FavoriteRecipeEntity::class,
        RecipeEntity::class,
        RecipeIngredientsCrossRef::class
    ],
    version = 8,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun listsDao(): ListsDao
    abstract fun favoritesDao(): FavoritesDao
    abstract fun recipesDao(): RecipesDao
    abstract fun statsDao(): StatsDao

}
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

/**
 * Base de datos principal de la aplicación usando Room.
 *
 * Contiene todas las entidades de la capa de datos local y expone los DAOs correspondientes.
 *
 * - `version = 9`: última versión del esquema.
 * - `exportSchema = false`: no se exporta esquema a JSON.
 * - `TypeConverters(Converters::class)`: para manejar tipos personalizados como listas de strings.
 *
 * DAOs expuestos:
 * - [productDao] para productos.
 * - [listsDao] para listas de usuario y sus elementos.
 * - [favoritesDao] para recetas favoritas.
 * - [recipesDao] para recetas y sus ingredientes.
 * - [statsDao] para estadísticas.
 */
@Database(
    entities = [
        ProductEntity::class,
        ListEntity::class,
        ListItemEntity::class,
        FavoriteRecipeEntity::class,
        RecipeEntity::class,
        RecipeIngredientsCrossRef::class
    ],
    version = 9,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    /** DAO para productos. */
    abstract fun productDao(): ProductDao

    /** DAO para listas de usuario y sus elementos. */
    abstract fun listsDao(): ListsDao

    /** DAO para recetas favoritas. */
    abstract fun favoritesDao(): FavoritesDao

    /** DAO para recetas y sus ingredientes. */
    abstract fun recipesDao(): RecipesDao

    /** DAO para estadísticas de la aplicación. */
    abstract fun statsDao(): StatsDao
}
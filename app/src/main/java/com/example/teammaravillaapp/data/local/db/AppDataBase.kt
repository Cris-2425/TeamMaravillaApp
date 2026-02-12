package com.example.teammaravillaapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.teammaravillaapp.data.local.dao.FavoritesDao
import com.example.teammaravillaapp.data.local.dao.ListsDao
import com.example.teammaravillaapp.data.local.dao.ProductDao
import com.example.teammaravillaapp.data.local.dao.RecipesDao
import com.example.teammaravillaapp.data.local.dao.StatsDao
import com.example.teammaravillaapp.data.local.entity.FavoriteRecipeEntity
import com.example.teammaravillaapp.data.local.entity.ListEntity
import com.example.teammaravillaapp.data.local.entity.ListItemEntity
import com.example.teammaravillaapp.data.local.entity.ProductEntity
import com.example.teammaravillaapp.data.local.entity.RecipeEntity
import com.example.teammaravillaapp.data.local.entity.RecipeIngredientsCrossRef

/**
 * Base de datos Room de la app (capa local), fuente de verdad para caché y estado offline.
 *
 * Centraliza el **esquema** y la obtención de DAOs. La *versión* define el contrato de migraciones:
 * cualquier cambio incompatible en entidades o índices debe reflejarse incrementando `version` y
 * aportando migraciones (si se preservan datos).
 *
 * ## Diseño
 * - `entities`: agrupa las tablas necesarias para productos, listas, favoritos y recetas.
 * - `exportSchema = false`: evita exportar el esquema; reduce ruido en repos, pero limita auditoría
 *   de cambios. Si se empieza a versionar el esquema, conviene activarlo.
 *
 * ## Concurrencia
 * `RoomDatabase` es **thread-safe**: los DAOs pueden usarse desde múltiples coroutines/hilos.
 * Room gestiona el *dispatcher* para métodos `suspend` y la invalidación para `Flow`.
 *
 * @see ProductDao
 * @see ListsDao
 * @see FavoritesDao
 * @see RecipesDao
 * @see StatsDao
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
abstract class AppDatabase : RoomDatabase() {

    /**
     * DAO de **productos**.
     *
     * @return Acceso a operaciones de lectura/escritura sobre `products`.
     * @see ProductDao
     */
    abstract fun productDao(): ProductDao

    /**
     * DAO de **listas de usuario** e items asociados.
     *
     * @return Acceso a operaciones sobre `user_lists` y `list_items`.
     * @see ListsDao
     */
    abstract fun listsDao(): ListsDao

    /**
     * DAO de **recetas favoritas** del usuario.
     *
     * @return Acceso a operaciones sobre `favorite_recipes`.
     * @see FavoritesDao
     */
    abstract fun favoritesDao(): FavoritesDao

    /**
     * DAO de **recetas** y su relación con ingredientes (cross-ref).
     *
     * @return Acceso a operaciones sobre `recipes` y `recipe_ingredients`.
     * @see RecipesDao
     */
    abstract fun recipesDao(): RecipesDao

    /**
     * DAO de **estadísticas** de la aplicación.
     *
     * @return Acceso a consultas/actualizaciones relacionadas con métricas locales.
     * @see StatsDao
     */
    abstract fun statsDao(): StatsDao
}
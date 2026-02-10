package com.example.teammaravillaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.teammaravillaapp.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para la gestión del catálogo de productos.
 *
 * Permite observar, insertar, actualizar y eliminar productos.
 * Se puede usar Flow para obtener datos reactivos.
 */
@Dao
interface ProductDao {

    /** Observa todos los productos ordenados alfabéticamente por nombre. */
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun observeAll(): Flow<List<ProductEntity>>

    /** Obtiene todos los productos ordenados alfabéticamente por nombre (snapshot). */
    @Query("SELECT * FROM products ORDER BY name ASC")
    suspend fun getAll(): List<ProductEntity>

    /** Inserta o reemplaza múltiples productos. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ProductEntity>)

    /** Inserta o reemplaza un solo producto. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: ProductEntity)

    /** Elimina un producto por su ID. */
    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteById(id: String)

    /** Elimina todos los productos. */
    @Query("DELETE FROM products")
    suspend fun clear()

    /** Devuelve la cantidad total de productos almacenados. */
    @Query("SELECT COUNT(*) FROM products")
    suspend fun count(): Int
}
package com.example.teammaravillaapp.data.local.dao

import androidx.room.*
import com.example.teammaravillaapp.data.local.entity.ListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListsDao {

    @Query("SELECT * FROM user_lists ORDER BY rowid DESC")
    fun observeAll(): Flow<List<ListEntity>>

    @Query("SELECT * FROM user_lists WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ListEntity?

    @Query("SELECT COUNT(*) FROM user_lists")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(list: ListEntity)

    @Query("DELETE FROM user_lists")
    suspend fun clear()
}
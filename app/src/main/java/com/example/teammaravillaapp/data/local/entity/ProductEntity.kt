package com.example.teammaravillaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val imageUrl: String?,
    val imageRes: Int?          // ✅ nuevo
)
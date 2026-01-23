package com.example.teammaravillaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val imageRes: Int?,          // drawable id (Int)
    val instructions: String
)
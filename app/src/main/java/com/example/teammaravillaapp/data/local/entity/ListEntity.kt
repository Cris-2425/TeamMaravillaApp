package com.example.teammaravillaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_lists")
data class ListEntity(
    @PrimaryKey val id: String,
    val name: String,
    val background: String,
    val createdAt: Long
)

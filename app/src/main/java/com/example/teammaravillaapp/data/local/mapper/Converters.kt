package com.example.teammaravillaapp.data.local.mapper

import androidx.room.TypeConverter

object Converters {

    @TypeConverter
    @JvmStatic
    fun fromStringList(list: List<String>?): String {
        return list?.joinToString(separator = "|") ?: ""
    }

    @TypeConverter
    @JvmStatic
    fun toStringList(data: String?): List<String> {
        if (data.isNullOrBlank()) return emptyList()
        return data.split("|").filter { it.isNotBlank() }
    }
}
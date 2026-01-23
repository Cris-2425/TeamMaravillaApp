package com.example.teammaravillaapp.data.local.mapper

import androidx.room.TypeConverter
import org.json.JSONArray

object Converters {

    @TypeConverter
    @JvmStatic
    fun fromStringList(list: List<String>?): String {
        if (list.isNullOrEmpty()) return "[]"
        val json = JSONArray()
        list.forEach { json.put(it) }
        return json.toString()
    }

    @TypeConverter
    @JvmStatic
    fun toStringList(data: String?): List<String> {
        if (data.isNullOrBlank()) return emptyList()

        // intentamos JSON primero; si falla, hacemos fallback al split.
        return runCatching {
            val json = JSONArray(data)
            buildList(json.length()) {
                for (i in 0 until json.length()) add(json.getString(i))
            }
        }.getOrElse {
            data.split("|").filter { it.isNotBlank() }
        }
    }
}
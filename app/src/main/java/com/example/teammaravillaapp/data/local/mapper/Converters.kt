package com.example.teammaravillaapp.data.local.mapper

import androidx.room.TypeConverter
import org.json.JSONArray

object Converters {

    @TypeConverter
    @JvmStatic
    fun stringListToJson(list: List<String>?): String {
        if (list.isNullOrEmpty()) return "[]"
        val json = JSONArray()
        list.forEach { json.put(it) }
        return json.toString()
    }

    @TypeConverter
    @JvmStatic
    fun jsonToStringList(data: String?): List<String> {
        if (data.isNullOrBlank()) return emptyList()

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
package com.example.teammaravillaapp.data.local.mapper

import androidx.room.TypeConverter
import org.json.JSONArray

/**
 * Converters personalizados para Room.
 *
 * Permiten almacenar y recuperar listas de Strings como JSON en la base de datos.
 * Esto es útil para campos que no son tipos primitivos soportados por Room.
 */
object Converters {

    /**
     * Convierte una lista de Strings en un JSON string para almacenarla en Room.
     *
     * @param list Lista de Strings a convertir.
     * @return JSON string representando la lista. Retorna "[]" si la lista es nula o vacía.
     */
    @TypeConverter
    @JvmStatic
    fun stringListToJson(list: List<String>?): String {
        if (list.isNullOrEmpty()) return "[]"
        val json = JSONArray()
        list.forEach { json.put(it) }
        return json.toString()
    }

    /**
     * Convierte un JSON string de Room a una lista de Strings.
     *
     * @param data JSON string obtenido de la base de datos.
     * @return Lista de Strings. Retorna lista vacía si el string es nulo o vacío.
     * En caso de error en el parsing JSON, usa un fallback separando por "|".
     */
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
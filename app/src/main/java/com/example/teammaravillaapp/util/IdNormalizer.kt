package com.example.teammaravillaapp.util

object IdNormalizer {

    fun fromName(name: String): String =
        name.trim()
            .lowercase()
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u")
            .replace("ñ", "n")
            .replace("[^a-z0-9]+".toRegex(), "_")
            .trim('_')
}
package com.example.teammaravillaapp.data.remote.dto

/**
 * Respuesta estándar del backend al crear un recurso (p. ej. un fichero en una carpeta JSON).
 *
 * @property id Identificador generado por el backend (típicamente un UUID).
 * @property message Mensaje informativo devuelto por el backend.
 */
data class CreateResponseDto(
    val id: String,
    val message: String
)
package com.example.teammaravillaapp.data.remote.dto
import com.example.teammaravillaapp.model.ListBackground
/**
 * DTO que representa una lista de usuario para la API.
 *
 * Se usa para:
 * - Obtener listas desde el backend.
 * - Enviar listas al backend para sincronización.
 *
 * Todos los campos corresponden directamente a los atributos de la lista en la API.
 *
 * @property id Identificador único de la lista (generalmente UUID).
 * @property name Nombre de la lista mostrado en UI.
 * @property background Fondo o tema visual de la lista (string que mapea a [ListBackground]).
 * @property createdAt Timestamp de creación de la lista (epoch millis).
 * @property items Elementos de la lista ([ListItemDto]), ordenados según posición.
 */
data class UserListDto(
    val id: String,
    val name: String,
    val background: String,
    val createdAt: Long,
    val items: List<ListItemDto>
)
package com.example.teammaravillaapp.data.remote.dto
import com.example.teammaravillaapp.model.UserListSnapshot
import com.example.teammaravillaapp.model.ListItemSnapshot

/**
 * DTO que representa un ítem dentro de una lista de compras o tareas.
 *
 * Se utiliza en la sincronización remota de listas ([UserListDto], [UserListSnapshot]) y
 * para mapear a la capa de dominio ([ListItemSnapshot]).
 *
 * @property productId Identificador del producto asociado al ítem.
 * @property addedAt Timestamp de creación del ítem (milisegundos desde epoch).
 * @property position Posición del ítem dentro de la lista (para ordenamiento).
 * @property checked Estado de completado del ítem (true si está marcado como completado).
 * @property quantity Cantidad del producto en la lista.
 */
data class ListItemDto(
    val productId: String,
    val addedAt: Long,
    val position: Int,
    val checked: Boolean,
    val quantity: Int
)
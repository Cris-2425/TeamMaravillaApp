package com.example.teammaravillaapp.data.remote.mapper

import com.example.teammaravillaapp.data.remote.dto.ListItemDto
import com.example.teammaravillaapp.data.remote.dto.UserListDto
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.ListItemSnapshot
import com.example.teammaravillaapp.model.UserListSnapshot

/**
 * Extensiones de mapeo entre DTOs remotos y modelos de dominio para **listas**.
 *
 * Centraliza conversiones para mantener:
 * - un único punto de verdad sobre el contrato API ↔ dominio
 * - orden determinista (por `position`) en sincronización y UI
 * - *fallbacks* seguros ante valores desconocidos (p. ej. `background`)
 *
 * Estas funciones son **puras** (sin efectos secundarios) y seguras para uso en cualquier hilo.
 *
 * @see UserListDto
 * @see UserListSnapshot
 * @see ListItemDto
 * @see ListItemSnapshot
 */
fun UserListDto.toSnapshot(): UserListSnapshot =
    UserListSnapshot(
        id = id,
        name = name,
        background = background.toListBackgroundOrDefault(),
        createdAt = createdAt,
        items = items
            .sortedBy { it.position }
            .map { it.toSnapshot() }
    )

/**
 * Convierte un *snapshot* de dominio en DTO listo para envío a API.
 *
 * Mantiene el orden por `position` para evitar escrituras no deterministas cuando el backend
 * persiste la colección tal cual.
 *
 * @receiver Snapshot de dominio.
 * @return DTO equivalente para transporte.
 */
fun UserListSnapshot.toDto(): UserListDto =
    UserListDto(
        id = id,
        name = name,
        background = background.name,
        createdAt = createdAt,
        items = items
            .sortedBy { it.position }
            .map { it.toDto() }
    )

/**
 * Convierte un DTO de item en su representación de dominio para sincronización.
 *
 * @receiver DTO remoto.
 * @return Snapshot de item en dominio.
 */
fun ListItemDto.toSnapshot(): ListItemSnapshot =
    ListItemSnapshot(
        productId = productId,
        addedAt = addedAt,
        position = position,
        checked = checked,
        quantity = quantity
    )

/**
 * Convierte un snapshot de item a DTO remoto.
 *
 * @receiver Snapshot de dominio.
 * @return DTO listo para API.
 */
fun ListItemSnapshot.toDto(): ListItemDto =
    ListItemDto(
        productId = productId,
        addedAt = addedAt,
        position = position,
        checked = checked,
        quantity = quantity
    )

/**
 * Convierte un `String` en [ListBackground] con *fallback* seguro.
 *
 * ### Por qué existe
 * El backend persiste `background` como texto. Para mantener robustez ante:
 * - valores legacy
 * - datos corruptos
 * - cambios de enumeración
 *
 * se degrada a un valor por defecto en lugar de fallar.
 *
 * @receiver Valor textual recibido/persistido.
 * @return [ListBackground] si existe; en caso contrario [ListBackground.FONDO1].
 */
private fun String.toListBackgroundOrDefault(): ListBackground =
    runCatching { ListBackground.valueOf(this) }
        .getOrElse { ListBackground.FONDO1 }
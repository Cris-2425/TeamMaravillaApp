package com.example.teammaravillaapp.data.remote.dto
import com.example.teammaravillaapp.data.session.SessionStore
/**
 * DTO que representa la respuesta del backend tras un login o registro.
 *
 * Se utiliza para:
 * - Persistir la sesión de usuario en [SessionStore].
 * - Mapear la información mínima de usuario para la capa de dominio.
 *
 * @property id Token o identificador único devuelto por el backend (clave principal de la sesión).
 * @property name Nombre del usuario.
 * @property email Email del usuario.
 * @property message Mensaje opcional de error o información adicional del backend.
 */
data class AuthResponseDto(
    val id: String,
    val name: String,
    val email: String,
    val message: String
)
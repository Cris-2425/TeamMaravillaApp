package com.example.teammaravillaapp.data.remote.dto

/**
 * DTO para el request de login en la API.
 *
 * Se utiliza para enviar credenciales del usuario al backend.
 *
 * @property name Nombre de usuario.
 * @property passwd Contraseña del usuario.
 */
data class LoginRequestDto(
    val name: String,
    val passwd: String
)
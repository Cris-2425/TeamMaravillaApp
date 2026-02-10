package com.example.teammaravillaapp.data.remote.dto

/**
 * DTO usado para enviar datos de registro al backend.
 *
 * Se envía al endpoint de registro de usuario.
 *
 * @property name Nombre de usuario (username).
 * @property passwd Contraseña del usuario.
 * @property email Correo electrónico del usuario.
 */
data class RegisterRequestDto(
    val name: String,
    val passwd: String,
    val email: String
)
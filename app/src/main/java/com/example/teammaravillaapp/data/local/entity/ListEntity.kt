package com.example.teammaravillaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa una lista creada por el usuario.
 *
 * Tabla: user_lists
 *
 * @property id Identificador único de la lista (UUID).
 * @property name Nombre visible de la lista.
 * @property background Fondo elegido para la lista (se almacena como String, enum ListBackground).
 * @property createdAt Timestamp de creación en milisegundos.
 */
@Entity(tableName = "user_lists")
data class ListEntity(
    @PrimaryKey val id: String,
    val name: String,
    val background: String,
    val createdAt: Long
)
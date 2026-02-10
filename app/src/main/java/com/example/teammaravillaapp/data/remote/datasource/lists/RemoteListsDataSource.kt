package com.example.teammaravillaapp.data.remote.datasource.lists

import com.example.teammaravillaapp.model.UserListSnapshot

/**
 * Fuente de datos remota para listas de usuario.
 *
 * Define los contratos para interactuar con las listas en el backend.
 * La implementación concreta puede usar Retrofit, OkHttp u otra librería HTTP.
 */
interface RemoteListsDataSource {

    /**
     * Obtiene todas las listas desde el backend.
     *
     * @return Lista de [UserListSnapshot] con el estado completo de cada lista.
     */
    suspend fun fetchAll(): List<UserListSnapshot>

    /**
     * Sobrescribe todas las listas en el backend con la lista proporcionada.
     *
     * Reemplaza completamente el estado remoto de listas.
     *
     * @param lists Lista de [UserListSnapshot] a enviar al backend.
     */
    suspend fun overwriteAll(lists: List<UserListSnapshot>)
}
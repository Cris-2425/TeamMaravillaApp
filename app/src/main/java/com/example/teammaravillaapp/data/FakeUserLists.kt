package com.example.teammaravillaapp.data

import android.util.Log
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.util.TAG_GLOBAL
import java.util.UUID

/**
 *
 * Repositorio en memoria para almacenar listas del usuario.
 * Actúa como una "fake base de datos" para las pantallas CreateList y ListDetail.
 */
object FakeUserLists {

    /** Estructura interna (id único y contenido) */
    private data class Entry(val id: String, var list: UserList)

    private val store = mutableListOf<Entry>()

    /** Añade una nueva lista al repositorio y devuelve su ID. */
    fun add(list: UserList): String {
        val id = UUID.randomUUID().toString()
        store += Entry(id, list)
        Log.e(TAG_GLOBAL, "FakeUserLists → Añadida: id=$id, name='${list.name}', bg=${list.background}")
        return id
    }

    /** Devuelve todas las listas almacenadas con su ID. */
    fun all(): List<Pair<String, UserList>> = store.map { it.id to it.list }

    /** Devuelve una lista por su ID, si existe. */
    fun get(id: String): UserList? = store.firstOrNull { it.id == id }?.list

    /** Busca una lista por nombre (útil cuando no usamos navegación). */
    fun getByName(name: String): UserList? = store.firstOrNull { it.list.name == name }?.list

    /** Actualiza una lista ya existente (por ID). */
    fun update(id: String, newList: UserList) {
        val entry = store.firstOrNull { it.id == id }
        if (entry != null) {
            entry.list = newList
            Log.e(TAG_GLOBAL, "FakeUserLists → Actualizada (id=$id): '${newList.name}' (${newList.products.size} productos)")
        } else {
            Log.e(TAG_GLOBAL, "FakeUserLists → No se encontró la lista con id=$id para actualizar")
        }
    }

    /** Actualiza productos de una lista concreta (por nombre). */
    fun updateProductsByName(name: String, newProducts: List<Product>) {
        val entry = store.firstOrNull { it.list.name == name }
        if (entry != null) {
            entry.list = entry.list.copy(products = newProducts)
            Log.e(TAG_GLOBAL, "Productos actualizados: '${name}' (${newProducts.size})")
        } else {
            Log.e(TAG_GLOBAL, "No se encontró lista con nombre='$name'")
        }
    }

    /** Elimina todas las listas (reset). */
    fun clear() {
        store.clear()
        Log.e(TAG_GLOBAL, "Repositorio limpiado")
    }

    /** Devuelve una lista de ejemplo si el repositorio está vacío. */
    fun sample(): UserList {
        if (store.isEmpty()) {
            val demo = UserList(
                name = "Compra semanal",
                background = ListBackground.FONDO2,
                products = emptyList()
            )
            add(demo)
        }
        return store.last().list
    }
}

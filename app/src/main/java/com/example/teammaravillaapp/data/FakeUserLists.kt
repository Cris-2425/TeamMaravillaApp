package com.example.teammaravillaapp.data

import android.util.Log
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.util.TAG_GLOBAL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

/**
 * Repositorio **en memoria** para las listas del usuario.
 *
 * ✅ Ahora ES reactivo (StateFlow) para que Home/ListDetail se actualicen solos.
 * No hay persistencia: al cerrar la app se pierde el contenido.
 */
object FakeUserLists {

    /** Estructura interna (id único + contenido). */
    private data class Entry(val id: String, var list: UserList)

    /** Almacén en memoria de listas del usuario. */
    private val store = mutableListOf<Entry>()

    /** Stream reactivo con todas las listas (id + lista). */
    private val _lists = MutableStateFlow<List<Pair<String, UserList>>>(emptyList())
    val lists: StateFlow<List<Pair<String, UserList>>> = _lists

    /** Emite una copia segura del estado actual. */
    private fun emit() {
        _lists.value = store.map { it.id to it.list }
    }

    /**
     * Inserta una lista y devuelve su **ID** generado.
     *
     * ✅ Regla: el ID "real" es el de Entry. Para evitar inconsistencias,
     * guardamos la lista con `id` actualizado a ese mismo valor.
     */
    fun add(list: UserList): String {
        val id = UUID.randomUUID().toString()
        val normalized = list.copy(id = id)
        store += Entry(id, normalized)
        emit()

        Log.d(
            TAG_GLOBAL,
            "FakeUserLists → Añadida: id=$id, name='${normalized.name}', bg=${normalized.background}"
        )
        return id
    }

    /**
     * Devuelve todas las listas en forma `(id, lista)`.
     * (Copia segura)
     */
    fun all(): List<Pair<String, UserList>> = store.map { it.id to it.list }

    /** Busca una lista por **id**. */
    fun get(id: String): UserList? = store.firstOrNull { it.id == id }?.list

    /** Devuelve el **id** de la lista con ese nombre, si existe. */
    fun getIdByName(name: String): String? =
        store.firstOrNull { it.list.name == name }?.id

    /**
     * Reemplaza el contenido de la lista con **id** dado.
     * Mantiene el id coherente.
     */
    fun update(id: String, newList: UserList) {
        val entry = store.firstOrNull { it.id == id }
        if (entry != null) {
            entry.list = newList.copy(id = id)
            emit()
            Log.d(
                TAG_GLOBAL,
                "FakeUserLists → Actualizada (id=$id): '${entry.list.name}' (${entry.list.products.size} productos)"
            )
        } else {
            Log.d(TAG_GLOBAL, "FakeUserLists → No se encontró la lista con id=$id para actualizar")
        }
    }

    /**
     * Actualiza **solo los productos** de la lista con ese **id**.
     * ✅ Mejor que por nombre (el nombre puede repetirse / cambiar).
     */
    fun updateProducts(id: String, newProducts: List<Product>) {
        val entry = store.firstOrNull { it.id == id }
        if (entry != null) {
            entry.list = entry.list.copy(products = newProducts)
            emit()
            Log.d(TAG_GLOBAL, "FakeUserLists → Productos actualizados: id=$id (${newProducts.size})")
        } else {
            Log.d(TAG_GLOBAL, "FakeUserLists → No se encontró lista con id=$id")
        }
    }

    /**
     * Mantengo tu método por compatibilidad, pero internamente lo resolvemos a id.
     * (Ideal: ir migrando llamadas a updateProducts(id, ...))
     */
    fun updateProductsByName(name: String, newProducts: List<Product>) {
        val id = getIdByName(name)
        if (id != null) updateProducts(id, newProducts)
        else Log.d(TAG_GLOBAL, "FakeUserLists → No se encontró lista con nombre='$name'")
    }

    /** Borra todo el repositorio (reset). */
    fun clear() {
        store.clear()
        emit()
        Log.d(TAG_GLOBAL, "FakeUserLists → Repositorio limpiado")
    }

    /**
     * Datos demo SIN mutar el repositorio.
     * Útil para previews.
     */
    fun sampleData(): List<Pair<String, UserList>> {
        val id = "demo-compra-semanal"
        val demo = UserList(
            id = id,
            name = "Compra semanal",
            background = ListBackground.FONDO2,
            products = emptyList()
        )
        return listOf(id to demo)
    }

    /**
     * Inicializa datos demo SOLO si está vacío.
     * ✅ Llamar desde MainActivity o desde HomeViewModel init.
     */
    fun seedIfEmpty() {
        if (store.isNotEmpty()) return
        val demoList = UserList(
            id = "",
            name = "Compra semanal",
            background = ListBackground.FONDO2,
            products = emptyList()
        )
        add(demoList)
    }
}
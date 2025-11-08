// com.example.teammaravillaapp.data.FakeUserLists
package com.example.teammaravillaapp.data

import android.util.Log
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.util.TAG_GLOBAL
import java.util.UUID

/**
 * Repositorio **en memoria** para las listas del usuario.
 *
 * Sirve como “fake DB” para prototipar pantallas como **CreateList** y **ListDetail**.
 * No hay persistencia: al cerrar la app se pierde el contenido.
 *
 */
object FakeUserLists {

    /** Estructura interna (id único + contenido mutable). */
    private data class Entry(val id: String, var list: UserList)

    /** Almacén en memoria. */
    private val store = mutableListOf<Entry>()

    /**
     * Inserta una lista y devuelve su **ID** generado.
     *
     * @param list contenido de la lista (nombre, fondo y productos).
     * @return `String` con el identificador único (UUID).
     */
    fun add(list: UserList): String {
        val id = UUID.randomUUID().toString()
        store += Entry(id, list)
        Log.e(TAG_GLOBAL, "FakeUserLists → Añadida: id=$id, name='${list.name}', bg=${list.background}")
        return id
    }

    /**
     * Devuelve todas las listas en forma `(id, lista)`.
     *
     * Útil para pintar listados manteniendo una referencia al id.
     */
    fun all(): List<Pair<String, UserList>> = store.map { it.id to it.list }

    /**
     * Busca una lista por **id**.
     *
     * @return la lista o `null` si no existe.
     */
    fun get(id: String): UserList? = store.firstOrNull { it.id == id }?.list

    /**
     * Busca una lista por **nombre visible** (clave natural).
     *
     * Útil cuando aún no tienes navegación con ids.
     */
    fun getByName(name: String): UserList? = store.firstOrNull { it.list.name == name }?.list

    /**
     * Reemplaza el contenido de la lista con **id** dado.
     *
     * @param id identificador de la lista.
     * @param newList lista nueva completa (se hace `copy` total).
     */
    fun update(id: String, newList: UserList) {
        val entry = store.firstOrNull { it.id == id }
        if (entry != null) {
            entry.list = newList
            Log.e(TAG_GLOBAL, "FakeUserLists → Actualizada (id=$id): '${newList.name}' (${newList.products.size} productos)")
        } else {
            Log.e(TAG_GLOBAL, "FakeUserLists → No se encontró la lista con id=$id para actualizar")
        }
    }

    /**
     * Actualiza **solo los productos** de la lista con ese **nombre**.
     *
     * Útil para “guardado automático” desde ListDetail cuando se añade/borra un producto.
     *
     * @param name nombre visible de la lista.
     * @param newProducts productos nuevos a asignar.
     */
    fun updateProductsByName(name: String, newProducts: List<Product>) {
        val entry = store.firstOrNull { it.list.name == name }
        if (entry != null) {
            entry.list = entry.list.copy(products = newProducts)
            Log.e(TAG_GLOBAL, "Productos actualizados: '$name' (${newProducts.size})")
        } else {
            Log.e(TAG_GLOBAL, "No se encontró lista con nombre='$name'")
        }
    }

    /**
     * Borra todo el repositorio (reset).
     */
    fun clear() {
        store.clear()
        Log.e(TAG_GLOBAL, "Repositorio limpiado")
    }

    /**
     * Devuelve una lista de ejemplo y, si no hay datos, la **crea** primero.
     *
     * @return última lista (demo si estaba vacío).
     */
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
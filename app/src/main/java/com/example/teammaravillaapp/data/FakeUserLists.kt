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
 */
object FakeUserLists {

    /** Almacén en memoria de listas del usuario. */
    private val store = mutableListOf<UserList>()

    /**
     * Inserta una lista y devuelve su **ID**.
     *
     * Se asume que el `id` viene ya inicializado en [UserList.id].
     * Si necesitas generar uno nuevo, puedes usar [UUID.randomUUID].
     *
     * @param list contenido de la lista (id, nombre, fondo y productos).
     * @return `String` con el identificador de la lista.
     */
    fun add(list: UserList): String {
        store += list
        Log.e(
            TAG_GLOBAL,
            "FakeUserLists → Añadida: id=${list.id}, name='${list.name}', bg=${list.background}"
        )
        return list.id
    }

    /**
     * Devuelve todas las listas en forma `(id, lista)`.
     *
     * Útil para pintar listados manteniendo una referencia al id.
     */
    fun all(): List<Pair<String, UserList>> = store.map { it.id to it }

    /**
     * Busca una lista por **id**.
     *
     * @return la lista o `null` si no existe.
     */
    fun get(id: String): UserList? = store.firstOrNull { it.id == id }

    /**
     * Busca una lista por **nombre visible** (clave natural).
     *
     * Útil cuando aún no tienes navegación con ids
     * o para operaciones rápidas de demo.
     */
    fun getByName(name: String): UserList? =
        store.firstOrNull { it.name == name }

    /**
     * Reemplaza el contenido de la lista con **id** dado.
     *
     * @param id identificador de la lista.
     * @param newList lista nueva completa.
     */
    fun update(id: String, newList: UserList) {
        val index = store.indexOfFirst { it.id == id }
        if (index != -1) {
            store[index] = newList
            Log.e(
                TAG_GLOBAL,
                "FakeUserLists → Actualizada (id=$id): '${newList.name}' (${newList.products.size} productos)"
            )
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
        val index = store.indexOfFirst { it.name == name }
        if (index != -1) {
            val old = store[index]
            store[index] = old.copy(products = newProducts)
            Log.e(
                TAG_GLOBAL,
                "FakeUserLists → Productos actualizados: '$name' (${newProducts.size})"
            )
        } else {
            Log.e(TAG_GLOBAL, "FakeUserLists → No se encontró lista con nombre='$name'")
        }
    }

    /**
     * Borra todo el repositorio (reset).
     */
    fun clear() {
        store.clear()
        Log.e(TAG_GLOBAL, "FakeUserLists → Repositorio limpiado")
    }

    /**
     * Devuelve una lista de ejemplo y, si no hay datos, la **crea** primero.
     *
     * @return última lista (demo si estaba vacío).
     */
    fun sample(): UserList {
        if (store.isEmpty()) {
            val demo = UserList(
                id = UUID.randomUUID().toString(),
                name = "Compra semanal",
                background = ListBackground.FONDO2,
                products = emptyList()
            )
            add(demo)
        }
        return store.last()
    }
}
package com.example.teammaravillaapp.page.home

import com.example.teammaravillaapp.data.repository.lists.ListProgress
import com.example.teammaravillaapp.model.UserList

/**
 * Estado de UI de la pantalla Home.
 *
 * Este estado representa únicamente información necesaria para **pintar la interfaz**:
 * - El texto actual del buscador.
 * - La lista de filas (listas de usuario) ya preparadas para renderizar.
 *
 * Motivo:
 * - Evita que la UI tenga que combinar datos (listas + progreso) por su cuenta.
 * - Facilita que el ViewModel sea el responsable de “montar” el estado final.
 *
 * @property search Texto actual del campo de búsqueda. Se mantiene tal cual lo escribe el usuario.
 * @property rows Filas de listas ya listas para mostrar (incluye progreso por lista).
 *
 * Ejemplo de uso:
 * {@code
 * val uiState by vm.uiState.collectAsStateWithLifecycle()
 * SearchField(value = uiState.search, onValueChange = vm::onSearchChange)
 * uiState.rows.forEach { row -> ListCard(...) }
 * }
 */
data class HomeUiState(
    val search: String = "",
    val rows: List<HomeListRow> = emptyList()
)

/**
 * Modelo de presentación para una fila en Home.
 *
 * Contiene:
 * - El identificador de la lista.
 * - El modelo de dominio [UserList] (nombre, fondo, etc.).
 * - El progreso calculado de la lista ([ListProgress]) para mostrar “X/Y”.
 *
 * Nota:
 * - Aunque [UserList] ya incluye un id, se mantiene [id] explícito para simplificar
 *   mapeos, keys de LazyColumn y evitar depender de estructura interna del modelo.
 *
 * @property id Identificador único de la lista. No debe ser vacío.
 * @property list Modelo de dominio de la lista (nombre, fondo, etc.).
 * @property progress Progreso de la lista (marcados/total). Si no existe, se usa 0/0.
 *
 * Ejemplo de uso:
 * {@code
 * HomeListRow(
 *   id = list.id,
 *   list = list,
 *   progress = ListProgress(checkedCount = 2, totalCount = 10)
 * )
 * }
 */
data class HomeListRow(
    val id: String,
    val list: UserList,
    val progress: ListProgress = ListProgress(checkedCount = 0, totalCount = 0)
)
package com.example.teammaravillaapp.page.listdetail

/**
 * Acciones (intenciones) que el usuario puede realizar en la pantalla de detalle de lista.
 *
 * Este sealed interface se usa normalmente desde el ViewModel para:
 * - traducir interacción de UI (clicks, cambios de texto, etc.) a comandos de dominio
 * - decidir si una acción implica persistencia (repositorio) o solo afecta a UI (estado local)
 *
 * Nota de arquitectura:
 * - Acciones como [QueryChanged] suelen afectar únicamente al estado de búsqueda en UI.
 * - Acciones como [AddProduct], [RemoveProduct], etc. suelen delegarse en un UseCase/Repository.
 *
 * Ejemplo de uso:
 * {@code
 * fun onSearchTextChange(text: String) {
 *   dispatch(ListDetailAction.QueryChanged(text))
 * }
 *
 * fun onAddProduct(productId: String) {
 *   dispatch(ListDetailAction.AddProduct(productId))
 * }
 * }
 *
 * @see com.example.teammaravillaapp.page.listdetail.usecase.ListDetailHandleActionUseCase
 */
sealed interface ListDetailAction {

    /** Elimina todos los productos actualmente marcados como “checked”. */
    data object RemoveChecked : ListDetailAction

    /** Desmarca (checked = false) todos los productos de la lista. */
    data object UncheckAll : ListDetailAction

    /** Vacía la lista por completo (elimina todos los productos). */
    data object ClearList : ListDetailAction

    /**
     * Cambio del texto de búsqueda.
     *
     * Acción de UI: normalmente actualiza un `StateFlow<String>` en ViewModel para filtrar resultados.
     *
     * @property value Texto nuevo introducido por el usuario. Puede ser vacío.
     */
    data class QueryChanged(val value: String) : ListDetailAction

    /**
     * Añade un producto a la lista.
     *
     * @property productId Identificador del producto a añadir. Debe existir en catálogo.
     */
    data class AddProduct(val productId: String) : ListDetailAction

    /**
     * Elimina un producto de la lista.
     *
     * @property productId Identificador del producto a eliminar.
     */
    data class RemoveProduct(val productId: String) : ListDetailAction

    /**
     * Alterna el estado `checked` de un producto en la lista.
     *
     * @property productId Identificador del producto a alternar.
     */
    data class ToggleChecked(val productId: String) : ListDetailAction

    /**
     * Incrementa en 1 la cantidad de un producto.
     *
     * @property productId Identificador del producto al que se incrementa la cantidad.
     */
    data class IncQuantity(val productId: String) : ListDetailAction

    /**
     * Decrementa en 1 la cantidad de un producto, con mínimo 1.
     *
     * @property productId Identificador del producto al que se decrementa la cantidad.
     */
    data class DecQuantity(val productId: String) : ListDetailAction
}
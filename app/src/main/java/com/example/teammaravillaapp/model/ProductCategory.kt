package com.example.teammaravillaapp.model

/**
 * Categorías de productos disponibles en la app.
 *
 * @property label etiqueta de la categoría para mostrar en UI.
 */
enum class ProductCategory(val label: String) {
    FRUITS("Frutas"),
    VEGETABLES("Verduras"),
    DAIRY("Lácteos"),
    BAKERY("Panadería"),
    MEAT("Carnes"),
    FISH("Pescados"),
    DRINKS("Bebidas"),
    PASTA("Pasta"),
    RICE("Arroz"),
    CLEANING("Limpieza"),
    OTHER("Otros")
}
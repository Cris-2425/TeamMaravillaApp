package com.example.teammaravillaapp.data.seed

import androidx.annotation.DrawableRes
import com.example.teammaravillaapp.model.ProductCategory

/**
 * Modelo de seed “neutro” para publicar productos en backend y/o construir catálogos.
 *
 * Motivo:
 * - Separar la representación de seed (id/nombre/categoría/imagen local) del modelo final [Product].
 * - Evitar dependencias de UI/Room/Network en el seed (solo datos mínimos + resource id opcional).
 *
 * Contrato importante:
 * - [id] debe ser **estable** y alineado con la normalización que usa el resto de la app
 *   (por ejemplo, `IdNormalizer.fromName(name)` o el normalizador de `ProductData`).
 * - Si el backend usa [id] como nombre de fichero de imagen, cambiar este id rompe URLs/cache.
 *
 * @property id Identificador estable del producto.
 * @property name Nombre legible.
 * @property category Categoría del producto.
 * @property imageRes Drawable local opcional, usado para:
 * - subir la imagen al backend (si existe flujo de upload),
 * - o rehidratar imágenes en modo offline.
 */
data class SeedItem(
    val id: String,
    val name: String,
    val category: ProductCategory,
    @DrawableRes val imageRes: Int? = null
)
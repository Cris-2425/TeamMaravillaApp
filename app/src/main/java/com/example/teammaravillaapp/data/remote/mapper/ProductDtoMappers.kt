package com.example.teammaravillaapp.data.remote.mapper

import com.example.teammaravillaapp.data.remote.dto.ProductDto
import com.example.teammaravillaapp.model.Product

/**
 * Extensiones de mapeo entre DTO de API y modelo de dominio.
 *
 * Único lugar donde ocurre la conversión entre capas:
 * - `ProductDto` <-> `Product` (domain)
 *
 * ### Estrategia
 * - Mantener la lógica de negocio separada de la capa de datos.
 * - Evitar transformaciones repetidas en ViewModel o Repository.
 * - Permitir fallback seguro para categorías desconocidas.
 */

/**
 * Convierte un [ProductDto] (API) a [Product] (domain).
 *
 * - Mapea `category` a [ProductCategory] usando [toProductCategoryOrDefault].
 * - `imageRes` se inicializa como null; se usa solo en local si aplica.
 */
fun ProductDto.toDomain(): Product =
    Product(
        id = id,
        name = name,
        category = category.toProductCategoryOrDefault(),
        imageUrl = imageUrl,
        imageRes = null
    )

/**
 * Convierte un [Product] (domain) a [ProductDto] (API).
 *
 * - Convierte la categoría a su valor API mediante [apiValue].
 * - Se excluye `imageRes` ya que es solo local.
 */
fun Product.toDto(): ProductDto =
    ProductDto(
        id = id,
        name = name,
        category = category.apiValue(),
        imageUrl = imageUrl
    )
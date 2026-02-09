package com.example.teammaravillaapp.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.model.Manzana
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Burbuja de producto reutilizable (icono + nombre).
 *
 * Muestra un producto como “burbuja” clicable, ideal para selección rápida en la pantalla de listas.
 * El componente es de presentación: no contiene lógica de negocio ni logs; delega la interacción a [onClick].
 *
 * @param modifier Modificador de Compose para controlar tamaño/posición.
 * @param product Producto a representar. Se utiliza su nombre e imagen (URL o recurso local).
 * @param onClick Acción a ejecutar cuando el usuario pulsa la burbuja.
 *
 * @see ProductImage
 *
 * Ejemplo de uso:
 * {@code
 * ProductBubble(
 *   product = product,
 *   onClick = { viewModel.onProductSelected(product.id) }
 * )
 * }
 */
@Composable
fun ProductBubble(
    product: Product,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.width(72.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onClick)
        ) {
            ProductImage(
                name = product.name,
                imageUrl = product.imageUrl,
                imageRes = product.imageRes,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = product.name,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductBubblePreview() {
    TeamMaravillaAppTheme {
        ProductBubble(product = Manzana, onClick = {})
    }
}
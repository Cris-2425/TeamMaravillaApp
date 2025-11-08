package com.example.teammaravillaapp.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.model.SearchFieldData
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Campo de texto reutilizable para búsquedas.
 *
 * Se usa en pantallas como el *Home* o *ListDetail* para permitir
 * filtrar productos o listas. Implementa un `OutlinedTextField`
 * con diseño Material 3, adaptable y simple.
 *
 * Funcionalmente:
 * - Llama a [onValueChange] cada vez que cambia el texto.
 * - Muestra el texto de [searchData.placeholder] cuando está vacío.
 * - Ocupa todo el ancho disponible con [Modifier.fillMaxWidth].
 *
 * @param searchData Datos actuales del campo (texto y placeholder).
 * @param modifier Permite aplicar ajustes externos (padding, width, etc.).
 * @param onValueChange Acción a ejecutar cada vez que el usuario escribe.
 */
@Composable
fun SearchField(
    searchData: SearchFieldData,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        value = searchData.value,
        onValueChange = onValueChange,
        placeholder = {
            Text(searchData.placeholder)
        },
        modifier = modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchField() {
    TeamMaravillaAppTheme {
        SearchField(
            searchData = SearchFieldData(
                value = "",
                placeholder = "Buscar producto o lista"
            ),
            onValueChange = {}
        )
    }
}
package com.example.teammaravillaapp.page

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.BackgroundGrid
import com.example.teammaravillaapp.component.CreateListTopBar
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.SuggestedListSection
import com.example.teammaravillaapp.data.FakeUserLists
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.ListBackgrounds
import com.example.teammaravillaapp.model.SuggestedListData
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

/**
 * Contenedor de la pantalla de **creación de lista**.
 *
 * Coloca el contenido y el botón de volver reutilizable.
 *
 * @param onBack acción al pulsar atrás (por ejemplo, navController.navigateUp()).
 * @param onListCreated callback con el id de la lista creada (para navegar a su detalle).
 */
@Composable
fun CreateListt(
    onBack: () -> Unit = {},
    onListCreated: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CreateListContent(
            onBack = onBack,
            onListCreated = onListCreated
        )

        Box(modifier = Modifier.align(Alignment.BottomStart)) {
            BackButton(onClick = onBack)
        }
    }
}

/**
 * Contenido principal de **Crear Lista**.
 *
 * - Campo de nombre.
 * - Selección de fondo a través de [BackgroundGrid].
 * - Sugerencias rápidas con [SuggestedListSection].
 * - TopBar con acciones de cancelar/guardar usando [CreateListTopBar].
 *
 * Al pulsar **Guardar**, añade una nueva [UserList] a [FakeUserLists]
 * y dispara [onListCreated] con el id generado.
 *
 * @param onBack acción para cerrar la pantalla sin guardar.
 * @param onListCreated callback con el id de la nueva lista.
 */
@Composable
fun CreateListContent(
    onBack: () -> Unit = {},
    onListCreated: (String) -> Unit = {}
) {
    var name by rememberSaveable { mutableStateOf("") }
    var selectedBackground by rememberSaveable { mutableStateOf(ListBackground.FONDO1) }
    val backgroundRes = ListBackgrounds.getBackgroundRes(selectedBackground)

    Box(Modifier.fillMaxSize()) {
        // Fondo general con la imagen del fondo elegido
        GeneralBackground(bgRes = backgroundRes)

        Column(Modifier.fillMaxSize()) {

            // TopBar con Cancelar / Guardar
            CreateListTopBar(
                onCancel = {
                    Log.e(TAG_GLOBAL, "Crear Lista → Cancelar")
                    onBack()
                },
                onSave = {
                    val newList = UserList(
                        name = name.ifBlank { "Nueva lista" },
                        background = selectedBackground,
                        products = emptyList()
                    )
                    val id = FakeUserLists.add(newList)
                    Log.e(TAG_GLOBAL, "Crear Lista → Guardada: id=$id, name='${newList.name}'")
                    onListCreated(id)
                },
                saveEnabled = name.isNotBlank()
            )

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        Log.e(TAG_GLOBAL, "Nueva lista con nombre: '$it'")
                    },
                    placeholder = { Text("Nombre lista") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    "Fondo de la lista",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(8.dp))

                BackgroundGrid(
                    selectedBg = selectedBackground,
                    onSelect = { chosen ->
                        selectedBackground = chosen
                        Log.e(TAG_GLOBAL, "Fondo elegido: $chosen")
                    }
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    "Listas sugeridas",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(8.dp))

                SuggestedListSection(items = SuggestedListData.items) { picked ->
                    name = picked.name
                    Log.e(TAG_GLOBAL, "Lista sugerida: '${picked.name}'")
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Preview()
@Composable
fun PreviewCreateListt() {
    TeamMaravillaAppTheme {
        CreateListt()
    }
}
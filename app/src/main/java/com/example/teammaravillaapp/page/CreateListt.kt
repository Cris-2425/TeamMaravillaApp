package com.example.teammaravillaapp.page

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
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
import java.util.UUID

/**
 * Contenedor de la pantalla de **creación de lista**.
 *
 * @param onBack Acción al pulsar atrás.
 * @param onListCreated Acción al crear la lista correctamente (devuelve el id).
 */
@Composable
fun CreateListt(
    onBack: () -> Unit,
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
 * Al pulsar **Guardar**, añade una nueva [UserList] a [FakeUserLists].
 */
@Composable
fun CreateListContent(
    onBack: () -> Unit,
    onListCreated: (String) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var selectedBackground by rememberSaveable { mutableStateOf(ListBackground.FONDO1) }
    val backgroundRes = ListBackgrounds.getBackgroundRes(selectedBackground)
    val defaultName = stringResource(R.string.create_list_default_name)

    Box(Modifier.fillMaxSize()) {
        GeneralBackground(bgRes = backgroundRes)

        Column(Modifier.fillMaxSize()) {

            CreateListTopBar(
                onCancel = {
                    Log.d(TAG_GLOBAL, "Crear Lista → Cancelar")
                    onBack()
                },
                onSave = {
                    val finalName = name.ifBlank { defaultName }

                    val newList = UserList(
                        id = UUID.randomUUID().toString(),
                        name = finalName,
                        background = selectedBackground,
                        products = emptyList()
                    )

                    val id = FakeUserLists.add(newList)
                    Log.d(
                        TAG_GLOBAL,
                        "Crear Lista → Guardada: id=$id, name='${newList.name}' bg=${newList.background}"
                    )
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
                        Log.d(TAG_GLOBAL, "Crear Lista → nombre: '$it'")
                    },
                    placeholder = { Text(stringResource(R.string.create_list_name_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.create_list_background_title),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(8.dp))

                BackgroundGrid(
                    selectedBg = selectedBackground,
                    onSelect = { chosen ->
                        selectedBackground = chosen
                        Log.d(TAG_GLOBAL, "Crear Lista → fondo elegido: $chosen")
                    }
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.create_list_suggested_title),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(8.dp))

                SuggestedListSection(items = SuggestedListData.items) { picked ->
                    name = picked.name
                    Log.d(TAG_GLOBAL, "Crear Lista → lista sugerida: '${picked.name}'")
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Preview
@Composable
fun PreviewCreateListt() {
    TeamMaravillaAppTheme {
        CreateListt(
            onBack = {},
            onListCreated = {}
        )
    }
}
package com.example.teammaravillaapp.page.createlist

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.BackgroundGrid
import com.example.teammaravillaapp.component.CreateListTopBar
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.SuggestedListSection
import com.example.teammaravillaapp.model.ListBackgrounds
import com.example.teammaravillaapp.model.SuggestedListData
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

@Composable
fun CreateListt(
    onBack: () -> Unit = {},
    onListCreated: (String) -> Unit = {},
    createListViewModel: CreateListViewModel = viewModel()
) {
    Box(modifier = Modifier.fillMaxSize()) {

        CreateListContent(
            onBack = onBack,
            onListCreated = onListCreated,
            createListViewModel = createListViewModel
        )

        Box(modifier = Modifier.align(Alignment.BottomStart)) {
            BackButton(onClick = onBack)
        }
    }
}

@Composable
fun CreateListContent(
    onBack: () -> Unit = {},
    onListCreated: (String) -> Unit = {},
    createListViewModel: CreateListViewModel
) {
    val uiState by createListViewModel.uiState.collectAsState()
    val backgroundRes = ListBackgrounds.getBackgroundRes(uiState.selectedBackground)

    Box(Modifier.fillMaxSize()) {
        GeneralBackground(bgRes = backgroundRes)

        Column(Modifier.fillMaxSize()) {

            CreateListTopBar(
                onCancel = {
                    Log.d(TAG_GLOBAL, "Crear Lista → Cancelar")
                    onBack()
                },
                onSave = {
                    Log.d(TAG_GLOBAL, "Crear Lista → Guardar")
                    createListViewModel.save(onListCreated)
                },
                // ✅ coherente con finalName
                saveEnabled = true
            )

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = {
                        createListViewModel.onNameChange(it)
                        Log.d(TAG_GLOBAL, "Crear Lista → Nombre: '$it'")
                    },
                    placeholder = { Text(stringResource(R.string.home_quick_new_list)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Fondo de la lista",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(8.dp))

                BackgroundGrid(
                    selectedBg = uiState.selectedBackground,
                    onSelect = { chosen ->
                        createListViewModel.onBackgroundSelect(chosen)
                        Log.d(TAG_GLOBAL, "Crear Lista → Fondo elegido: $chosen")
                    }
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Listas sugeridas",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(8.dp))

                SuggestedListSection(items = SuggestedListData.items) { picked ->
                    createListViewModel.onSuggestedPicked(picked.name)
                    Log.d(TAG_GLOBAL, "Crear Lista → Lista sugerida: '${picked.name}'")
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
        CreateListt()
    }
}
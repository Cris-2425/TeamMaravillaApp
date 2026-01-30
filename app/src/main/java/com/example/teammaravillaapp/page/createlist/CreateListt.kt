package com.example.teammaravillaapp.page.createlist

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.BackgroundGrid
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.SuggestedListSection
import com.example.teammaravillaapp.data.seed.ListBackgrounds
import com.example.teammaravillaapp.data.seed.SuggestedListData
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.util.TAG_GLOBAL

@Composable
fun CreateListt(
    onBack: () -> Unit = {},
    onListCreated: (String) -> Unit = {},
    onUiEvent: (UiEvent) -> Unit = {},
    vm: CreateListViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(vm) {
        vm.events.collect { onUiEvent(it) }
    }

    val backgroundRes = ListBackgrounds.getBackgroundRes(uiState.selectedBackground)

    Box(modifier = Modifier.fillMaxSize()) {
        GeneralBackground(bgRes = backgroundRes, overlayAlpha = 0.20f) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(18.dp))

                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = {
                        vm.onNameChange(it)
                        Log.d(TAG_GLOBAL, "Crear Lista → Nombre: '$it'")
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
                    selectedBg = uiState.selectedBackground,
                    onSelect = { chosen ->
                        vm.onBackgroundSelect(chosen)
                        Log.d(TAG_GLOBAL, "Crear Lista → Fondo elegido: $chosen")
                    }
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.create_list_suggested_title),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(8.dp))

                SuggestedListSection(items = SuggestedListData.items) { picked ->
                    vm.onSuggestedPicked(picked.name, picked.productIds)
                    Log.d(TAG_GLOBAL, "Crear Lista → Lista sugerida: '${picked.name}' (ids=${picked.productIds.size})")
                }

                Spacer(Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 18.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            Log.d(TAG_GLOBAL, "Crear Lista → Cancelar")
                            onBack()
                        },
                        modifier = Modifier.weight(1f)
                    ) { Text(stringResource(R.string.common_cancel)) }

                    Button(
                        onClick = {
                            Log.d(TAG_GLOBAL, "Crear Lista → Guardar")
                            vm.save(onListCreated)
                        },
                        modifier = Modifier.weight(1f)
                    ) { Text(stringResource(R.string.common_save)) }
                }
            }
        }

        //Box(modifier = Modifier.align(Alignment.BottomStart)) {
        //    BackButton(onClick = onBack)
        //}
    }
}
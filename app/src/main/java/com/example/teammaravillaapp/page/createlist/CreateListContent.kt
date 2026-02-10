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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackgroundGrid
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.SuggestedListSection
import com.example.teammaravillaapp.data.seed.ListBackgrounds
import com.example.teammaravillaapp.data.seed.SuggestedListData
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

/**
 * UI pura de la pantalla **CreateList**.
 *
 * Motivo:
 * - Facilita Previews (rúbrica).
 * - Evita dependencias a Hilt/Nav.
 * - Permite reutilización y testing.
 *
 * @param uiState Estado actual a renderizar.
 * @param onBack Acción de cancelar/volver.
 * @param onNameChange Callback al cambiar el nombre.
 * @param onBackgroundSelect Callback al seleccionar fondo.
 * @param onSuggestedPick Callback al elegir una sugerida (nombre + ids).
 * @param onSave Acción al guardar.
 *
 * Ejemplo:
 * {@code
 * CreateListContent(
 *   uiState = state,
 *   onBack = { nav.popBackStack() },
 *   onNameChange = vm::onNameChange,
 *   onBackgroundSelect = vm::onBackgroundSelect,
 *   onSuggestedPick = vm::onSuggestedPicked,
 *   onSave = { vm.save { ... } }
 * )
 * }
 */
@Composable
fun CreateListContent(
    uiState: CreateListUiState,
    onBack: () -> Unit,
    onNameChange: (String) -> Unit,
    onBackgroundSelect: (ListBackground) -> Unit,
    onSuggestedPick: (String, List<String>) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundRes = ListBackgrounds.getBackgroundRes(uiState.selectedBackground)

    Box(modifier = modifier.fillMaxSize()) {
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
                        onNameChange(it)
                        Log.d(TAG_GLOBAL, "CreateList → Name='$it'")
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
                    imageResProvider = { ListBackgrounds.getBackgroundRes(it) },
                    onSelect = onBackgroundSelect
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.create_list_suggested_title),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(8.dp))

                SuggestedListSection(items = SuggestedListData.items) { picked ->
                    onSuggestedPick(picked.name, picked.productIds)
                    Log.d(TAG_GLOBAL, "CreateList → Suggested='${picked.name}' (ids=${picked.productIds.size})")
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
                            Log.d(TAG_GLOBAL, "CreateList → Cancel")
                            onBack()
                        },
                        modifier = Modifier.weight(1f)
                    ) { Text(stringResource(R.string.common_cancel)) }

                    Button(
                        onClick = {
                            Log.d(TAG_GLOBAL, "CreateList → Save")
                            onSave()
                        },
                        modifier = Modifier.weight(1f),
                        enabled = uiState.trimmedName.isNotBlank()
                    ) { Text(stringResource(R.string.common_save)) }
                }
            }
        }
    }
}
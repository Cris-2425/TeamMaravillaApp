package com.example.teammaravillaapp.component

import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

/**
 * TopBar para **crear lista** con acciones de Cancelar/Guardar.
 *
 * @param onCancel callback al pulsar “Cancelar”.
 * @param onSave callback al pulsar “Guardar”.
 * @param saveEnabled habilita o deshabilita el botón Guardar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListTopBar(
    onCancel: () -> Unit = {},
    onSave: () -> Unit = {},
    saveEnabled: Boolean = true
) {
    CenterAlignedTopAppBar(
        title = {
            Title(texto = stringResource(R.string.create_list_topbar_title))
        },
        navigationIcon = {
            Button(
                onClick = {
                    Log.d(TAG_GLOBAL, "CreateListTopBar → Cancelar")
                    onCancel()
                },
                enabled = true
            ) {
                Text(stringResource(R.string.create_list_cancel))
            }
        },
        actions = {
            Button(
                onClick = {
                    Log.d(TAG_GLOBAL, "CreateListTopBar → Guardar")
                    onSave()
                },
                enabled = saveEnabled
            ) {
                Text(stringResource(R.string.create_list_save))
            }
        }
    )
}

@Preview
@Composable
fun PreviewCreateListTopBar() {
    TeamMaravillaAppTheme {
        CreateListTopBar()
    }
}
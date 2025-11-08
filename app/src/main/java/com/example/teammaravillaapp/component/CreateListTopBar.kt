package com.example.teammaravillaapp.component

import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.teammaravillaapp.util.TAG_GLOBAL

/**
 * Barra superior reutilizable para la pantalla de creación de listas.
 *
 * @param onCancel Acción al pulsar "Cancelar".
 * @param onSave Acción al pulsar "Guardar".
 * @param saveEnabled Indica si el botón "Guardar" debe estar activo.
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
            Title("Crear Lista")
                },
        navigationIcon = {
            Button(
                onClick = {
                    Log.e(TAG_GLOBAL, "CreateListTopBar -> Cancelar")
                    onCancel()
                },
                enabled = true
            ) {
                Text("Cancelar")
            }
        },
        actions = {
            Button(
                onClick = {
                    Log.e(TAG_GLOBAL, "CreateListTopBar -> Guardar")
                    onSave()
                },
                enabled = saveEnabled
            ) {
                Text("Guardar")
            }
        }
    )
}
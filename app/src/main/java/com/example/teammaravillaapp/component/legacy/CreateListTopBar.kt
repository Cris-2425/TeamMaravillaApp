package com.example.teammaravillaapp.component.legacy

import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Top bar para la pantalla de creación de lista con acciones de Cancelar y Guardar.
 *
 * Este componente es puramente de presentación: muestra un título centrado y dos acciones.
 * La lógica de navegación o persistencia se delega a los callbacks [onCancel] y [onSave],
 * lo que facilita su reutilización y mantiene la separación de responsabilidades (MVVM).
 *
 * @param modifier Modificador de Compose para personalizar tamaño, padding o comportamiento de layout.
 * @param onCancel Acción a ejecutar al pulsar “Cancelar” (por ejemplo, volver atrás o cerrar la pantalla).
 * @param onSave Acción a ejecutar al pulsar “Guardar” (por ejemplo, solicitar al ViewModel que guarde).
 * @param saveEnabled Controla si la acción “Guardar” está habilitada. Útil para validaciones (p.ej. nombre no vacío).
 *
 * @see CenterAlignedTopAppBar
 *
 * Ejemplo de uso:
 * {@code
 * CreateListTopBar(
 *   saveEnabled = uiState.canSave,
 *   onCancel = { navController.navigateUp() },
 *   onSave = { viewModel.onSaveClick() }
 * )
 * }
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListTopBar(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    saveEnabled: Boolean
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { Title(texto = stringResource(R.string.create_list_topbar_title)) },
        navigationIcon = {
            Button(onClick = onCancel) {
                Text(stringResource(R.string.create_list_cancel))
            }
        },
        actions = {
            Button(
                onClick = onSave,
                enabled = saveEnabled
            ) {
                Text(stringResource(R.string.create_list_save))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun CreateListTopBarPreview() {
    TeamMaravillaAppTheme {
        CreateListTopBar(
            onCancel = {},
            onSave = {},
            saveEnabled = true
        )
    }
}
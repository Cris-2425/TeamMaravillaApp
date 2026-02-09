package com.example.teammaravillaapp.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Diálogo para renombrar una lista.
 *
 * Versión "UI pura": el texto del campo se recibe desde el exterior (pantalla/ViewModel),
 * lo que facilita pruebas, evita estado oculto y cumple mejor con la separación de responsabilidades.
 *
 * @param modifier Modificador de Compose para personalizar el layout del diálogo (opcional).
 * @param nameValue Valor actual del campo de texto (controlado desde fuera).
 * @param onNameChange Callback para notificar cambios en el texto del campo.
 * @param onDismiss Acción al cerrar el diálogo (tap fuera o botón cancelar).
 * @param onConfirm Acción al confirmar el renombrado. Se recomienda pasar el valor ya validado/trimmed.
 *
 * Ejemplo de uso:
 * {@code
 * RenameListDialog(
 *   nameValue = uiState.renameText,
 *   onNameChange = viewModel::onRenameTextChange,
 *   onDismiss = viewModel::onRenameDismiss,
 *   onConfirm = { viewModel.onRenameConfirm() }
 * )
 * }
 */
@Composable
fun RenameListDialog(
    modifier: Modifier = Modifier,
    nameValue: String,
    onNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val title = stringResource(R.string.rename_list_title)
    val placeholder = stringResource(R.string.rename_list_placeholder)
    val save = stringResource(R.string.common_save)
    val cancel = stringResource(R.string.common_cancel)

    val trimmed = nameValue.trim()
    val canSave = trimmed.isNotBlank()

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = nameValue,
                onValueChange = onNameChange,
                singleLine = true,
                placeholder = { Text(placeholder) }
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = canSave
            ) { Text(save) }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text(cancel) }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RenameListDialogPreview() {
    TeamMaravillaAppTheme {
        RenameListDialog(
            nameValue = "Compra semanal",
            onNameChange = {},
            onDismiss = {},
            onConfirm = {}
        )
    }
}
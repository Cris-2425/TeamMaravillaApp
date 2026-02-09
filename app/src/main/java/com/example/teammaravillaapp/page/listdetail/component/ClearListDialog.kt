package com.example.teammaravillaapp.page.listdetail.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Diálogo de confirmación para vaciar completamente una lista.
 *
 * Componente de presentación: no realiza acciones por sí mismo, delega en callbacks.
 *
 * @param onDismiss Acción al cerrar el diálogo (tap fuera o botón “No”).
 * @param onConfirm Acción al confirmar el vaciado (botón “Sí”).
 * @param modifier Modificador de Compose para personalizar el diálogo (opcional).
 *
 * Ejemplo de uso:
 * {@code
 * if (uiState.showClearDialog) {
 *   ClearListDialog(
 *     onDismiss = viewModel::onDismissClearDialog,
 *     onConfirm = viewModel::onConfirmClearList
 *   )
 * }
 * }
 */
@Composable
internal fun ClearListDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.list_detail_clear_confirm_title)) },
        text = { Text(stringResource(R.string.list_detail_clear_confirm_body)) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(R.string.list_detail_clear_confirm_yes))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(stringResource(R.string.list_detail_clear_confirm_no))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ClearListDialogPreview() {
    TeamMaravillaAppTheme {
        ClearListDialog(
            onDismiss = {},
            onConfirm = {}
        )
    }
}
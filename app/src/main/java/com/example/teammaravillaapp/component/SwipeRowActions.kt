package com.example.teammaravillaapp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Contenedor de fila con acciones por gesto swipe (editar / borrar).
 *
 * - Swipe StartToEnd (izquierda → derecha): dispara [onEdit] sin descartar la fila.
 * - Swipe EndToStart (derecha → izquierda): dispara [onDelete] y permite descartar la fila.
 *
 * Nota: se usa [rowKey] para poder resetear el estado del swipe si cambia el item (por recomposición/undo).
 *
 * @param modifier Modificador de Compose para controlar layout.
 * @param rowKey Identificador estable del item (id de la lista, producto, etc.) para resetear el estado del swipe.
 * @param onDelete Acción de borrar (EndToStart).
 * @param onEdit Acción de editar/renombrar (StartToEnd).
 * @param content Contenido visual de la fila.
 *
 * Ejemplo de uso:
 * {@code
 * SwipeRowActions(
 *   rowKey = list.id.toString(),
 *   onDelete = { viewModel.onDelete(list.id) },
 *   onEdit = { viewModel.onRenameStart(list.id) }
 * ) {
 *   ListCard(...)
 * }
 * }
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeRowActions(
    modifier: Modifier = Modifier,
    rowKey: String,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    content: @Composable () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    onEdit()
                    false // no descartamos la fila
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    onDelete()
                    true // descartamos la fila
                }
                else -> false
            }
        }
    )

    LaunchedEffect(rowKey) { dismissState.reset() }

    SwipeToDismissBox(
        modifier = modifier,
        state = dismissState,
        enableDismissFromStartToEnd = true,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(cs.tertiaryContainer)
                            .padding(horizontal = 18.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = cs.onTertiaryContainer
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.swipe_action_rename),
                                color = cs.onTertiaryContainer,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(cs.errorContainer)
                            .padding(horizontal = 18.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = cs.onErrorContainer
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.swipe_action_delete),
                                color = cs.onErrorContainer,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }

                else -> Unit
            }
        }
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun SwipeRowActionsPreview() {
    TeamMaravillaAppTheme {
        SwipeRowActions(
            rowKey = "1",
            onDelete = {},
            onEdit = {}
        ) {
            Surface(Modifier.fillMaxWidth().padding(16.dp)) {
                Text("Contenido de fila (preview)", modifier = Modifier.padding(16.dp))
            }
        }
    }
}
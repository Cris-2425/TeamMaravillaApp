package com.example.teammaravillaapp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeRowActions(
    id: String,
    onDelete: () -> Unit,      // EndToStart (izquierda)
    onEdit: () -> Unit,        // StartToEnd (derecha)
    content: @Composable () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    onEdit()
                    false // 👈 NO descartamos la fila (solo acción)
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    onDelete()
                    true // 👈 aquí sí se “descarta” (desaparece)
                }
                else -> false
            }
        }
    )

    // Si la fila “vuelve” por undo o recomposición, resetea
    LaunchedEffect(id) { dismissState.reset() }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = true,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            val dir = dismissState.dismissDirection
            when (dir) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    // Fondo renombrar (derecha)
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
                                text = "Renombrar",
                                color = cs.onTertiaryContainer,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    // Fondo borrar (izquierda)
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
                                text = "Borrar",
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
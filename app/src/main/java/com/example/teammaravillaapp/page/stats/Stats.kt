package com.example.teammaravillaapp.page.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Pantalla contenedora de Estadísticas.
 *
 * Responsabilidades:
 * - Obtener el [StatsViewModel] (Hilt) y recolectar su estado.
 * - Escuchar eventos one-shot (snackbars) y reenviarlos a [onUiEvent].
 * - Delegar el render a [StatsContent] (presentación pura).
 *
 * Motivo:
 * - Separar dependencias Android/DI de la UI pura, permitiendo previews y tests.
 *
 * @param onBack Callback de navegación hacia atrás.
 * Restricciones:
 * - No nulo.
 * - Debe ser rápido (se ejecuta en el hilo de UI).
 * @param onUiEvent Consumidor de eventos de UI one-shot (snackbars).
 * Restricciones:
 * - No nulo.
 * - Se recomienda que sea idempotente.
 * @param vm ViewModel inyectado por Hilt. Se permite override para tests/inyección manual.
 *
 * @see StatsContent UI pura.
 * @see StatsViewModel Lógica de carga y refresco.
 *
 * Ejemplo de uso:
 * {@code
 * StatsScreen(
 *   onBack = navController::popBackStack,
 *   onUiEvent = { event -> handleUiEvent(event) }
 * )
 * }
 */
@Composable
fun Stats(
    onBack: () -> Unit,
    onUiEvent: (UiEvent) -> Unit,
    vm: StatsViewModel = hiltViewModel()
) {
    LaunchedEffect(vm) {
        vm.events.collect { onUiEvent(it) }
    }

    StatsContent(
        uiState = vm.uiState,
        onRetry = vm::refresh,
        onBack = onBack
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewStats_Loading() {
    TeamMaravillaAppTheme {
        StatsContent(
            uiState = MutableStateFlow(StatsUiState(isLoading = true)),
            onRetry = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewStats_WithData() {
    TeamMaravillaAppTheme {
        StatsContent(
            uiState = MutableStateFlow(
                StatsUiState(
                    isLoading = false,
                    lists = 6,
                    products = 120,
                    recipes = 24,
                    favorites = 5,
                    totalItems = 80,
                    checkedItems = 31,
                    listsLast7Days = 2,
                    itemsLast7Days = 17,
                    topProducts = listOf(
                        TopProductStat("Leche", 12),
                        TopProductStat("Pan", 9),
                        TopProductStat("Huevos", 7)
                    )
                )
            ),
            onRetry = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewStats_Error() {
    TeamMaravillaAppTheme {
        StatsContent(
            uiState = MutableStateFlow(
                StatsUiState(
                    isLoading = false,
                    error = IllegalStateException("DB locked"),
                    lists = 6,
                    products = 120
                )
            ),
            onRetry = {},
            onBack = {}
        )
    }
}
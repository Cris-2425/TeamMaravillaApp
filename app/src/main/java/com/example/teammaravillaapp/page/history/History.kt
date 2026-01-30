package com.example.teammaravillaapp.page.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.ListCard
import com.example.teammaravillaapp.component.SectionCard
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.model.CardInfo
import com.example.teammaravillaapp.ui.events.UiEvent

@Composable
fun History(
    onBack: () -> Unit,
    onOpenList: (String) -> Unit,
    onUiEvent: (UiEvent) -> Unit,
    vm: HistoryViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(vm) {
        vm.events.collect { onUiEvent(it) }
    }

    Box(Modifier.fillMaxSize()) {
        GeneralBackground(overlayAlpha = 0.20f) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Title(texto = stringResource(R.string.history_title))

                SectionCard {
                    when {
                        uiState.isLoading -> {
                            Text(
                                text = stringResource(R.string.common_loading),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        uiState.rows.isEmpty() -> {
                            Text(
                                text = stringResource(R.string.history_empty),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        else -> {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                TextButton(onClick = vm::onClearAll) {
                                    Text(stringResource(R.string.history_clear))
                                }
                            }

                            Spacer(Modifier.height(8.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                uiState.rows.forEach { row ->
                                    ListCard(
                                        cardInfo = CardInfo(
                                            imageID = R.drawable.list_supermarket,
                                            imageDescription = row.name,
                                            title = row.name,
                                            subtitle = stringResource(R.string.history_open_again)
                                        ),
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = { onOpenList(row.id) }
                                    )

                                    TextButton(onClick = { vm.onRemove(row.id) }) {
                                        Text(stringResource(R.string.history_remove))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Box(Modifier.align(Alignment.BottomStart)) {
        //    BackButton(onClick = onBack)
        //}
    }
}
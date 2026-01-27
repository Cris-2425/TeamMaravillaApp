package com.example.teammaravillaapp.page.history

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.ListCard
import com.example.teammaravillaapp.component.SectionCard
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.data.prefs.RecentListsPrefs
import kotlinx.coroutines.launch

@Composable
fun History(
    onBack: () -> Unit,
    onOpenList: (String) -> Unit,
    vm: HistoryViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    // 1) leemos ids del DataStore
    val recentIds by RecentListsPrefs.observeIds(ctx).collectAsState(initial = emptyList())

    // 2) se los pasamos al VM (para que combine con Room)
    LaunchedEffect(recentIds) {
        vm.setRecentIds(recentIds)
    }

    val uiState by vm.uiState.collectAsState()

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
                if (uiState.rows.isEmpty()) {
                    Text(
                        text = stringResource(R.string.history_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { scope.launch { RecentListsPrefs.clear(ctx) } }) {
                            Text(stringResource(R.string.history_clear))
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        uiState.rows.forEach { row ->
                            ListCard(
                                cardInfo = com.example.teammaravillaapp.model.CardInfo(
                                    imageID = com.example.teammaravillaapp.R.drawable.list_supermarket,
                                    imageDescription = row.name,
                                    title = row.name,
                                    subtitle = stringResource(R.string.history_open_again)
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onOpenList(row.id) }
                            )

                            TextButton(
                                onClick = { scope.launch { RecentListsPrefs.remove(ctx, row.id) } }
                            ) {
                                Text(stringResource(R.string.history_remove))
                            }
                        }
                    }
                }
            }
        }

        Box(Modifier.align(Alignment.BottomStart)) {
            BackButton(onClick = onBack)
        }
    }
}
    }
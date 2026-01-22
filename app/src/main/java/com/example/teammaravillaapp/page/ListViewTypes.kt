package com.example.teammaravillaapp.page

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.CircularOption
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.model.ListStyle
import com.example.teammaravillaapp.page.prefs.UserPrefsViewModel
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

@Composable
fun ListViewTypes(
    onCancel: () -> Unit = {},
    onSave: () -> Unit = {},
    vm: UserPrefsViewModel = hiltViewModel()
) {
    val prefsState by vm.uiState.collectAsState()

    var current by rememberSaveable { mutableStateOf(ListStyle.LISTA) }

    LaunchedEffect(prefsState.listStyle) {
        current = prefsState.listStyle
    }

    Box(Modifier.fillMaxSize()) {
        GeneralBackground(overlayAlpha = 0.15f)

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onCancel) {
                    Text(text = stringResource(R.string.list_view_types_cancel))
                }
                Title(texto = stringResource(R.string.list_view_types_title))
                TextButton(onClick = {
                    vm.setListStyle(current)
                    onSave()
                }) {
                    Text(text = stringResource(R.string.list_view_types_save))
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.list_view_types_subtitle),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularOption(
                    label = stringResource(R.string.list_view_types_option_list),
                    selected = current == ListStyle.LISTA
                ) { current = ListStyle.LISTA }

                CircularOption(
                    label = stringResource(R.string.list_view_types_option_mosaic),
                    selected = current == ListStyle.MOSAIC
                ) { current = ListStyle.MOSAIC }

                CircularOption(
                    label = stringResource(R.string.list_view_types_option_other),
                    selected = current == ListStyle.ETC
                ) { current = ListStyle.ETC }
            }
        }

        Box(Modifier.align(Alignment.BottomStart)) {
            BackButton(onClick = onCancel)
        }
    }
}

@Preview
@Composable
fun PreviewListViewTypes() {
    TeamMaravillaAppTheme {
        ListViewTypes()
    }
}
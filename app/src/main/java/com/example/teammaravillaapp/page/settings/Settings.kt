package com.example.teammaravillaapp.page.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.ThemeModeRow
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.model.ThemeMode
import com.example.teammaravillaapp.ui.app.ThemeViewModel

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    vm: ThemeViewModel = hiltViewModel()
) {
    val mode by vm.themeMode.collectAsStateWithLifecycle()

    Box(Modifier.fillMaxSize()) {
        GeneralBackground(overlayAlpha = 0.20f) {

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Title(texto = stringResource(R.string.settings_title))

                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    tonalElevation = 2.dp
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.settings_theme_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = stringResource(R.string.settings_theme_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(6.dp))

                        ThemeModeRow(
                            title = stringResource(R.string.settings_theme_system),
                            selected = mode == ThemeMode.SYSTEM,
                            onClick = { vm.setThemeMode(ThemeMode.SYSTEM) }
                        )
                        ThemeModeRow(
                            title = stringResource(R.string.settings_theme_light),
                            selected = mode == ThemeMode.LIGHT,
                            onClick = { vm.setThemeMode(ThemeMode.LIGHT) }
                        )
                        ThemeModeRow(
                            title = stringResource(R.string.settings_theme_dark),
                            selected = mode == ThemeMode.DARK,
                            onClick = { vm.setThemeMode(ThemeMode.DARK) }
                        )
                    }
                }
            }

            //Box(Modifier.align(Alignment.BottomStart)) {
            //    BackButton(onClick = onBack)
            //}
        }
    }
}
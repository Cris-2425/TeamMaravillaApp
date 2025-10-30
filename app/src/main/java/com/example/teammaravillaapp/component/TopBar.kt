package com.example.teammaravillaapp.component

import android.icu.text.StringSearch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    onMenuClick: () -> Unit = {},
    onSearch: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    TopAppBar(title = {

    }
    )
}

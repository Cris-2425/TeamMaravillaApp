package com.example.teammaravillaapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController

@Composable
fun rememberNavActions(navController: NavHostController): NavActions =
    remember(navController) { NavActions(navController) }

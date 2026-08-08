package com.example.shoply.presentation.screens.settingscreen

import SettingsScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object SettingsDestination

fun NavGraphBuilder.settingScreen(
    onNavigateToPassword: () -> Unit,
) {
    composable<SettingsDestination> {
        SettingsScreen(onNavigateToPassword = onNavigateToPassword)
    }
}

fun NavController.navigateToSettingsScreen() {
    navigate(SettingsDestination) {
        launchSingleTop = true
    }
}

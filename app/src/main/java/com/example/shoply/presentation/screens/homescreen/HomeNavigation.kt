package com.example.shoply.presentation.screens.homescreen

import FabConfig
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object HomeDestination

fun NavGraphBuilder.homeScreen(
    onFabConfigChange: (FabConfig) -> Unit
) {
    composable<HomeDestination> {
        HomeScreen(
            onFabConfigChange = onFabConfigChange
        )
    }
}

fun NavController.navigateToHomeScreen() {
    navigate(HomeDestination) {
        popUpTo(graph.id)
    }
}
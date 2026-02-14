package com.example.shoply.presentation.screens.homescreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object HomeDestination

fun NavGraphBuilder.homeScreen(
) {
    composable<HomeDestination> {
        HomeScreen()
    }
}

fun NavController.navigateToHomeScreen() {
    navigate(HomeDestination) {
        popUpTo(graph.id)
    }
}
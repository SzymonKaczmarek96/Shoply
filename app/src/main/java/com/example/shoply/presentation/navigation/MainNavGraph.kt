package com.example.shoply.presentation.navigation

import MainScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable


@Serializable
data object MainScreenDestination

fun NavGraphBuilder.mainNavGraph(
    rootNavController: NavController
) {
    navigation<MainNavGraph>(
        startDestination = MainScreenDestination
    ) {
        composable<MainScreenDestination> {
            MainScreen(
                onLogoutClick = {
                    rootNavController.navigate(LoginNavGraph) {
                        popUpTo(MainNavGraph) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
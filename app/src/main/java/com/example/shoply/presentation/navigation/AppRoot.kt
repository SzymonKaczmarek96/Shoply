package com.example.shoply.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

//change selected item in bottomBarScreen
// split screen which are using bottom bar and top bar from login screen


@Serializable
data object LoginNavGraph

@Serializable
data object MainNavGraph

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = LoginNavGraph
    ) {
        loginNavGraph(navController)
        mainNavGraph(navController)
    }
}

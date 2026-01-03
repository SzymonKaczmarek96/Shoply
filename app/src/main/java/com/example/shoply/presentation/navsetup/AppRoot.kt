package com.example.shoply.presentation.navsetup

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.shoply.presentation.screens.homescreen.homeScreen
import com.example.shoply.presentation.screens.homescreen.navigateToHomeScreen
import com.example.shoply.presentation.screens.loginscreen.LoginDestination
import com.example.shoply.presentation.screens.loginscreen.loginScreen


@Composable
fun AppRoot() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = LoginDestination
    ) {
        loginScreen(
            onLoginClick = { navController.navigateToHomeScreen() },
            onCreateAccountClick = {},
            onGoogleLoginClick = {},
            onAppleLoginClick = {}
        )
        homeScreen()
    }
}


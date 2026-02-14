package com.example.shoply.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.shoply.presentation.screens.loginscreen.LoginDestination
import com.example.shoply.presentation.screens.loginscreen.loginScreen
import com.example.shoply.presentation.screens.registerscreen.navigateToRegistrationScreen
import com.example.shoply.presentation.screens.registerscreen.registrationScreen
import com.example.shoply.presentation.screens.verificationscreen.navigateToVerificationScreen
import com.example.shoply.presentation.screens.verificationscreen.verificationScreen


fun NavGraphBuilder.loginNavGraph(
    navController: NavController
) {
    navigation<LoginNavGraph>(
        startDestination = LoginDestination
    ) {
        loginScreen(
            onLoginClick = {
                navController.navigate(MainNavGraph) {
                    popUpTo(LoginNavGraph) { inclusive = true }
                }
            },
            onCreateAccountClick = {
                navController.navigateToRegistrationScreen()
            },
            onGoogleLoginClick = {},
            onAppleLoginClick = {}
        )

        registrationScreen(
            onSignUpClick = {
                navController.navigateToVerificationScreen()
            },
            onBackButtonClick = {
                navController.popBackStack()
            }
        )

        verificationScreen(
            onBackClick = {
                navController.popBackStack()
            }
        )
    }
}
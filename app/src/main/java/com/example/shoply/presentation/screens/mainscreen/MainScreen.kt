package com.example.shoply.presentation.screens.mainscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shoply.presentation.components.ShoplyBottomBar
import com.example.shoply.presentation.components.ShoplyFab
import com.example.shoply.presentation.components.ShoplyTopBar
import com.example.shoply.presentation.components.dialogs.DialogHost
import com.example.shoply.presentation.components.dialogs.DialogLayout
import com.example.shoply.presentation.components.dialogs.DialogState
import com.example.shoply.presentation.screens.homescreen.HomeDestination
import com.example.shoply.presentation.screens.homescreen.homeScreen
import com.example.shoply.presentation.screens.homescreen.navigateToHomeScreen
import com.example.shoply.presentation.screens.productcatalogscreen.navigateToProductCatalogScreen
import com.example.shoply.presentation.screens.productcatalogscreen.productCatalogScreen
import com.myapp.shoply.R

@Composable
fun MainScreen(
    onLogoutClick: () -> Unit,
) {

    val dialogHost = remember { DialogHost() }
    val dialogState by dialogHost.state.collectAsState()

    val mainNavController = rememberNavController()
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val isHomeScreen = navBackStackEntry?.destination?.hasRoute<HomeDestination>() == true
    Box() {
        Scaffold(
            topBar = {
                ShoplyTopBar(
                    modifier = Modifier,
                    title = stringResource(R.string.app_name),
                    isHomeScreen = isHomeScreen,
                    onBackButtonClick = { mainNavController.popBackStack() },
                    onLogoutClick = onLogoutClick,
                    onSideMenuClick = {}
                )
            },
            floatingActionButton = {
                ShoplyFab(modifier = Modifier, onFabClick = {
                    if (isHomeScreen) {
                        // Handle FAB click on Home Screen
                    } else {
                        dialogHost.showDialog(
                            DialogState.InputDialog(
                                title = "Add Product",
                                message = "Please enter your product: ",
                                confirmButtonText = "Submit",
                                dismissButtonText = "Cancel",
                                onConfirm = { _, _ -> },
                                onDismiss = {}
                            )
                        )
                    }
                })
            },
            floatingActionButtonPosition = FabPosition.Center,
            bottomBar = {
                ShoplyBottomBar(
                    onHomeClick = { mainNavController.navigateToHomeScreen() },
                    onProductsClick = { mainNavController.navigateToProductCatalogScreen() },
                    onSettingsClick = { }
                )
            }
        ) { padding ->
            NavHost(
                modifier = Modifier.padding(padding),
                navController = mainNavController,
                startDestination = HomeDestination
            ) {
                homeScreen()
                productCatalogScreen()
            }
        }
    }
    DialogLayout(
        dialogState = dialogState,
        modifier = Modifier
    )
}
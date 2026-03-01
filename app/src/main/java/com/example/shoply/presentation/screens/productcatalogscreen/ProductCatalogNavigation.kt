package com.example.shoply.presentation.screens.productcatalogscreen

import FabConfig
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object ProductCatalogDestination

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.productCatalogScreen(
    onFabConfigChange: (FabConfig) -> Unit
) {
    composable<ProductCatalogDestination> {
        ProductCatalogScreen(
            modifier = Modifier,
            onFabConfigChange = onFabConfigChange
        )
    }
}

fun NavController.navigateToProductCatalogScreen() {
    navigate(ProductCatalogDestination) {
        launchSingleTop = true
    }
}
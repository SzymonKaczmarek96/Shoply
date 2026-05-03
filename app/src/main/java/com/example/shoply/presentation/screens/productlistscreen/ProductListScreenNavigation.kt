package com.example.shoply.presentation.screens.productlistscreen

import FabConfig
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import java.util.UUID


@Serializable
data class ProductListDestination(
    val listId: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.productListScreen(
    onFabConfigChange: (FabConfig) -> Unit,
    navigateToProductCatalog: () -> Unit,
) {
    composable<ProductListDestination> { backStackEntry ->
        val destination = backStackEntry.toRoute<ProductListDestination>()
        val listId = destination.listId?.let { UUID.fromString(it) }
        ProductListScreen(
            listId = listId,
            onFabClickChange = onFabConfigChange,
            onShoppingIconClick = navigateToProductCatalog,
        )
    }
}

fun NavController.navigateToProductListScreen(listId: UUID?) {
    navigate(route = ProductListDestination(listId = listId?.toString())) {
        launchSingleTop = true
    }
}

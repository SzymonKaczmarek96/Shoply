package com.example.shoply.presentation.screens.productcatalogscreen

import FabConfig
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.shoply.presentation.components.DropdownMenuConfig
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ProductCatalogDestination(
    val listId: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.productCatalogScreen(
    onFabConfigChange: (FabConfig) -> Unit,
    onNavigateBack: () -> Unit,
    showSpecialIcon: Boolean,
    onMenuConfigChange: (DropdownMenuConfig) -> Unit
) {
    composable<ProductCatalogDestination> { backStep ->
        val destination = backStep.toRoute<ProductCatalogDestination>()
        val listId = destination.listId?.let { UUID.fromString(it) }
        ProductCatalogScreen(
            modifier = Modifier,
            onFabConfigChange = onFabConfigChange,
            showSpecialIcon = showSpecialIcon,
            onNavigateBack = onNavigateBack,
            listId = listId,
            onMenuConfigChange = onMenuConfigChange
        )
    }
}

fun NavController.navigateToProductCatalogScreen(listId: UUID?) {
    navigate(route = ProductCatalogDestination(listId = listId.toString())) {
        launchSingleTop = true

    }
}
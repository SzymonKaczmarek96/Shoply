package com.example.shoply.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview


data class ShoplyBottomBarItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val isSelected: Boolean? = false,
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoplyBottomBar(
    isProductScreen: Boolean,
    isHomeScreen: Boolean,
    onHomeClick: () -> Unit,
    onProductsClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    val items = listOf(
        ShoplyBottomBarItem(
            title = "Home",
            icon = Icons.Default.Home,
            onClick = onHomeClick,
            isSelected = isHomeScreen
        ),
        ShoplyBottomBarItem(
            title = "Products",
            icon = Icons.Default.ShoppingCart,
            onClick = onProductsClick,
            isSelected = isProductScreen,
        ),
        ShoplyBottomBarItem(
            title = "Settings",
            icon = Icons.Default.Settings,
            onClick = onSettingsClick
        )
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = item.isSelected == true,
                onClick = {
                    item.onClick.invoke()
                },
                label = { Text(text = item.title) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                }
            )
        }
    }

}

@Preview
@Composable
fun ShoplyBottomBarPreview() {
    ShoplyBottomBar(
        isHomeScreen = true,
        isProductScreen = false,
        onHomeClick = {},
        onProductsClick = {},
        onSettingsClick = {}
    )
}
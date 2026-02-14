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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview


data class ShoplyBottomBarItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoplyBottomBar(
    onHomeClick: () -> Unit,
    onProductsClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val items = listOf(
        ShoplyBottomBarItem(
            title = "Home",
            icon = Icons.Default.Home,
            onClick = onHomeClick
        ),
        ShoplyBottomBarItem(
            title = "Products",
            icon = Icons.Default.ShoppingCart,
            onClick = onProductsClick
        ),
        ShoplyBottomBarItem(
            title = "Settings",
            icon = Icons.Default.Settings,
            onClick = onSettingsClick
        )
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    item.onClick.invoke()
                    selectedItemIndex = index
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
    ShoplyBottomBar(onHomeClick = {}, onProductsClick = {}, onSettingsClick = {})
}
package com.example.shoply.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoply.presentation.utils.UiDim
import com.myapp.shoply.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoplyTopBar(
    modifier: Modifier,
    title: String,
    isHomeScreen: Boolean = false,
    dropdownMenuItems: DropdownMenuConfig,
    onBackButtonClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    isLoginScreen: Boolean = false,
) {
    var expanded = remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
        navigationIcon = {
            IconButton(
                onClick = if (isHomeScreen) onLogoutClick else onBackButtonClick
            ) {
                Icon(
                    modifier = Modifier
                        .padding(start = UiDim.PADDING_SMALL)
                        .size(24.dp),
                    imageVector =
                        if (isHomeScreen) ImageVector.vectorResource(R.drawable.logout_icon)
                        else Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Icon",
                    tint = Color.Black,
                )
            }
        },
        modifier = modifier,
        actions = {
            if (!isLoginScreen) {
                IconButton(
                    onClick = { expanded.value = !expanded.value }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Side Menu Icon",
                        tint = Color.Black,
                    )
                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false },
                        content = {
                            DropdownSideMenuContent(
                                items = dropdownMenuItems.dropdownItems,
                                onExpandedChange = { expanded.value = false }
                            )
                        }
                    )
                }
            }
        },
    )
}

@Composable
private fun DropdownSideMenuContent(
    items: List<AppDropdownItem>,
    onExpandedChange: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = { Text(item.text) },
                onClick = {
                    item.onClick()
                    onExpandedChange.invoke()
                },
                trailingIcon = { Icon(item.icon, contentDescription = item.text) }
            )
        }
    }

}

@Preview
@Composable
private fun NavTopBarPreview() {
    ShoplyTopBar(
        modifier = Modifier,
        title = "Shoply",
        dropdownMenuItems = DropdownMenuConfig(),
    )
}

@Preview
@Composable
private fun DropdownSideMenuPreview() {
    DropdownSideMenuContent(
        items = listOf(
            AppDropdownItem(
                text = "Item 1",
                onClick = {},
                icon = Icons.Default.Favorite
            )
        ),
        onExpandedChange = {}
    )
}

data class AppDropdownItem(
    val text: String,
    val onClick: () -> Unit,
    val icon: ImageVector
)

data class DropdownMenuConfig(
    val dropdownItems: List<AppDropdownItem> = emptyList()
)
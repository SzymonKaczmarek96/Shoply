package com.example.shoply.presentation.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Toys
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.shoply.domain.model.ProductCategory

object ProductCategoryIconMapper {

    fun iconForCategory(productCategory: ProductCategory): ImageVector =
        when (productCategory) {
            ProductCategory.GROCERY -> Icons.Filled.ShoppingBag
            ProductCategory.ELECTRONICS -> Icons.Filled.Computer
            ProductCategory.CLOTHING -> Icons.Filled.Checkroom
            ProductCategory.HOUSEHOLD -> Icons.Filled.Home
            ProductCategory.TOYS -> Icons.Filled.Toys
            ProductCategory.SPORTS -> Icons.Filled.SportsSoccer
            ProductCategory.OTHER -> Icons.Filled.Category
        }
}
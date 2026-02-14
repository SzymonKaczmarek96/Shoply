package com.example.shoply.presentation.screens.productcatalogscreen

import com.example.shoply.domain.model.Product
import com.example.shoply.domain.model.ProductCategory

data class TestUseCase(
    val products: MutableList<Product> = mutableListOf(),
) {
    init {
        products.addAll(
            listOf(
                Product(
                    name = "Bread",
                    description = "Fresh bakery bread",
                    icon = android.R.drawable.ic_menu_crop,
                    category = ProductCategory.HOUSEHOLD
                ),
                Product(
                    name = "Milk",
                    description = "2% fat milk",
                    icon = android.R.drawable.ic_menu_gallery,
                    category = ProductCategory.SPORTS
                ),
                Product(
                    name = "Apples",
                    description = "Green apples",
                    icon = android.R.drawable.ic_menu_compass,
                    category = ProductCategory.OTHER,
                    isPurchased = true
                ),
                Product(
                    name = "Apples",
                    description = "Green apples",
                    icon = android.R.drawable.ic_menu_compass,
                    category = ProductCategory.OTHER,
                    isPurchased = true
                ),
                Product(
                    name = "Apples",
                    description = "Green apples",
                    icon = android.R.drawable.ic_menu_compass,
                    category = ProductCategory.OTHER,
                    isPurchased = true
                ), Product(
                    name = "Apples",
                    description = "Green apples",
                    icon = android.R.drawable.ic_menu_compass,
                    category = ProductCategory.OTHER,
                    isPurchased = true
                ),
                Product(
                    name = "Apples",
                    description = "Green apples",
                    icon = android.R.drawable.ic_menu_compass,
                    category = ProductCategory.OTHER,
                    isPurchased = true
                ),
                Product(
                    name = "Apples",
                    description = "Green apples",
                    icon = android.R.drawable.ic_menu_compass,
                    category = ProductCategory.OTHER,
                    isPurchased = true
                ),
                Product(
                    name = "Apples",
                    description = "Green apples",
                    icon = android.R.drawable.ic_menu_compass,
                    category = ProductCategory.OTHER,
                    isPurchased = true
                ),
                Product(
                    name = "Apples",
                    description = "Green apples",
                    icon = android.R.drawable.ic_menu_compass,
                    category = ProductCategory.OTHER,
                    isPurchased = true
                ), Product(
                    name = "Apples",
                    description = "Green apples",
                    icon = android.R.drawable.ic_menu_compass,
                    category = ProductCategory.OTHER,
                    isPurchased = true
                )
            )
        )
    }
}
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
                    category = ProductCategory.HOUSEHOLD
                ),
                Product(
                    name = "Milk",
                    category = ProductCategory.SPORTS
                ),
                Product(
                    name = "Apples",
                    category = ProductCategory.OTHER,
                    isPurchased = true
                ),
                Product(
                    name = "Apples",
                    category = ProductCategory.OTHER,
                    isPurchased = true
                ),
                Product(
                    name = "Apples",
                    category = ProductCategory.OTHER,
                    isPurchased = true
                ), Product(
                    name = "Apples",
                    category = ProductCategory.OTHER,
                    isPurchased = true
                ),
                Product(
                    name = "Apples",
                    category = ProductCategory.OTHER,
                    isPurchased = true
                ),
                Product(
                    name = "Apples",
                    category = ProductCategory.OTHER,
                    isPurchased = true
                ),
                Product(
                    name = "Apples",
                    category = ProductCategory.OTHER,
                    isPurchased = true
                ),
                Product(
                    name = "Apples",
                    category = ProductCategory.OTHER,
                    isPurchased = true
                ), Product(
                    name = "Apples",
                    category = ProductCategory.OTHER,
                    isPurchased = true
                )
            )
        )
    }
}
package com.example.shoply.domain.model

import java.util.UUID

data class Product(
    val uuid: UUID = UUID.randomUUID(),
    val name: String,
    val isPurchased: Boolean = false,
    val isSelected: Boolean = false,
    val category: ProductCategory = ProductCategory.OTHER
) {
}

enum class ProductCategory() {
    ALL,
    GROCERY,
    ELECTRONICS,
    CLOTHING,
    HOUSEHOLD,
    TOYS,
    SPORTS,
    OTHER
}

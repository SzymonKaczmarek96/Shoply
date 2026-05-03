package com.example.shoply.domain.model

import com.example.shoply.data.model.ProductEntity
import java.util.UUID

data class Product(
    val productId: UUID = UUID.randomUUID(),
    val name: String,
    val category: ProductCategory = ProductCategory.OTHER
)

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        productId = this.productId,
        name = this.name,
        category = this.category,
    )
}

enum class ProductCategory() {
    ALL,
    GROCERY,
    ELECTRONICS,
    CLOTHING,
    HOUSEHOLD,
    TOYS,
    SPORTS,
    OTHER;
}


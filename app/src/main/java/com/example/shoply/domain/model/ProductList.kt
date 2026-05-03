package com.example.shoply.domain.model

import com.example.shoply.data.model.ProductListEntity
import java.util.UUID

data class ProductList(
    val productListId: UUID = UUID.randomUUID(),
    val name: String,
    val products: List<ProductInList>,
    val members: List<User>,
) {
    val isComplete: Boolean
        get() = products.isNotEmpty() && products.all { it.isPurchased }

    val totalProducts: Int
        get() = products.size

    val purchasedProducts: Int
        get() = products.count { it.isPurchased }

    val totalQuantity: Int
        get() = products.sumOf { it.quantity }

    val purchasedQuantity: Int
        get() = products.filter { it.isPurchased }.sumOf { it.quantity }
}

fun ProductList.toEntity(): ProductListEntity {
    return ProductListEntity(
        productListId = this.productListId,
        name = this.name,
    )
}

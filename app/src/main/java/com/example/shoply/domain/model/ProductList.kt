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

    val totalQuantity: Int
        get() = products.count()

    val purchasedQuantity: Int
        get() = products.count { it.isPurchased }
}

fun ProductList.toEntity(): ProductListEntity {
    return ProductListEntity(
        productListId = this.productListId,
        name = this.name,
    )
}

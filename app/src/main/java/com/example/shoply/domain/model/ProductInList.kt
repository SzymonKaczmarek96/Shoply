package com.example.shoply.domain.model

import com.example.shoply.data.model.ProductInListEntity
import java.util.UUID

data class ProductInList(
    val id: UUID = UUID.randomUUID(),
    val productListId: UUID,
    val product: Product,
    val isPurchased: Boolean = false,
    val quantity: Int = 1,
)

fun ProductInList.toEntity(): ProductInListEntity {
    return ProductInListEntity(
        id = this.id,
        productListId = this.productListId,
        productId = this.product.productId,
        quantity = this.quantity,
        isPurchased = this.isPurchased,
    )
}

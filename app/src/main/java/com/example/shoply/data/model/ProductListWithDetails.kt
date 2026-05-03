package com.example.shoply.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.shoply.domain.model.ProductInList

data class ProductWithDetails(
    @Embedded val productInList: ProductInListEntity,

    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    val product: ProductEntity
)

fun ProductWithDetails.toDomain(): ProductInList {
    return ProductInList(
        id = this.productInList.id,
        productListId = this.productInList.productListId,
        product = this.product.toDomain(),
        quantity = this.productInList.quantity,
        isPurchased = this.productInList.isPurchased
    )
}

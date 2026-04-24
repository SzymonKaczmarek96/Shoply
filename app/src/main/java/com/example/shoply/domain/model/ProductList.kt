package com.example.shoply.domain.model

import com.example.shoply.data.model.ProductListEntity
import com.example.shoply.data.model.ProductListWithDetails
import java.util.UUID

data class ProductList(
    val productListId: UUID = UUID.randomUUID(),
    val name: String,
    val products: List<Product>,
    val members: List<User>,
) {
    val isComplete: Boolean
        get() = products.all { it.isPurchased }

    val quantityOfProducts: Int
        get() = products.size

    val quantityOfPurchasedProducts: Int
        get() = products.count { it.isPurchased }

}

fun ProductList.toEntity(): ProductListWithDetails {
    return ProductListWithDetails(
        productListEntity = ProductListEntity(
            productListId = this.productListId,
            name = this.name
        ),
        products = this.products.map { it.toEntity(this.productListId) },
        members = this.members.map { it.toEntity() },
    )
}

package com.example.shoply.domain.model

import java.util.UUID

data class ProductList(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val items: List<Product>,
    val members: List<User>,
) {
    val isComplete: Boolean
        get() = items.any { !it.isPurchased }

    val quantityOfProducts: Int
        get() = items.size

    val quantityOfPurchasedProducts: Int
        get() = items.count { it.isPurchased }
}

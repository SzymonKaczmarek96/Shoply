package com.example.shoply.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shoply.domain.model.Product
import com.example.shoply.domain.model.ProductCategory
import java.util.UUID

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val productId: UUID,
    val name: String,
    val isPurchased: Boolean,
    val category: ProductCategory,
    val productListId: UUID? = null
)

fun ProductEntity.toDomain(): Product {
    return Product(
        productId = this.productId,
        name = this.name,
        isPurchased = this.isPurchased,
        category = this.category,
    )
}

fun ProductEntity.assignToList(listId: UUID): ProductEntity {
    return this.copy(productListId = listId)
}
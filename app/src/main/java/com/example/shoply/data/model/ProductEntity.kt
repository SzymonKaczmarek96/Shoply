package com.example.shoply.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shoply.domain.model.Product
import com.example.shoply.domain.model.ProductCategory
import java.util.UUID

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val productId: UUID = UUID.randomUUID(),
    val name: String,
    val category: ProductCategory,
)

fun ProductEntity.toDomain(): Product {
    return Product(
        productId = this.productId,
        name = this.name,
        category = this.category,
    )
}
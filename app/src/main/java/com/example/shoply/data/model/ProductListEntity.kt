package com.example.shoply.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shoply.domain.model.ProductList
import java.util.UUID

@Entity(tableName = "product_lists")
data class ProductListEntity(
    @PrimaryKey
    val productListId: UUID,
    val name: String,
)

fun ProductListEntity.toDomain(): ProductList {
    return ProductList(
        productListId = this.productListId,
        name = this.name,
        products = emptyList(),
        members = emptyList()
    )
}


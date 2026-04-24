package com.example.shoply.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.shoply.domain.model.ProductList

data class ProductListWithDetails(
    @Embedded
    val productListEntity: ProductListEntity,

    @Relation(
        parentColumn = "productListId",
        entityColumn = "productListId"
    )
    val products: List<ProductEntity>,

    @Relation(
        parentColumn = "productListId",
        entityColumn = "userId",
        associateBy = Junction(ProductListMemberCrossReference::class),
    )
    val members: List<UserEntity>
)

fun ProductListWithDetails.toDomain(): ProductList {
    return ProductList(
        productListId = this.productListEntity.productListId,
        name = this.productListEntity.name,
        products = this.products.map { it.toDomain() },
        members = this.members.map { it.toDomain() }
    )
}

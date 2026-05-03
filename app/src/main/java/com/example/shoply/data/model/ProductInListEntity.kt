package com.example.shoply.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "products_in_lists",
    foreignKeys = [
        ForeignKey(
            entity = ProductListEntity::class,
            parentColumns = ["productListId"],
            childColumns = ["productListId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["productId"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("productListId"),
        Index("productId"),
        Index(value = ["productListId", "productId"], unique = true)
    ]
)
data class ProductInListEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val productListId: UUID,
    val productId: UUID,
    val quantity: Int = 1,
    val isPurchased: Boolean = false,
)
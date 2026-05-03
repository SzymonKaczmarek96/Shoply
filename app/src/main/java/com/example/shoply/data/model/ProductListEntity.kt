package com.example.shoply.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "product_lists",
    indices = [
        Index(value = ["name"], unique = true)
    ]
)
data class ProductListEntity(
    @PrimaryKey
    val productListId: UUID,
    val name: String,
)



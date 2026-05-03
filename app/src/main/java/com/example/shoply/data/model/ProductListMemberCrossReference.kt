package com.example.shoply.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.UUID

@Entity(
    tableName = "product_list_member_cross_ref",
    primaryKeys = ["productListId", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = ProductListEntity::class,
            parentColumns = ["productListId"],
            childColumns = ["productListId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProductListMemberCrossReference(
    val productListId: UUID,
    val userId: UUID
)

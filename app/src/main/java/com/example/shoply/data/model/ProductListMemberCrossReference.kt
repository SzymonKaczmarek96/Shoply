package com.example.shoply.data.model

import androidx.room.Entity
import java.util.UUID

@Entity(
    tableName = "product_list_member_cross_ref",
    primaryKeys = ["productListId", "userId"]
)
data class ProductListMemberCrossReference(
    val productListId: UUID,
    val userId: UUID
)

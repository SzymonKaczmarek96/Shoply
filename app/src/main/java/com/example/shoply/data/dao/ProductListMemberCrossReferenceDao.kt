package com.example.shoply.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.shoply.data.model.ProductListMemberCrossReference
import java.util.UUID

@Dao
interface ProductListMemberCrossReferenceDao {
    @Insert
    suspend fun addMemberToList(crossRef: ProductListMemberCrossReference)

    @Delete
    suspend fun removeMemberFromList(crossRef: ProductListMemberCrossReference)

    @Query("DELETE FROM product_list_member_cross_ref WHERE productListId = :listId")
    suspend fun removeAllMembersFromList(listId: UUID)

    @Query("DELETE FROM product_list_member_cross_ref WHERE userId = :userId")
    suspend fun removeUserFromAllLists(userId: UUID)
}
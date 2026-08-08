package com.example.shoply.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.shoply.data.model.UserEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query(
        """SELECT u.* FROM users u
        INNER JOIN product_list_member_cross_ref ref ON u.userId = ref.userId
          WHERE ref.productListId = :listId
          """
    )
    fun getMemberFromList(listId: UUID): Flow<List<UserEntity>>
}
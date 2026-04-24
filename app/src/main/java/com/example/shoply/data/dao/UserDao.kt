package com.example.shoply.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.shoply.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>
}
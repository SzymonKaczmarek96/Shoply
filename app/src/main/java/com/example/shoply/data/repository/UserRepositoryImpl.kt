package com.example.shoply.data.repository

import com.example.shoply.data.dao.UserDao
import com.example.shoply.domain.model.User
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {

    override fun getAllUsers(): Flow<List<User>> {
        return TODO("Provide the return value")
    }
}
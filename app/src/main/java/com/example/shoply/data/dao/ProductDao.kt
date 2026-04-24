package com.example.shoply.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.shoply.data.model.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Insert
    suspend fun insertProducts(products: List<ProductEntity>)
}
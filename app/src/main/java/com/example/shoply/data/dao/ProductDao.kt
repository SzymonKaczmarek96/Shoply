package com.example.shoply.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shoply.data.model.ProductEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Query("UPDATE products SET name = :name WHERE productId = :productId")
    suspend fun updateProduct(
        productId: UUID,
        name: String,
    )

    @Query("DELETE FROM products WHERE productId=:productId")
    suspend fun deleteProduct(productId: UUID)
}
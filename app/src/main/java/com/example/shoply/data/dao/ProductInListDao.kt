package com.example.shoply.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.shoply.data.model.ProductInListEntity
import com.example.shoply.data.model.ProductWithDetails
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ProductInListDao {
    @Transaction
    @Query(" SELECT * FROM products_in_lists WHERE productListId = :listId")
    fun getProductsForList(listId: UUID): Flow<List<ProductWithDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductInListEntity)

    @Update
    suspend fun updateProduct(product: ProductInListEntity)

    @Delete
    suspend fun deleteProduct(product: ProductInListEntity)
}
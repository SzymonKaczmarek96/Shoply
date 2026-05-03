package com.example.shoply.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.shoply.data.model.ProductListEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ProductListDao {

    // ========================================
    // Queries
    // ========================================

    @Query("SELECT * FROM product_lists")
    fun getAllProductLists(): Flow<List<ProductListEntity>>

    @Query("SELECT * FROM product_lists WHERE productListId = :listId")
    suspend fun getProductListById(listId: UUID): ProductListEntity?

    @Query("SELECT * FROM product_lists WHERE LOWER(name) = LOWER(:name)")
    suspend fun getProductListByName(name: String): ProductListEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM product_lists WHERE LOWER(name) = LOWER(:name))")
    suspend fun existsWithName(name: String): Boolean

    @Query("SELECT COUNT(*) FROM product_lists")
    suspend fun getProductListCount(): Int

    // ========================================
    // Insert/Update/Delete
    // ========================================

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertProductList(productList: ProductListEntity)

    @Update
    suspend fun updateProductList(productList: ProductListEntity)

    @Delete
    suspend fun deleteProductList(productList: ProductListEntity)

    @Query("DELETE FROM product_lists WHERE productListId = :listId")
    suspend fun deleteProductListById(listId: UUID)
}
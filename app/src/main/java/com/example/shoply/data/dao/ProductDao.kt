package com.example.shoply.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.shoply.data.model.ProductEntity
import com.example.shoply.domain.model.ProductCategory
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE name =:name")
    fun getProductByName(name: String): ProductEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Query("SELECT * FROM products WHERE productId = :productId")
    suspend fun getProductById(productId: UUID): ProductEntity?

    @Query("UPDATE products SET name = :name, category = :category WHERE productId = :productId")
    suspend fun updateProduct(
        productId: UUID,
        name: String,
        category: String,
    )

    @Query("DELETE FROM products WHERE productId=:productId")
    suspend fun deleteProduct(productId: UUID)

    @Transaction
    suspend fun getOrCreate(product: ProductEntity): ProductEntity {
        val existingProduct = getProductByName(product.name)
        if (existingProduct != null) {
            return existingProduct
        }

        insertProduct(product)
        return getProductByName(product.name)
    }

    @Query("SELECT EXISTS(SELECT 1 FROM products WHERE LOWER(name) = LOWER(:name))")
    suspend fun existsByName(name: String): Boolean

    @Query("UPDATE products SET category = :category WHERE productId = :productId")
    suspend fun updateProductCategory(category: ProductCategory, productId: UUID)
}
package com.example.shoply.data.repository

import com.example.shoply.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>

    suspend fun addProducts(vararg products: Product)
}
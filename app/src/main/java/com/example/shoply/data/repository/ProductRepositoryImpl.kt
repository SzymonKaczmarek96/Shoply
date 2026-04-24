package com.example.shoply.data.repository

import com.example.shoply.data.dao.ProductDao
import com.example.shoply.data.model.toDomain
import com.example.shoply.domain.model.Product
import com.example.shoply.domain.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val productDao: ProductDao
) : ProductRepository {
    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addProducts(vararg products: Product) {
        val productsEntity = products.map { it.toEntity() }
        productDao.insertProducts(productsEntity)
    }


}
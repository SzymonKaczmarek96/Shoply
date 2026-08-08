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

    override suspend fun updateProducts(product: Product) {
        val productEntity = product.toEntity()
        productDao.updateProduct(
            productId = productEntity.productId,
            name = productEntity.name,
            category = productEntity.category,
        )
    }

    override suspend fun deleteSelectedProducts(products: List<Product>) {
        val productsEntity = products.map { it.toEntity() }
        productsEntity.forEach {
            productDao.deleteProduct(it.productId)
        }
    }

    override suspend fun getOrCreateProduct(product: Product): Product {
        return productDao.getOrCreate(product.toEntity()).toDomain()
    }

    override suspend fun existsByName(name: String): Boolean {
        return productDao.existsByName(name)
    }


}
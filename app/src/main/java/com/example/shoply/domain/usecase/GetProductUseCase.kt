package com.example.shoply.domain.usecase

import com.example.shoply.data.repository.ProductRepository
import com.example.shoply.domain.model.Product
import kotlinx.coroutines.flow.Flow

class GetProductUseCase(
    private val productRepository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return productRepository.getAllProducts()
    }
}
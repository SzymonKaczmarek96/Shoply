package com.example.shoply.domain.usecase.product

import com.example.shoply.data.repository.ProductRepository
import com.example.shoply.domain.model.Product

class GetOrCreateProductUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product): Product {
        return productRepository.getOrCreateProduct(product)
    }
}
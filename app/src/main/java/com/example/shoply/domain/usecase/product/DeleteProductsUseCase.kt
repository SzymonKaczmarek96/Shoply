package com.example.shoply.domain.usecase.product

import com.example.shoply.data.repository.ProductRepository
import com.example.shoply.domain.model.Product
import com.example.shoply.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.first

class DeleteProductsUseCase(
    private val productRepository: ProductRepository,
    private val getProductUseCase: GetProductUseCase
) {
    suspend operator fun invoke(
        products: List<Product>
    ): UseCaseResult<String, String> {
        if (products.isEmpty()) {
            return UseCaseResult.Error("No products selected for deletion")
        }

        if (!matchedProducts(products, getProductUseCase.invoke().first())) {
            return UseCaseResult.Error("One or more selected products not found")
        }

        productRepository.deleteSelectedProducts(products)
        return UseCaseResult.Success("Products deleted successfully")
    }

    private fun matchedProducts(
        productsForDelete: List<Product>,
        getAllProducts: List<Product>
    ): Boolean {
        return productsForDelete.all { product ->
            getAllProducts.contains(product)
        }

    }
}
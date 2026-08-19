package com.example.shoply.domain.usecase.product

import com.example.shoply.data.repository.ProductRepository
import com.example.shoply.domain.model.Product
import com.example.shoply.domain.usecase.UseCaseResult
import okio.IOException
import java.sql.SQLException

class ChangeProductsCategoryUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(products: List<Product>): UseCaseResult<Unit, Throwable> {
        return try {
            val result = productRepository.updateCategories(products)
            UseCaseResult.Success(result)
        } catch (e: IOException) {
            UseCaseResult.Error(e)
        } catch (e: SQLException) {
            UseCaseResult.Error(e)
        }
    }
}
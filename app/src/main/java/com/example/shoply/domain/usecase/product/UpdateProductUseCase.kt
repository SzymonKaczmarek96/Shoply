package com.example.shoply.domain.usecase.product

import com.example.shoply.data.repository.ProductRepository
import com.example.shoply.domain.error.productserror.ProductNameValidationException
import com.example.shoply.domain.model.Product
import com.example.shoply.domain.usecase.UseCaseResult
import okio.IOException
import java.sql.SQLException

class UpdateProductUseCase(
    private val productRepository: ProductRepository,
    private val validateProductNameUseCase: ValidateProductNameUseCase
) {
    suspend operator fun invoke(product: Product): UseCaseResult<Unit, Throwable> {
        return try {
            validateProductNameUseCase.invoke(product.name)
            val result = productRepository.updateProducts(product = product)
            UseCaseResult.Success(result)
        } catch (e: IOException) {
            UseCaseResult.Error(e)
        } catch (e: SQLException) {
            UseCaseResult.Error(e)
        } catch (e: ProductNameValidationException) {
            UseCaseResult.Error(e)
        }
    }
}
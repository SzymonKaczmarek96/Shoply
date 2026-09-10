package com.example.shoply.domain.usecase.productlist

import com.example.shoply.data.repository.ProductListRepository
import com.example.shoply.domain.model.ProductList
import com.example.shoply.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.first
import okio.IOException
import java.sql.SQLException

class AddProductListUseCase(
    private val productListRepository: ProductListRepository
) {
    suspend operator fun invoke(productList: ProductList): UseCaseResult<Unit, String> {
        val existingLists = productListRepository.getAllProductList().first()

        val isDuplicate = existingLists.any { list ->
            list.name.equals(productList.name.trim(), ignoreCase = true)
        }

        if (isDuplicate) {
            throw IllegalArgumentException("Product list with name '${productList.name}' already exists")
        }
        return try {
            UseCaseResult.Success(productListRepository.addProductList(productList))
        } catch (e: IllegalArgumentException) {
            UseCaseResult.Error(e.message ?: "Something went wrong")
        } catch (e: IOException) {
            UseCaseResult.Error(e.message ?: "Something went wrong")
        } catch (e: SQLException) {
            UseCaseResult.Error(e.message ?: "Something went wrong")
        }
    }
}

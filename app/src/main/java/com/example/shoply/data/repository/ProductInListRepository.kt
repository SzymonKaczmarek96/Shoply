package com.example.shoply.data.repository

import com.example.shoply.domain.model.ProductInList
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ProductInListRepository {

    fun getAllProductInList(listId: UUID): Flow<List<ProductInList>>

    suspend fun addProductInList(productInList: ProductInList)

    suspend fun addProductsInList(productsInList: List<ProductInList>)

    suspend fun deleteProductInList(productInList: ProductInList)

    suspend fun updateProductInList(productInList: ProductInList)

    suspend fun deleteProductsInList(productsInList: List<ProductInList>)

    suspend fun findContainingProducts(listId: UUID, letter: String): List<ProductInList>

    suspend fun isExistsProductInList(listId: UUID, productName: String): Boolean

}
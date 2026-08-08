package com.example.shoply.data.repository

import com.example.shoply.domain.model.ProductList
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ProductListRepository {

    fun getAllProductList(): Flow<List<ProductList>>

    fun getAllProductListsWithDetails(): Flow<List<ProductList>>

    suspend fun getProductListWithDetails(listId: UUID): ProductList?

    suspend fun addProductList(productList: ProductList)

    suspend fun updateProductListName(listId: UUID, newName: String)

    suspend fun deleteProductList(productList: ProductList)

}
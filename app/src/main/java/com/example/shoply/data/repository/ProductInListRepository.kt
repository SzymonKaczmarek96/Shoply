package com.example.shoply.data.repository

import com.example.shoply.domain.model.ProductInList
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ProductInListRepository {

    fun getAllProductInList(listId: UUID): Flow<List<ProductInList>>

    suspend fun addProductInList(productInList: ProductInList)

    suspend fun deleteProductInList(listId: UUID, productInList: ProductInList)

}
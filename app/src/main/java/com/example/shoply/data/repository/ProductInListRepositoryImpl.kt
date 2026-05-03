package com.example.shoply.data.repository

import com.example.shoply.data.dao.ProductInListDao
import com.example.shoply.data.model.toDomain
import com.example.shoply.domain.model.ProductInList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class ProductInListRepositoryImpl(
    private val productInListDao: ProductInListDao
) : ProductInListRepository {

    override fun getAllProductInList(listId: UUID): Flow<List<ProductInList>> {
        return productInListDao.getProductsForList(listId)
            .map { flowList -> flowList.map { it.toDomain() } }
    }

    override suspend fun addProductInList(
        listId: UUID,
        productInList: ProductInList
    ) {
    }

    override suspend fun deleteProductInList(
        listId: UUID,
        productInList: ProductInList
    ) {
        TODO("Not yet implemented")
    }
}
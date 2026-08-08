package com.example.shoply.data.repository

import com.example.shoply.data.dao.ProductInListDao
import com.example.shoply.data.model.toDomain
import com.example.shoply.domain.model.ProductInList
import com.example.shoply.domain.model.toEntity
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

    override suspend fun addProductInList(productInList: ProductInList) {
        productInListDao.insertProduct(productInList.toEntity())
    }

    override suspend fun addProductsInList(productsInList: List<ProductInList>) {
        productInListDao.insertProducts(productsInList.map { it.toEntity() })
    }

    override suspend fun deleteProductInList(productInList: ProductInList) {
        productInListDao.deleteProduct(productInList.toEntity())
    }

    override suspend fun updateProductInList(productInList: ProductInList) {
        productInListDao.updateProduct(productInList.toEntity())
    }

    override suspend fun deleteProductsInList(productsInList: List<ProductInList>) {
        productInListDao.deleteProducts(productsInList.map { it.toEntity() })
    }

    override suspend fun findContainingProducts(listId: UUID, letter: String): List<ProductInList> {
        return productInListDao.findContainingProducts(
            listId = listId,
            query = letter
        ).map { it.toDomain() }
    }

    override suspend fun isExistsProductInList(
        listId: UUID,
        productName: String
    ): Boolean {
        return productInListDao.existsProduct(listId, productName)
    }


}
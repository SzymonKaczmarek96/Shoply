package com.example.shoply.data.repository

import com.example.shoply.data.dao.ProductListDao
import com.example.shoply.domain.model.ProductList
import com.example.shoply.domain.model.User
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class ProductListRepositoryImpl(
    private val productListDao: ProductListDao
) : ProductListRepository {
    override fun getAllProductList(): Flow<List<ProductList>> {
        TODO("Not yet implemented")
    }


    override fun getAllProductListsWithDetails(): Flow<List<ProductList>> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductListWithDetails(listId: UUID): ProductList? {
        TODO("Not yet implemented")
    }

    override suspend fun addProductList(productList: ProductList) {
        TODO("Not yet implemented")
    }


    override suspend fun updateProductListName(listId: UUID, newName: String) {
        val list = productListDao.getProductListById(listId)
        list?.let {
            productListDao.updateProductList(it.copy(name = newName))
        }
    }

    override suspend fun deleteProductList(productList: ProductList) {
        TODO("Not yet implemented")
    }

    override suspend fun addMemberToList(
        listId: UUID,
        member: User
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun removeMemberFromList(listId: UUID) {
        TODO("Not yet implemented")
    }
}
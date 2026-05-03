package com.example.shoply.data.repository

import com.example.shoply.data.dao.ProductListDao
import com.example.shoply.data.model.ProductListEntity
import com.example.shoply.data.model.ProductListMemberCrossReference
import com.example.shoply.data.model.toDomain
import com.example.shoply.domain.model.ProductList
import com.example.shoply.domain.model.User
import com.example.shoply.domain.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class ProductListRepositoryImpl(
    private val productListDao: ProductListDao
) : ProductListRepository {

    override fun getAllProductList(): Flow<List<ProductList>> {
        return productListDao.getAllProductList().map { entries ->
            entries.map { it.toDomain() }
        }
    }

    override fun getAllProductListsWithDetails(): Flow<List<ProductList>> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductListWithDetails(listId: UUID): ProductList? {
        TODO("Not yet implemented")
    }

    override suspend fun addProductList(productList: ProductList) {
        productListDao.insertProductList(
            ProductListEntity(
                productListId = productList.productListId,
                name = productList.name,
            )
        )
        val memberEntity = productList.members.map { it.toEntity() }
        productListDao.insertMember(memberEntity.first())

        val crossReferences = productList.members.map { member ->
            ProductListMemberCrossReference(
                productListId = productList.productListId,
                userId = member.userId
            )
        }
        productListDao.insertMemberCrossReferences(crossReferences)
    }

    override suspend fun updateProductListName(listId: UUID, newName: String) {
        val list = productListDao.getProductListById(listId)
        list?.let {
            productListDao.updateProductList(it.copy(name = newName))
        }
    }

    override suspend fun deleteProductList(productList: ProductList) {
        productListDao.deleteProductList(
            ProductListEntity(
                productListId = productList.productListId,
                name = productList.name
            )
        )
        productListDao.removeMemberFromList(productList.productListId)
    }

    override suspend fun addMemberToList(
        listId: UUID,
        member: User
    ) {
        productListDao.insertMember(member.toEntity())

        productListDao.insertMemberCrossReference(
            ProductListMemberCrossReference(
                productListId = listId,
                userId = member.userId
            )
        )
    }

    override suspend fun removeMemberFromList(listId: UUID) {
        productListDao.removeMemberFromList(listId)
    }

}
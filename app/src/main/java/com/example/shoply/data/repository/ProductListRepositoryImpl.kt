package com.example.shoply.data.repository

import com.example.shoply.data.dao.ProductInListDao
import com.example.shoply.data.dao.ProductListDao
import com.example.shoply.data.dao.UserDao
import com.example.shoply.data.model.toDomain
import com.example.shoply.domain.model.ProductList
import com.example.shoply.domain.model.toEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.UUID

class ProductListRepositoryImpl(
    private val productListDao: ProductListDao,
    private val productInListDao: ProductInListDao,
    private val userDao: UserDao,
) : ProductListRepository {
    override fun getAllProductList(): Flow<List<ProductList>> {
        return productListDao.getAllProductLists().map { entities ->
            entities.map {
                ProductList(
                    productListId = it.productListId,
                    name = it.name,
                    products = emptyList(),
                    members = emptyList()
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllProductListsWithDetails(): Flow<List<ProductList>> {
        return productListDao.getAllProductLists()
            .flatMapLatest { listEntities ->
                if (listEntities.isEmpty()) {
                    return@flatMapLatest flowOf(emptyList())
                }
                val flows = listEntities.map { listEntity ->
                    combine(
                        productInListDao.getProductsForList(listEntity.productListId),
                        userDao.getMemberFromList(listEntity.productListId)
                    ) { products, members ->
                        ProductList(
                            productListId = listEntity.productListId,
                            name = listEntity.name,
                            products = products.map { it.toDomain() },
                            members = members.map { it.toDomain() }
                        )
                    }
                }

                combine(flows) { productLists ->
                    productLists.toList()
                }
            }
    }

    override suspend fun getProductListWithDetails(listId: UUID): ProductList? {
        val productList = productListDao.getProductListById(listId)
        return ProductList(
            productListId = productList?.productListId ?: UUID.fromString(""),
            name = productList?.name ?: "",
            products = productInListDao.getProductsForList(listId).first().map { it.toDomain() },
            members = userDao.getMemberFromList(listId).first().map { it.toDomain() }
        )
    }

    override suspend fun addProductList(productList: ProductList) {
        return productListDao.insertProductList(productList.toEntity())
    }

    override suspend fun updateProductListName(listId: UUID, newName: String) {
        val isExisting = productListDao.getProductListById(listId)
        if (isExisting != null && isExisting.productListId != listId) {
            throw IllegalArgumentException("Product list '$newName' already exists")
        }
        val list = productListDao.getProductListById(listId)
        list?.let {
            productListDao.updateProductList(it.copy(name = newName))
        }
    }

    override suspend fun deleteProductList(productList: ProductList) {
        productListDao.deleteProductList(productList.toEntity())
    }
}
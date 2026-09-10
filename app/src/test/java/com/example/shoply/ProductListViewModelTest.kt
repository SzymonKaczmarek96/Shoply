package com.example.shoply

import com.example.shoply.domain.model.Product
import com.example.shoply.domain.model.ProductCategory
import com.example.shoply.domain.model.ProductInList
import com.example.shoply.domain.model.ProductList
import com.example.shoply.domain.model.Role
import com.example.shoply.domain.model.User
import com.example.shoply.domain.usecase.UseCaseResult
import com.example.shoply.domain.usecase.product.GetOrCreateProductUseCase
import com.example.shoply.domain.usecase.productinlist.AddProductInListUseCase
import com.example.shoply.domain.usecase.productinlist.DeleteProductInListUseCase
import com.example.shoply.domain.usecase.productinlist.DeleteProductsInListUseCase
import com.example.shoply.domain.usecase.productinlist.FindProductsInListUseCase
import com.example.shoply.domain.usecase.productinlist.GetProductInListUseCase
import com.example.shoply.domain.usecase.productinlist.UpdateProductInListUseCase
import com.example.shoply.domain.usecase.productlist.GetProductListsWithDetailsUseCase
import com.example.shoply.presentation.screens.productlistscreen.ProductListScreenViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModelTest {

    @get:Rule
    val mainDispatcherRule: MainDispatcherRule = MainDispatcherRule()

    private val getOrCreateProductUseCase: GetOrCreateProductUseCase = mockk()
    private val getProductInList: GetProductInListUseCase = mockk()
    private val addProductInListUseCase: AddProductInListUseCase = mockk()
    private val deleteProductInListUseCase: DeleteProductInListUseCase = mockk()
    private val updateProductInListUseCase: UpdateProductInListUseCase = mockk()
    private val getProductListsWithDetailsUseCase: GetProductListsWithDetailsUseCase = mockk()
    private val deleteProductsInListUseCase: DeleteProductsInListUseCase = mockk()
    private val findProductsInListUseCase: FindProductsInListUseCase = mockk()

    private fun createViewModel(
        stateInit: ProductListScreenViewModel.State = ProductListScreenViewModel.State()
    ) = ProductListScreenViewModel(
        getOrCreateProductUseCase = getOrCreateProductUseCase,
        getProductInList = getProductInList,
        addProductInListUseCase = addProductInListUseCase,
        deleteProductInListUseCase = deleteProductInListUseCase,
        updateProductInListUseCase = updateProductInListUseCase,
        getProductListsWithDetailsUseCase = getProductListsWithDetailsUseCase,
        deleteProductsInListUseCase = deleteProductsInListUseCase,
        findProductsInListUseCase = findProductsInListUseCase,
        dispatcher = mainDispatcherRule.testDispatcher,
        stateInit = stateInit
    )

    @Test
    fun shouldUpdateListId() {
        //given
        val viewModel = createViewModel(
            stateInit =
                ProductListScreenViewModel.State(
                    productCategories = ProductCategory.entries
                )
        )
        val listId = UUID.fromString("12345678-1234-1234-1234-123456789012")

        //when
        viewModel.updateListId(listId)

        //then
        val state = viewModel.state.value
        assertEquals(listId, state.listId)
    }

    @Test
    fun shouldNotUpdateListIdWhenIdIsSame() {
        //given
        val viewModel = createViewModel(
            stateInit =
                ProductListScreenViewModel.State(
                    productCategories = ProductCategory.entries,
                    listId = UUID.fromString("12345678-1234-1234-1234-123456789012")
                )
        )
        val listId = UUID.fromString("12345678-1234-1234-1234-123456789012")
        val stateBefore = viewModel.state.value

        //when
        viewModel.updateListId(listId)

        //then
        val stateAfter = viewModel.state.value
        assertEquals(stateAfter, stateBefore)
    }

    @Test
    fun shouldNotUpdateListIdWhenIdIsNull() {
        //given
        val viewModel = createViewModel(
            stateInit =
                ProductListScreenViewModel.State(
                    productCategories = ProductCategory.entries,
                )
        )
        val stateBefore = viewModel.state.value

        //when
        viewModel.updateListId(null)

        //then
        val stateAfter = viewModel.state.value
        assertEquals(stateAfter, stateBefore)
    }

    @Test
    fun shouldUpdateProductInList() = runTest {
        //given
        val viewModel = createViewModel(
            stateInit =
                ProductListScreenViewModel.State(
                    productCategories = ProductCategory.entries,
                    listId = UUID.fromString("12345678-1234-1234-1234-123456789012"),
                    allProducts = testProductInList
                )
        )
        val expectedProduct = testProductInList[0].copy(isPurchased = true)
        val productId = UUID.fromString("12345678-1234-1234-1234-123456789011")
        coEvery { updateProductInListUseCase.invoke(expectedProduct) } returns UseCaseResult.Success(
            Unit
        )
        coEvery { getProductListsWithDetailsUseCase.invoke() } returns flowOf(
            createTestProductListWithAllPurchasedProducts()
        )
        //when
        viewModel.updatePurchasedStatusProductInList(productId)
        advanceUntilIdle()
        //then
        val state = viewModel.state.value
        assertEquals(state.allProducts?.size, 2)
        assertEquals(state.allProducts?.get(0)?.isPurchased, true)
        assertEquals(state.isSuccess, true)
    }

    @Test
    fun shouldThrowExceptionWhenUpdateProductInListDoesNotWork() = runTest {
        //given
        val viewModel = createViewModel(
            stateInit =
                ProductListScreenViewModel.State(
                    productCategories = ProductCategory.entries,
                    listId = UUID.fromString("12345678-1234-1234-1234-123456789012"),
                    allProducts = testProductInList
                )
        )
        val expectedProduct = testProductInList[0].copy(isPurchased = true)
        val productId = UUID.fromString("12345678-1234-1234-1234-123456789011")
        coEvery { updateProductInListUseCase.invoke(expectedProduct) } returns
                UseCaseResult.Error(Exception("Illegal argument"))
        coEvery { getProductListsWithDetailsUseCase.invoke() } returns flowOf(
            createTestProductListWithAllPurchasedProducts()
        )

        //when
        viewModel.updatePurchasedStatusProductInList(productId)
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(state.isError, true)
        assertEquals(state.userMessage, "Illegal argument")
    }

    @Test
    fun shouldDeleteProductInList() = runTest {

    }


    private val testProduct1 = Product(
        productId = UUID.fromString("12345678-1234-1234-1234-123456789014"),
        name = "Test Product",
        category = ProductCategory.GROCERY
    )

    private val testProduct2 =
        Product(
            productId = UUID.fromString("12345678-1234-1234-1234-123456789012"),
            name = "Another Product",
            category = ProductCategory.ELECTRONICS
        )


    private val testProductInList = listOf(
        ProductInList(
            id = UUID.fromString("12345678-1234-1234-1234-123456789011"),
            productListId = UUID.fromString("12345678-1234-1234-1234-123456789014"),
            product = testProduct1,
            isPurchased = false,
            quantity = 1
        ),
        ProductInList(
            id = UUID.fromString("12345678-1234-1234-1234-123456789012"),
            productListId = UUID.fromString("12345678-1234-1234-1234-123456789012"),
            product = testProduct2,
            isPurchased = true,
            quantity = 1
        )
    )
    private val testProductInListWithAllPurchasedProducts = listOf(
        ProductInList(
            id = UUID.fromString("12345678-1234-1234-1234-123456789011"),
            productListId = UUID.fromString("12345678-1234-1234-1234-123456789014"),
            product = testProduct1,
            isPurchased = true,
            quantity = 1
        ),
        ProductInList(
            id = UUID.fromString("12345678-1234-1234-1234-123456789012"),
            productListId = UUID.fromString("12345678-1234-1234-1234-123456789012"),
            product = testProduct2,
            isPurchased = true,
            quantity = 1
        )
    )


    private fun createTestProductList() = listOf(
        ProductList(
            productListId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            name = "Test Product List",
            products = testProductInList,
            members = listOf(testUser)
        ),
    )

    private fun createTestProductListWithAllPurchasedProducts() =
        listOf(
            ProductList(
                productListId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                name = "Test Product List",
                products = testProductInListWithAllPurchasedProducts,
                members = listOf(testUser)
            ),
        )


    private val testUser = User(
        userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174888"),
        name = "Test User",
        email = "testuser@example.com",
        role = Role.CREATOR
    )

    private val testProduct = Product(
        productId = UUID.fromString("12345678-1234-1234-1234-123456789014"),
        name = "Test Product",
        category = ProductCategory.GROCERY
    )


}
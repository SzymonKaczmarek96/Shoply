package com.example.shoply

import com.example.shoply.domain.model.Product
import com.example.shoply.domain.model.ProductCategory
import com.example.shoply.domain.model.ProductInList
import com.example.shoply.domain.model.ProductList
import com.example.shoply.domain.model.Role
import com.example.shoply.domain.model.User
import com.example.shoply.domain.usecase.UseCaseResult
import com.example.shoply.domain.usecase.productlist.AddProductListUseCase
import com.example.shoply.domain.usecase.productlist.DeleteProductListUseCase
import com.example.shoply.domain.usecase.productlist.GetProductListsWithDetailsUseCase
import com.example.shoply.presentation.components.dialogs.UiDialog
import com.example.shoply.presentation.screens.homescreen.HomeScreenViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule()
    val mainDispatcherRule = MainDispatcherRule()
    private val addProductListUseCase: AddProductListUseCase = mockk()
    private val deleteProductListUseCase: DeleteProductListUseCase = mockk()
    private val getProductListsWithDetailsUseCase: GetProductListsWithDetailsUseCase = mockk()

    private fun createViewModel(
        state: HomeScreenViewModel.State
    ) = HomeScreenViewModel(
        addProductListUseCase = addProductListUseCase,
        deleteProductListUseCase = deleteProductListUseCase,
        getProductListsWithDetailsUseCase = getProductListsWithDetailsUseCase,
        dispatcherId = mainDispatcherRule.testDispatcher,
        stateInit = state
    )

    @Before
    fun init() {
        every { getProductListsWithDetailsUseCase.invoke() } returns flowOf(createTestProductList())
    }

    @Test
    fun shouldInitGetProductListsWithDetails() = runTest {
        //given
        val viewModel = createViewModel(
            HomeScreenViewModel.State(
                shopList = createTestProductList()
            )
        )

        //when then
        val state = viewModel.state.value
        assertEquals(createTestProductList(), state.shopList)
    }

    @Test
    fun shouldChangeActiveDialogStatus() {
        //given
        val viewModel = createViewModel(
            HomeScreenViewModel.State(
                shopList = createTestProductList(),
                activeDialog = UiDialog.NONE
            )
        )

        //when
        viewModel.onCreateListClick()

        //then
        val state = viewModel.state.value
        assertEquals(UiDialog.INPUT_DIALOG, state.activeDialog)
    }

    @Test
    fun shouldChangeDialogInput() {
        //given
        val viewModel = createViewModel(
            HomeScreenViewModel.State(
                shopList = createTestProductList(),
                activeDialog = UiDialog.NONE,
                dialogError = "Error"
            )
        )

        //when
        viewModel.onDialogInputChange("Success")

        //then
        val state = viewModel.state.value
        assertNull(state.dialogError)
        assertEquals("Success", state.dialogInput)
    }

    @Test
    fun shouldCreateListWhenDialogInputIsValid() = runTest {
        //given
        val viewModel = createViewModel(
            HomeScreenViewModel.State(
                shopList = createTestProductList(),
                activeDialog = UiDialog.INPUT_DIALOG,
                dialogInput = "New list"
            )
        )
        coEvery { addProductListUseCase.invoke(any<ProductList>()) } returns UseCaseResult.Success(
            Unit
        )

        //when
        viewModel.onDialogConfirm()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertTrue(state.isSuccess)
        assertEquals("New list has been added successfully", state.userMessage)
        coVerify(exactly = 1) { addProductListUseCase.invoke(any<ProductList>()) }
    }

    @Test
    fun shouldBlockCreatingDuplicateList() = runTest {
        //given
        val viewModel = createViewModel(
            HomeScreenViewModel.State(
                shopList = createTestProductList(),
                activeDialog = UiDialog.INPUT_DIALOG,
                dialogInput = "Test Product List"
            )
        )

        //when
        viewModel.onDialogConfirm()

        //then
        val state = viewModel.state.value
        assertEquals("A list with this name already exists", state.dialogError)
        coVerify(exactly = 0) { addProductListUseCase.invoke(any<ProductList>()) }
    }

    @Test
    fun shouldDoNothingWhenDialogInputIsMessageType() = runTest {
        //given
        val viewModel = createViewModel(
            HomeScreenViewModel.State(
                shopList = createTestProductList(),
                activeDialog = UiDialog.MESSAGE_DIALOG,
            )
        )

        //when
        viewModel.onDialogConfirm()

        //then
        val state = viewModel.state.value
        assertFalse(state.isSuccess)
    }

    @Test
    fun shouldDoNothingWhenDialogInputIsNone() = runTest {
        //given
        val viewModel = createViewModel(
            HomeScreenViewModel.State(
                shopList = createTestProductList(),
                activeDialog = UiDialog.NONE,
            )
        )

        //when
        viewModel.onDialogConfirm()

        //then
        val state = viewModel.state.value
        assertFalse(state.isSuccess)
    }

    @Test
    fun shouldDismissDialogAndResetInputs() = runTest {
        //given
        val viewModel = createViewModel(
            HomeScreenViewModel.State(
                shopList = createTestProductList(),
                activeDialog = UiDialog.INPUT_DIALOG,
                dialogInput = "Draft list",
                dialogError = "Error"
            )
        )

        //when
        viewModel.onDialogDismiss()

        //then
        val state = viewModel.state.value
        assertEquals(UiDialog.NONE, state.activeDialog)
        assertEquals("", state.dialogInput)
        assertNull(state.dialogError)
    }

    @Test
    fun shouldClearUserMessage() = runTest {
        //given
        val viewModel = createViewModel(
            HomeScreenViewModel.State(
                shopList = createTestProductList(),
                userMessage = "Visible message"
            )
        )

        //when
        viewModel.onMessageShown()

        //then
        assertNull(viewModel.state.value.userMessage)
    }

    @Test
    fun shouldDeleteShopListWhenUseCaseSucceeds() = runTest {
        //given
        val productList = createTestProductList().first()
        val viewModel = createViewModel(
            HomeScreenViewModel.State(
                shopList = createTestProductList()
            )
        )
        coEvery { deleteProductListUseCase.invoke(productList) } returns UseCaseResult.Success(Unit)

        //when
        viewModel.deleteShopList(productList.productListId)
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertTrue(state.isSuccess)
        assertEquals("${productList.name} list deleted", state.userMessage)
    }

    @Test
    fun shouldDeleteShopListWhenUseCaseFails() = runTest(mainDispatcherRule.testDispatcher) {
        //given
        val productList = createTestProductList().first()
        val viewModel = createViewModel(
            HomeScreenViewModel.State(
                shopList = createTestProductList()
            )
        )
        coEvery { deleteProductListUseCase.invoke(productList) } returns UseCaseResult.Error(
            RuntimeException("DB error")
        )

        //when
        viewModel.deleteShopList(productList.productListId)
        advanceUntilIdle()
        //then
        val state = viewModel.state.value
        assertTrue(state.isError)
        assertEquals("DB error", state.userMessage)
    }

    @Test
    fun shouldSetLoadingStateDuringShopListDeletion() = runTest {
        //given
        val productList = createTestProductList().first()
        val viewModel = createViewModel(
            HomeScreenViewModel.State(
                shopList = createTestProductList()
            )
        )
        coEvery { deleteProductListUseCase.invoke(productList) } returns UseCaseResult.Loading

        //when
        viewModel.deleteShopList(productList.productListId)
        advanceUntilIdle()
        //then
        val state = viewModel.state.value
        assertTrue(state.isLoading)
        assertFalse(state.isSuccess)
        assertFalse(state.isError)
    }

    @Test
    fun shouldIgnoreDeleteForMissingList() = runTest {
        //given
        val viewModel = createViewModel(
            HomeScreenViewModel.State(
                shopList = createTestProductList()
            )
        )

        //when
        viewModel.deleteShopList(UUID.randomUUID())
        advanceUntilIdle()

        //then
        coVerify(exactly = 0) { deleteProductListUseCase.invoke(any<ProductList>()) }
    }

    @Test
    fun shouldHandleAddListErrorState() = runTest {
        //given
        val viewModel = createViewModel(
            HomeScreenViewModel.State(
                shopList = createTestProductList(),
                activeDialog = UiDialog.INPUT_DIALOG,
                dialogInput = "New list"
            )
        )
        coEvery { addProductListUseCase.invoke(any<ProductList>()) } returns UseCaseResult.Error("Add failed")

        //when
        viewModel.onDialogConfirm()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertTrue(state.isError)
        assertEquals("Add failed", state.userMessage)
    }

    @Test
    fun shouldHandleAddListLoadingState() = runTest {
        //given
        val viewModel = createViewModel(
            HomeScreenViewModel.State(
                shopList = createTestProductList(),
                activeDialog = UiDialog.INPUT_DIALOG,
                dialogInput = "New list"
            )
        )
        coEvery { addProductListUseCase.invoke(any<ProductList>()) } returns UseCaseResult.Loading
        //when
        viewModel.onDialogConfirm()
        advanceUntilIdle()
        //then
        val state = viewModel.state.value
        assertTrue(state.isLoading)
    }

    private fun createTestProductList() = listOf(
        ProductList(
            productListId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            name = "Test Product List",
            products = listOf(testProductInList),
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

    private val testProductInList = ProductInList(
        id = UUID.fromString("12345678-1234-1234-1234-123456789000"),
        productListId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
        product = testProduct
    )
}
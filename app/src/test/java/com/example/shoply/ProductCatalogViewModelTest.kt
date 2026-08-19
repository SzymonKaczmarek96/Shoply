package com.example.shoply

import com.example.shoply.domain.error.productserror.ProductNameValidationError
import com.example.shoply.domain.error.productserror.ProductNameValidationException
import com.example.shoply.domain.model.Product
import com.example.shoply.domain.model.ProductCategory
import com.example.shoply.domain.model.ProductInList
import com.example.shoply.domain.usecase.UseCaseResult
import com.example.shoply.domain.usecase.product.ChangeProductsCategoryUseCase
import com.example.shoply.domain.usecase.product.DeleteProductsUseCase
import com.example.shoply.domain.usecase.product.GetProductUseCase
import com.example.shoply.domain.usecase.product.InsertProductUseCase
import com.example.shoply.domain.usecase.product.UpdateProductUseCase
import com.example.shoply.domain.usecase.productinlist.AddProductsInListUseCase
import com.example.shoply.domain.usecase.productinlist.GetProductInListUseCase
import com.example.shoply.presentation.components.dialogs.UiDialog
import com.example.shoply.presentation.screens.productcatalogscreen.ProductCatalogScreenViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.sql.SQLException
import java.util.UUID
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ProductCatalogViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getProductUseCase: GetProductUseCase = mockk()
    private val insertProductUseCase: InsertProductUseCase = mockk()
    private val addProductsInListUseCase: AddProductsInListUseCase = mockk()
    private val getProductInList: GetProductInListUseCase = mockk()
    private val deleteProductUseCase: DeleteProductsUseCase = mockk()
    private val updateProductUseCase: UpdateProductUseCase = mockk()
    private val changeProductsCategoryUseCase: ChangeProductsCategoryUseCase = mockk()

    private fun createViewModel(state: ProductCatalogScreenViewModel.State): ProductCatalogScreenViewModel {
        return ProductCatalogScreenViewModel(
            getProductUseCase = getProductUseCase,
            insertProductUseCase = insertProductUseCase,
            addProductsInListUseCase = addProductsInListUseCase,
            getProductInList = getProductInList,
            deleteProductUseCase = deleteProductUseCase,
            updateProductUseCase = updateProductUseCase,
            changeProductsCategoryUseCase = changeProductsCategoryUseCase,
            idDispatcher = mainDispatcherRule.testDispatcher,
            stateInit = state
        )
    }

    @Before
    fun init() {
        every { getProductUseCase() } returns flowOf(testProducts)
    }

    @Test
    fun shouldReturnAllProducts() = runTest {
        //when given
        val viewModel = createViewModel(createTestState())
        advanceUntilIdle()
        //then
        val state = viewModel.state.value
        assertEquals(testProducts, state.allProducts)
        assertEquals(2, state.allProducts.size)
        assertEquals(testProduct, state.allProducts[0])
    }

    @Test
    fun shouldUpdateLastScreenFlag() = runTest {
        //given
        val viewModel = createViewModel(createTestState())

        //when
        viewModel.setIsLastScreen(true)

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isLastScreenProductListScreen)
    }

    @Test
    fun shouldSelectProduct() = runTest {
        //given
        val productId = UUID.fromString("12345678-1234-1234-1234-123456789014")
        val viewModel = createViewModel(createTestStateWithData())
        every { getProductInList.invoke(productId) } returns flowOf(listOf(testProductInList[0]))
        coEvery { addProductsInListUseCase.invoke(any()) } returns Unit

        //when
        viewModel.addSelectedProductList(productId)
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(testProducts, state.allProducts)
        assertEquals(setOf(testProducts[1].productId), state.selectedIds)

        coVerify(exactly = 1) { addProductsInListUseCase.invoke(any()) }
    }

    @Test
    fun shouldSetIsLastScreenOnTrue() = runTest {
        //given
        val viewModel = createViewModel(createTestState())

        //when
        viewModel.setIsLastScreen(true)

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isLastScreenProductListScreen)
    }

    @Test
    fun shouldSetIsLastScreenOnFalse() = runTest {
        //given
        val viewModel = createViewModel(createTestState())

        //when
        viewModel.setIsLastScreen(false)

        //then
        val state = viewModel.state.value
        assertEquals(false, state.isLastScreenProductListScreen)
    }

    @Test
    fun shouldSelectProductById() = runTest {
        //given
        val productId = testProducts[0].productId
        val viewModel = createViewModel(createTestState())

        //when
        viewModel.selectProduct(productId)

        //then
        val state = viewModel.state.value
        assertEquals(productId, state.selectedProductId)
    }

    @Test
    fun shouldSetDialogTypeToUpdate() = runTest {
        //given
        val viewModel = createViewModel(createTestState())

        //when
        viewModel.setDialogTypeOnUpdate(true)

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isUpdateDialog)
    }

    @Test
    fun shouldSetDialogTypeToAdd() = runTest {
        //given
        val viewModel = createViewModel(ProductCatalogScreenViewModel.State(isUpdateDialog = true))

        //when
        viewModel.setDialogTypeOnUpdate(false)

        //then
        val state = viewModel.state.value
        assertEquals(false, state.isUpdateDialog)
    }

    @Test
    fun shouldOpenCreateDialog() = runTest {
        //given
        val viewModel = createViewModel(createTestState())

        //when
        viewModel.onCreateDialog()

        //then
        val state = viewModel.state.value
        assertEquals(UiDialog.INPUT_DIALOG, state.activeDialog)
    }

    @Test
    fun shouldUpdateDialogInput() = runTest {
        //given
        val viewModel = createViewModel(createTestState())
        val inputText = "New Product"

        //when
        viewModel.onDialogInputChange(inputText)

        //then
        val state = viewModel.state.value
        assertEquals(inputText, state.dialogInput)
        assertEquals(null, state.dialogError)
    }

    @Test
    fun shouldClearDialogErrorOnInputChange() = runTest {
        //given
        val viewModel = createViewModel(ProductCatalogScreenViewModel.State(dialogError = "Error"))

        //when
        viewModel.onDialogInputChange("New Input")

        //then
        val state = viewModel.state.value
        assertEquals("New Input", state.dialogInput)
        assertEquals(null, state.dialogError)
    }

    @Test
    fun shouldSelectProductCategory() = runTest {
        //given
        val viewModel = createViewModel(createTestState())

        //when
        viewModel.onSelectedProductCategory(ProductCategory.ELECTRONICS)

        //then
        val state = viewModel.state.value
        assertEquals(ProductCategory.ELECTRONICS, state.selectedCategoryFromDialog)
    }

    @Test
    fun shouldDismissDialog() = runTest {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                activeDialog = UiDialog.INPUT_DIALOG,
                dialogInput = "Input",
                dialogError = "Error",
                selectedCategoryFromDialog = ProductCategory.HOUSEHOLD
            )
        )

        //when
        viewModel.dismissDialog()

        //then
        val state = viewModel.state.value
        assertEquals(UiDialog.NONE, state.activeDialog)
        assertEquals("", state.dialogInput)
        assertEquals(null, state.dialogError)
        assertEquals(ProductCategory.ALL, state.selectedCategoryFromDialog)
    }

    @Test
    fun shouldAddProductToSelectedIds() = runTest {
        //given
        val viewModel = createViewModel(createTestState())
        val productId = testProducts[0].productId

        //when
        viewModel.onProductCheckedChange(productId)

        //then
        val state = viewModel.state.value
        assertEquals(true, state.selectedIds.contains(productId))
    }

    @Test
    fun shouldRemoveProductFromSelectedIds() = runTest {
        //given
        val selectedId = testProducts[0].productId
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                selectedIds = setOf(selectedId)
            )
        )

        //when
        viewModel.onProductCheckedChange(selectedId)

        //then
        val state = viewModel.state.value
        assertEquals(false, state.selectedIds.contains(selectedId))
    }

    @Test
    fun shouldFindProductsBySearchQuery() = runTest {
        //given
        val viewModel = createViewModel(createTestState())
        advanceUntilIdle()

        //when
        viewModel.findProduct("Test")
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals("Test", state.searchQuery)
    }

    @Test
    fun shouldReturnOriginalProductsWhenSearchIsBlank() = runTest {
        //given
        val viewModel = createViewModel(createTestState())
        advanceUntilIdle()

        //when
        viewModel.findProduct("")
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals("", state.searchQuery)
    }

    @Test
    fun shouldFilterByCategory() = runTest {
        //given
        val viewModel = createViewModel(createTestState())
        advanceUntilIdle()

        //when
        viewModel.onCategorySelected(ProductCategory.ELECTRONICS)

        //then
        val state = viewModel.state.value
        assertEquals(ProductCategory.ELECTRONICS, state.selectedCategoryFromFilterCategory)
    }

    @Test
    fun shouldDeleteProductsSuccessfully() = runTest {
        //given
        val viewModel = createViewModel(createTestStateWithData())
        coEvery { deleteProductUseCase.invoke(any()) } returns UseCaseResult.Success("Deleted")

        //when
        viewModel.deleteProducts()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isSuccess)
        assertEquals("Deleted", state.userMessage)
        coVerify(exactly = 1) { deleteProductUseCase.invoke(any()) }
    }

    @Test
    fun shouldHandleDeleteProductsError() = runTest {
        //given
        val viewModel = createViewModel(createTestState())
        coEvery { deleteProductUseCase.invoke(any()) } returns UseCaseResult.Error("Error")

        //when
        viewModel.deleteProducts()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isError)
        assertEquals("Error", state.userMessage)
    }

    @Test
    fun shouldHandleDeleteProductsLoadingState() = runTest {
        //given
        val viewModel = createViewModel(createTestState())
        coEvery { deleteProductUseCase.invoke(any()) } returns UseCaseResult.Loading

        //when
        viewModel.deleteProducts()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isError)
        assertEquals("Something went wrong", state.userMessage)
    }

    @Test
    fun shouldUpdateProductSuccessfully() = runTest {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                allProducts = testProducts,
                dialogInput = "Updated",
                selectedProductId = testProducts[0].productId,
                selectedCategoryFromDialog = ProductCategory.HOUSEHOLD,
                isUpdateDialog = true,
            )
        )
        coEvery { updateProductUseCase.invoke(any()) } returns UseCaseResult.Success(Unit)

        //when
        viewModel.confirmInput()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isSuccess)
        assertEquals("Successfully saved product", state.userMessage)
    }

    @Test
    fun shouldAddProductSuccessfully() = runTest {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                allProducts = testProducts,
                dialogInput = "New Product",
                selectedCategoryFromDialog = ProductCategory.GROCERY,
                isUpdateDialog = false,
            )
        )
        coEvery { insertProductUseCase.invoke(any()) } returns UseCaseResult.Success(Unit)

        //when
        viewModel.confirmInput()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isSuccess)
        assertEquals("Successfully saved product", state.userMessage)
    }

    @Test
    fun shouldHandleAddProductWithBlankName() = runTest {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                dialogInput = "Product",
                selectedCategoryFromDialog = ProductCategory.GROCERY,
                isUpdateDialog = false,
            )
        )
        coEvery { insertProductUseCase.invoke(any()) } returns UseCaseResult.Error(
            error = ProductNameValidationException(error = ProductNameValidationError.BLANK)
        )

        //when
        viewModel.confirmInput()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isError)
        assertEquals("Product name cannot be blank", state.dialogError)
    }

    @Test
    fun shouldHandleAddProductWithTooLongName() = runTest {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                dialogInput = "A".repeat(300),
                selectedCategoryFromDialog = ProductCategory.GROCERY,
                isUpdateDialog = false,
            )
        )
        coEvery { insertProductUseCase.invoke(any()) } returns UseCaseResult.Error(
            error = ProductNameValidationException(error = ProductNameValidationError.TOO_LONG)
        )

        //when
        viewModel.confirmInput()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isError)
        assertEquals("Product name is too long", state.dialogError)
    }

    @Test
    fun shouldHandleAddProductWithDuplicateName() = runTest {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                dialogInput = "Duplicate",
                selectedCategoryFromDialog = ProductCategory.GROCERY,
                isUpdateDialog = false,
            )
        )
        coEvery { insertProductUseCase.invoke(any()) } returns UseCaseResult.Error(
            error = ProductNameValidationException(error = ProductNameValidationError.ALREADY_EXISTS)
        )

        //when
        viewModel.confirmInput()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isError)
        assertEquals("Product with this name already exists", state.dialogError)
    }

    @Test
    fun shouldHandleAddProductWithIOException() = runTest {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                dialogInput = "Product",
                selectedCategoryFromDialog = ProductCategory.GROCERY,
                isUpdateDialog = false,
            )
        )
        coEvery { insertProductUseCase.invoke(any()) } returns UseCaseResult.Error(
            error = IOException("Network error")
        )

        //when
        viewModel.confirmInput()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isError)
        assertEquals("Network error", state.dialogError)
    }

    @Test
    fun shouldHandleAddProductWithSQLException() = runTest {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                dialogInput = "Product",
                selectedCategoryFromDialog = ProductCategory.GROCERY,
                isUpdateDialog = false,
            )
        )
        coEvery { insertProductUseCase.invoke(any()) } returns UseCaseResult.Error(
            error = SQLException("DB error")
        )

        //when
        viewModel.confirmInput()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isError)
        assertEquals("DB error", state.dialogError)
    }

    @Test
    fun shouldHandleAddProductLoadingState() = runTest {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                dialogInput = "Product",
                selectedCategoryFromDialog = ProductCategory.GROCERY,
                isUpdateDialog = false,
            )
        )
        coEvery { insertProductUseCase.invoke(any()) } returns UseCaseResult.Loading

        //when
        viewModel.confirmInput()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isLoading)
    }

    @Test
    fun shouldHandleUpdateProductLoadingState() = runTest {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                allProducts = testProducts,
                dialogInput = "Updated",
                selectedProductId = testProducts[0].productId,
                selectedCategoryFromDialog = ProductCategory.ELECTRONICS,
                isUpdateDialog = true,
            )
        )
        coEvery { updateProductUseCase.invoke(any()) } returns UseCaseResult.Loading

        //when
        viewModel.confirmInput()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isLoading)
    }

    @Test
    fun shouldHandleUpdateProductError() = runTest {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                allProducts = testProducts,
                dialogInput = "Updated",
                selectedProductId = testProducts[0].productId,
                selectedCategoryFromDialog = ProductCategory.HOUSEHOLD,
                isUpdateDialog = true,
            )
        )
        coEvery { updateProductUseCase.invoke(any()) } returns UseCaseResult.Error(
            error = IOException("Error")
        )

        //when
        viewModel.confirmInput()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isError)
        assertEquals("Error", state.dialogError)
    }

    @Test
    fun shouldAddNewCategoryWhenProductSaved() = runTest {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                allProducts = testProducts,
                dialogInput = "New",
                selectedCategoryFromDialog = ProductCategory.HOUSEHOLD,
                isUpdateDialog = false,
                existingProductCategory = setOf(ProductCategory.GROCERY, ProductCategory.ALL)
            )
        )
        coEvery { insertProductUseCase.invoke(any()) } returns UseCaseResult.Success(Unit)

        //when
        viewModel.confirmInput()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.existingProductCategory.contains(ProductCategory.HOUSEHOLD))
    }

    @Test
    fun shouldNotAddDuplicateCategory() = runTest {
        //given
        val initialCategories = setOf(ProductCategory.GROCERY, ProductCategory.ALL)
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                allProducts = testProducts,
                dialogInput = "New",
                selectedCategoryFromDialog = ProductCategory.GROCERY,
                isUpdateDialog = false,
                existingProductCategory = initialCategories
            )
        )
        coEvery { insertProductUseCase.invoke(any()) } returns UseCaseResult.Success(Unit)

        //when
        viewModel.confirmInput()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isSuccess)
        assertEquals(true, state.existingProductCategory.contains(ProductCategory.GROCERY))
    }

    @Test
    fun shouldReturnItemsFilteredByCategory() = runTest {
        //given
        val viewModel = createViewModel(createTestState())
        advanceUntilIdle()

        //when
        viewModel.onCategorySelected(ProductCategory.ELECTRONICS)

        //then
        val state = viewModel.state.value
        val items = state.items
        val electronicsProducts = testProducts.filter { it.category == ProductCategory.ELECTRONICS }
        assertEquals(electronicsProducts.size, items.size)
    }

    @Test
    fun shouldReturnAllItemsWhenCategoryIsAll() = runTest {
        //given
        val viewModel = createViewModel(createTestState())
        advanceUntilIdle()

        //when
        viewModel.onCategorySelected(ProductCategory.ALL)

        //then
        val state = viewModel.state.value
        val items = state.items
        assertEquals(testProducts.size, items.size)
    }

    @Test
    fun shouldUpdateProductNotFoundWhenSelectedProductIdNull() = runTest {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                allProducts = testProducts,
                dialogInput = "Updated",
                selectedProductId = null,
                selectedCategoryFromDialog = ProductCategory.ELECTRONICS,
                isUpdateDialog = true,
            )
        )
        coEvery { updateProductUseCase.invoke(any()) } returns UseCaseResult.Success(Unit)

        //when
        viewModel.confirmInput()
        advanceUntilIdle()

        //then
        coVerify(exactly = 0) { updateProductUseCase.invoke(any()) }
    }

    @Test
    fun shouldUpdateProductWhenProductFound() = runTest {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                allProducts = testProducts,
                dialogInput = "Updated Product",
                selectedProductId = testProducts[0].productId,
                selectedCategoryFromDialog = ProductCategory.HOUSEHOLD,
                isUpdateDialog = true,
            )
        )
        coEvery { updateProductUseCase.invoke(any()) } returns UseCaseResult.Success(Unit)

        //when
        viewModel.confirmInput()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isSuccess)
        coVerify(exactly = 1) { updateProductUseCase.invoke(any()) }
    }

    @Test
    fun shouldChangeCategoryOnFavorites() = runTest {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                allProducts = testProducts,
                selectedIds = setOf(testProducts[0].productId, testProducts[1].productId)
            )
        )
        coEvery { changeProductsCategoryUseCase.invoke(any()) } returns UseCaseResult.Success(Unit)
        coEvery { getProductUseCase.invoke() } returns flowOf(testProducts.map { it.copy(category = ProductCategory.FAVORITE) })

        //when
        viewModel.changeCategoryToFavorite()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isSuccess)
        assertEquals(state.userMessage, "Category updated successfully")
        assertEquals(state.allProducts[0].category, ProductCategory.FAVORITE)
        assertEquals(state.allProducts[1].category, ProductCategory.FAVORITE)
    }

    @Test
    fun shouldChangeCategoryOnElectronics() = runTest {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                allProducts = testProducts,
                selectedIds = setOf(testProducts[0].productId, testProducts[1].productId),
                selectedCategoryFromDialog = ProductCategory.ELECTRONICS
            )
        )
        coEvery { changeProductsCategoryUseCase.invoke(any()) } returns UseCaseResult.Success(Unit)
        coEvery { getProductUseCase.invoke() } returns flowOf(testProducts.map { it.copy(category = ProductCategory.ELECTRONICS) })

        //when
        viewModel.changeCategoryForSelectedCategory()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isSuccess)
        assertEquals(state.userMessage, "Category updated successfully")
        assertEquals(state.allProducts[0].category, ProductCategory.ELECTRONICS)
        assertEquals(state.allProducts[1].category, ProductCategory.ELECTRONICS)

    }

    @Test
    fun shouldThrowErrorMessageWhenChangingCategoryOnElectronics() = runTest {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                allProducts = testProducts,
                selectedIds = setOf(testProducts[0].productId, testProducts[1].productId),
                selectedCategoryFromDialog = ProductCategory.ELECTRONICS
            )
        )
        coEvery { changeProductsCategoryUseCase.invoke(any()) } returns UseCaseResult.Error(
            error = IOException(
                "Failed to change category"
            )
        )

        //when
        viewModel.changeCategoryForSelectedCategory()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isError)
        assertEquals(state.userMessage, "Failed to change category")
        assertEquals(state.allProducts[0].category, ProductCategory.GROCERY)
        assertEquals(state.allProducts[1].category, ProductCategory.ELECTRONICS)
    }

    @Test
    fun shouldIsLoadingWhenChangingCategoryOnElectronics() = runTest {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                allProducts = testProducts,
                selectedIds = setOf(testProducts[0].productId, testProducts[1].productId),
                selectedCategoryFromDialog = ProductCategory.ELECTRONICS
            )
        )
        coEvery { changeProductsCategoryUseCase.invoke(any()) } returns UseCaseResult.Loading

        //when
        viewModel.changeCategoryForSelectedCategory()
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isLoading)
        assertEquals(state.allProducts[0].category, ProductCategory.GROCERY)
        assertEquals(state.allProducts[1].category, ProductCategory.ELECTRONICS)
    }

    @Test
    fun shouldUpdateCategoryDialog() {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                selectedIds = setOf(testProducts[0].productId, testProducts[1].productId),
                isCategoryUpdate = false
            )
        )

        //when
        viewModel.setCategoryUpdate(true)

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isCategoryUpdate)
    }

    @Test
    fun shouldShowUpdateCategoryDialog() {
        //given
        val viewModel = createViewModel(
            ProductCatalogScreenViewModel.State(
                selectedIds = setOf(testProducts[0].productId, testProducts[1].productId),
                isCategoryUpdate = false
            )
        )

        //when
        viewModel.showUpdateCategoryDialog()

        //then
        val state = viewModel.state.value
        assertEquals(true, state.isCategoryUpdate)
        assertEquals(UiDialog.INPUT_DIALOG, state.activeDialog)
    }

    private val testProduct = Product(
        productId = UUID.fromString("12345678-1234-1234-1234-123456789014"),
        name = "Test Product",
        category = ProductCategory.GROCERY
    )

    private val testProducts = listOf(
        testProduct,
        Product(
            productId = UUID.fromString("12345678-1234-1234-1234-123456789012"),
            name = "Another Product",
            category = ProductCategory.ELECTRONICS
        )
    )

    private val testProductInList = listOf(
        ProductInList(
            id = UUID.randomUUID(),
            productListId = UUID.fromString("12345678-1234-1234-1234-123456789014"),
            product = testProduct,
            isPurchased = false,
            quantity = 1
        )
    )


    private fun createTestState() = ProductCatalogScreenViewModel.State()

    private fun createTestStateWithData() = ProductCatalogScreenViewModel.State(
        selectedIds = setOf(testProducts[1].productId)
    )

}
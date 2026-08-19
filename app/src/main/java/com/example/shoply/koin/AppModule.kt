package com.example.shoply.koin

import androidx.room.Room
import com.example.shoply.data.db.ShoplyDatabase
import com.example.shoply.data.repository.ProductInListRepository
import com.example.shoply.data.repository.ProductInListRepositoryImpl
import com.example.shoply.data.repository.ProductListRepository
import com.example.shoply.data.repository.ProductListRepositoryImpl
import com.example.shoply.data.repository.ProductRepository
import com.example.shoply.data.repository.ProductRepositoryImpl
import com.example.shoply.domain.usecase.product.ChangeProductsCategoryUseCase
import com.example.shoply.domain.usecase.product.DeleteProductsUseCase
import com.example.shoply.domain.usecase.product.GetOrCreateProductUseCase
import com.example.shoply.domain.usecase.product.GetProductUseCase
import com.example.shoply.domain.usecase.product.InsertProductUseCase
import com.example.shoply.domain.usecase.product.UpdateProductUseCase
import com.example.shoply.domain.usecase.product.ValidateProductNameUseCase
import com.example.shoply.domain.usecase.productinlist.AddProductInListUseCase
import com.example.shoply.domain.usecase.productinlist.AddProductsInListUseCase
import com.example.shoply.domain.usecase.productinlist.DeleteProductInListUseCase
import com.example.shoply.domain.usecase.productinlist.DeleteProductsInListUseCase
import com.example.shoply.domain.usecase.productinlist.FindProductsInListUseCase
import com.example.shoply.domain.usecase.productinlist.GetProductInListUseCase
import com.example.shoply.domain.usecase.productinlist.UpdateProductInListUseCase
import com.example.shoply.domain.usecase.productlist.AddProductListUseCase
import com.example.shoply.domain.usecase.productlist.DeleteProductListUseCase
import com.example.shoply.domain.usecase.productlist.GetProductListUseCase
import com.example.shoply.domain.usecase.productlist.GetProductListsWithDetailsUseCase
import com.example.shoply.presentation.screens.homescreen.HomeScreenViewModel
import com.example.shoply.presentation.screens.productcatalogscreen.ProductCatalogScreenViewModel
import com.example.shoply.presentation.screens.productlistscreen.ProductListScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //viewModel
    viewModel {
        HomeScreenViewModel(
            addProductListUseCase = get(),
            deleteProductListUseCase = get(),
            getProductListsWithDetailsUseCase = get(),
        )
    }
    viewModel {
        ProductCatalogScreenViewModel(
            getProductUseCase = get(),
            insertProductUseCase = get(),
            addProductsInListUseCase = get(),
            getProductInList = get(),
            deleteProductUseCase = get(),
            updateProductUseCase = get(),
            changeProductsCategoryUseCase = get(),
        )
    }
    viewModel {
        ProductListScreenViewModel(
            getProductInList = get(),
            addProductInListUseCase = get(),
            deleteProductInListUseCase = get(),
            getOrCreateProductUseCase = get(),
            updateProductInListUseCase = get(),
            getProductListsWithDetailsUseCase = get(),
            deleteProductsInListUseCase = get(),
            findProductsInListUseCase = get(),
        )
    }

    // Repository
    factory<ProductRepository> {
        ProductRepositoryImpl(get())
    }
    factory<ProductListRepository> {
        ProductListRepositoryImpl(get(), get(), get())
    }
    factory<ProductInListRepository> {
        ProductInListRepositoryImpl(get())
    }

    // UseCases
    factory { ValidateProductNameUseCase(get()) }
    factory { InsertProductUseCase(get(), get()) }
    factory { GetProductUseCase(get()) }
    factory { GetProductListUseCase(get()) }
    factory { AddProductListUseCase(get()) }
    factory { DeleteProductListUseCase(get()) }
    factory { GetProductInListUseCase(get()) }
    factory { AddProductInListUseCase(get()) }

    factory { AddProductsInListUseCase(get()) }
    factory { DeleteProductInListUseCase(get()) }
    factory { DeleteProductsInListUseCase(get()) }
    factory { GetOrCreateProductUseCase(get()) }
    factory { GetProductListsWithDetailsUseCase(get()) }
    factory { UpdateProductInListUseCase(get()) }
    factory { DeleteProductsUseCase(get(), get()) }
    factory { FindProductsInListUseCase(get()) }
    factory { ValidateProductNameUseCase(get()) }
    factory { UpdateProductUseCase(get(), get()) }
    factory { ChangeProductsCategoryUseCase(get()) }
}

//db
val databaseModule = module {
    single {
        Room.databaseBuilder(
            context = get(),
            klass = ShoplyDatabase::class.java,
            name = "shoply_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<ShoplyDatabase>().userDao() }
    single { get<ShoplyDatabase>().productDao() }
    single { get<ShoplyDatabase>().productListDao() }
    single { get<ShoplyDatabase>().productInListDao() }
}

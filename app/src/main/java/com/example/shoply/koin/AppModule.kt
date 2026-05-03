package com.example.shoply.koin

import androidx.room.Room
import com.example.shoply.data.db.ShoplyDatabase
import com.example.shoply.data.repository.ProductListRepository
import com.example.shoply.data.repository.ProductListRepositoryImpl
import com.example.shoply.data.repository.ProductRepository
import com.example.shoply.data.repository.ProductRepositoryImpl
import com.example.shoply.domain.usecase.product.GetProductUseCase
import com.example.shoply.domain.usecase.product.InsertProductUseCase
import com.example.shoply.domain.usecase.productinlist.GetProductInListUseCase
import com.example.shoply.domain.usecase.productlist.AddProductListUseCase
import com.example.shoply.domain.usecase.productlist.DeleteProductListUseCase
import com.example.shoply.domain.usecase.productlist.GetProductListUseCase
import com.example.shoply.presentation.screens.homescreen.HomeScreenViewModel
import com.example.shoply.presentation.screens.productcatalogscreen.ProductCatalogScreenViewModel
import com.example.shoply.presentation.screens.productlistscreen.ProductListScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        HomeScreenViewModel(
            getProductListUseCase = get(),
            addProductListUseCase = get(),
            deleteProductListUseCase = get(),
        )
    }
    viewModel {
        ProductCatalogScreenViewModel(
            getProductUseCase = get(),
            insertProductUseCase = get(),
        )
    }
    viewModel {
        ProductListScreenViewModel(
            addProductListUseCase = get(),
            getProductInList = get(),
        )
    }

    // Repository
    factory<ProductRepository> {
        ProductRepositoryImpl(get())
    }
    factory<ProductListRepository> {
        ProductListRepositoryImpl(get())
    }

    // UseCases
    factory { InsertProductUseCase(get()) }
    factory { GetProductUseCase(get()) }
    factory { GetProductListUseCase(get()) }
    factory { AddProductListUseCase(get()) }
    factory { DeleteProductListUseCase(get()) }
    factory { GetProductInListUseCase(get()) }
}

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
}

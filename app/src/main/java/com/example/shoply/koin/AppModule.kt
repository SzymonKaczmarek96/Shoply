package com.example.shoply.koin

import androidx.room.Room
import com.example.shoply.data.db.ShoplyDatabase
import com.example.shoply.data.repository.ProductRepository
import com.example.shoply.data.repository.ProductRepositoryImpl
import com.example.shoply.domain.usecase.GetProductListUseCase
import com.example.shoply.domain.usecase.GetProductUseCase
import com.example.shoply.domain.usecase.InsertProductUseCase
import com.example.shoply.presentation.screens.homescreen.HomeScreenViewModel
import com.example.shoply.presentation.screens.productcatalogscreen.ProductCatalogScreenViewModel
import com.example.shoply.presentation.screens.productlistscreen.ProductListScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::HomeScreenViewModel)
    viewModel {
        ProductCatalogScreenViewModel(
            getProductUseCase = get(),
            insertProductUseCase = get(),
        )
    }
    viewModel {
        ProductListScreenViewModel()
    }

    // Repository
    factory<ProductRepository> {
        ProductRepositoryImpl(get())
    }

    // UseCases
    factory { InsertProductUseCase(get()) }
    factory { GetProductUseCase(get()) }
    factory { GetProductListUseCase() }
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
}

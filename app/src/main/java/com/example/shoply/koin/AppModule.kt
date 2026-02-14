package com.example.shoply.koin

import com.example.shoply.domain.usecase.GetProductListUseCase
import com.example.shoply.presentation.components.dialogs.DialogHost
import com.example.shoply.presentation.screens.homescreen.HomeScreenViewModel
import com.example.shoply.presentation.screens.productcatalogscreen.ProductCatalogScreenViewModel
import com.example.shoply.presentation.screens.productcatalogscreen.TestUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    viewModelOf(::HomeScreenViewModel)
    viewModelOf(::ProductCatalogScreenViewModel)
    factory { GetProductListUseCase() }
    factory { TestUseCase() }
    single { DialogHost() }
}

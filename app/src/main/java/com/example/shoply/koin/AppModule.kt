package com.example.shoply.koin

import com.example.shoply.presentation.screens.homescreen.HomeScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::HomeScreenViewModel)
}

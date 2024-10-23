package com.example.labimagecompressor.di

import com.example.labimagecompressor.viewmodels.AbstractMainViewModel
import com.example.labimagecompressor.viewmodels.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<AbstractMainViewModel> {
        MainViewModel()
    }

}
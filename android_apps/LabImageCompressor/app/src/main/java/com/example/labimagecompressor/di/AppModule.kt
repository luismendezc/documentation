package com.example.labimagecompressor.di

import com.example.labimagecompressor.util.FileManagerImpl
import com.example.labimagecompressor.util.ImageCompressorImpl
import com.example.labimagecompressor.util.interfaces.FileManager
import com.example.labimagecompressor.util.interfaces.ImageCompressor
import com.example.labimagecompressor.viewmodels.AbstractMainViewModel
import com.example.labimagecompressor.viewmodels.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single <ImageCompressor> {
        ImageCompressorImpl(androidContext())
    }

    single <FileManager> {
        FileManagerImpl(androidContext())
    }

    viewModel<AbstractMainViewModel> {
        MainViewModel(
            get(),
            get()
        )
    }

}
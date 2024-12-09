package com.example.lab_testing_coroutines.di

import com.example.lab_testing_coroutines.logic.auth.AuthenticationService
import com.example.lab_testing_coroutines.logic.auth.AuthenticationServiceInterface
import com.example.lab_testing_coroutines.logic.auth.VerimiService
import com.example.lab_testing_coroutines.logic.auth.VerimiServiceInterface
import com.example.lab_testing_coroutines.viewmodels.MainActivityViewModel
import com.example.lab_testing_coroutines.viewmodels.MainActivityViewModelAbstract
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<VerimiServiceInterface> { VerimiService() }
    single<AuthenticationServiceInterface> { AuthenticationService(verimiService = get()) }

    viewModel<MainActivityViewModelAbstract> {
        MainActivityViewModel(authService = get())
    }


}
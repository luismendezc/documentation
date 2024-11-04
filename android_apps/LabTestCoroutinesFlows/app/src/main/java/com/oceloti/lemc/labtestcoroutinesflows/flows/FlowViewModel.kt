@file:OptIn(FlowPreview::class)

package com.oceloti.lemc.labtestcoroutinesflows.flows

import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oceloti.lemc.labtestcoroutinesflows.util.DispatcherProvider
import com.oceloti.lemc.labtestcoroutinesflows.util.StandardDispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class FlowViewModel(
    private val dispatchers: DispatcherProvider = StandardDispatchers()
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()
    
    val canRegister = email
        .debounce(500L)
        .combine(
            password,
        ) { email, password ->
            val isValidEmail = PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
            val isValidPassword = password.any { !it.isLetterOrDigit() } &&
                    password.length > 9

            isValidPassword && isValidEmail
        }.stateIn(
            viewModelScope + dispatchers.mainImmediate,
            SharingStarted.WhileSubscribed(5000L),
            false
        )

    fun register() {
        viewModelScope.launch (dispatchers.io) {
            _isLoading.value = true
            delay(3000L) // Registering...
            _isLoading.value = false
        }
    }

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }
}
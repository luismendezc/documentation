package com.example.lab_testing_coroutines.logic.auth

sealed class AuthState {
    class DisplayError(
        val title: String? = null,
        val description: String? = null,
    ): AuthState()

    data object Auth: AuthState()

    data object Unauthenticated: AuthState()
}
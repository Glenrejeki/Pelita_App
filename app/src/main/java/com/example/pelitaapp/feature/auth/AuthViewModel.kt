package com.example.pelitaapp.feature.auth

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,

    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val username: String = "",
    val fullName: String = "",

    val errorMessage: String? = null,
    val successMessage: String? = null
)

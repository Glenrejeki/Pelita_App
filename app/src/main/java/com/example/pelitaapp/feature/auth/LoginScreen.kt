package com.example.pelitaapp.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pelitaapp.core.data.repository.AuthRepository
import com.example.pelitaapp.core.util.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    init {
        // cek login state dari userId
        val isLoggedIn = authRepository.getCurrentUserId() != null
        _uiState.update { it.copy(isLoggedIn = isLoggedIn) }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    // ========= input handlers =========
    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value, errorMessage = null) }
    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value, errorMessage = null) }
    fun onConfirmPasswordChange(value: String) = _uiState.update { it.copy(confirmPassword = value, errorMessage = null) }
    fun onUsernameChange(value: String) = _uiState.update { it.copy(username = value, errorMessage = null) }
    fun onFullNameChange(value: String) = _uiState.update { it.copy(fullName = value, errorMessage = null) }

    // ========= actions =========
    fun login() {
        val state = _uiState.value

        val email = state.email.trim()
        val password = state.password

        if (!Validators.isEmailValid(email)) {
            _uiState.update { it.copy(errorMessage = "Email tidak valid.") }
            return
        }
        if (!Validators.isPasswordValid(password)) {
            _uiState.update { it.copy(errorMessage = "Password minimal 8 karakter.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                authRepository.signIn(email, password)
                _uiState.update { it.copy(isLoading = false, isLoggedIn = true, successMessage = "Login berhasil!") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Login gagal.") }
            }
        }
    }

    fun register() {
        val state = _uiState.value

        val email = state.email.trim()
        val password = state.password
        val confirm = state.confirmPassword
        val username = state.username.trim()
        val fullName = state.fullName.trim().ifBlank { null }

        if (!Validators.isEmailValid(email)) {
            _uiState.update { it.copy(errorMessage = "Email tidak valid.") }
            return
        }
        if (!Validators.isUsernameValid(username)) {
            _uiState.update { it.copy(errorMessage = "Username minimal 3 karakter, hanya huruf/angka/._") }
            return
        }
        if (!Validators.isPasswordValid(password)) {
            _uiState.update { it.copy(errorMessage = "Password minimal 8 karakter.") }
            return
        }
        if (password != confirm) {
            _uiState.update { it.copy(errorMessage = "Konfirmasi password tidak cocok.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                authRepository.signUp(
                    email = email,
                    password = password,
                    username = username,
                    fullName = fullName
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        successMessage = "Akun berhasil dibuat!"
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Register gagal.") }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                authRepository.signOut()
                _uiState.update {
                    AuthUiState(
                        isLoading = false,
                        isLoggedIn = false,
                        successMessage = "Logout berhasil."
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Logout gagal.") }
            }
        }
    }
}

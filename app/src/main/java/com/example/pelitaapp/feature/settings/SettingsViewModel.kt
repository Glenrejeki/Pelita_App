package com.example.pelitaapp.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pelitaapp.core.data.repository.AuthRepository
import com.example.pelitaapp.core.datastore.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState(isLoading = true))
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        observeTheme()
    }

    private fun observeTheme() {
        viewModelScope.launch {
            try {
                settingsDataStore.isDarkModeFlow.collect { isDark ->
                    _uiState.update { it.copy(isLoading = false, isDarkMode = isDark) }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "Gagal memuat setting.")
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            try {
                settingsDataStore.setDarkMode(enabled)
                _uiState.update { it.copy(successMessage = "Tema diperbarui.") }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Gagal menyimpan tema.") }
            }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                authRepository.signOut()
                _uiState.update { it.copy(isLoading = false, successMessage = "Logout berhasil.") }
                onLoggedOut()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Logout gagal.") }
            }
        }
    }

    /**
     * Placeholder: Delete account butuh implementasi di AuthRepository + Supabase.
     */
    fun deleteAccount() {
        _uiState.update {
            it.copy(errorMessage = "Delete account belum diimplementasi (butuh Supabase function/RLS).")
        }
    }
}
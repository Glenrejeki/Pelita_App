package com.example.pelitaapp.feature.settings

data class SettingsUiState(){
    val isLoading: Boolean = false,
    val isDarkMode: Boolean = false,

    val errorMessage: String? = null,
    val successMessage: String? = null
}

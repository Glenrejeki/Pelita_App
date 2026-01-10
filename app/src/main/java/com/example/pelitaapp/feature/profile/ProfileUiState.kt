package com.example.pelitaapp.feature.profile

import com.example.pelitaapp.core.data.model.Profile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,

    // edit state
    val usernameInput: String = "",
    val fullNameInput: String = "",
    val bioInput: String = "",
    val avatarUrlInput: String = "",

    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

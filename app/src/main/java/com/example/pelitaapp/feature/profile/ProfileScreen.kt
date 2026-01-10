package com.example.pelitaapp.feature.profile

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
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadMyProfile()
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    fun loadMyProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val profile = authRepository.getMyProfile()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        profile = profile,
                        usernameInput = profile?.username ?: "",
                        fullNameInput = profile?.fullName ?: "",
                        bioInput = profile?.bio ?: "",
                        avatarUrlInput = profile?.avatarUrl ?: ""
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Gagal memuat profil."
                    )
                }
            }
        }
    }

    // ===== input handlers =====
    fun onUsernameChange(v: String) = _uiState.update { it.copy(usernameInput = v, errorMessage = null) }
    fun onFullNameChange(v: String) = _uiState.update { it.copy(fullNameInput = v, errorMessage = null) }
    fun onBioChange(v: String) = _uiState.update { it.copy(bioInput = v, errorMessage = null) }
    fun onAvatarUrlChange(v: String) = _uiState.update { it.copy(avatarUrlInput = v, errorMessage = null) }

    fun saveProfile() {
        val state = _uiState.value

        val username = state.usernameInput.trim()
        val fullName = state.fullNameInput.trim()
        val bio = state.bioInput.trim()
        val avatar = state.avatarUrlInput.trim()

        if (username.isNotBlank() && !Validators.isUsernameValid(username)) {
            _uiState.update { it.copy(errorMessage = "Username tidak valid (min 3, hanya huruf/angka/._).") }
            return
        }
        if (!Validators.isBioValid(bio)) {
            _uiState.update { it.copy(errorMessage = "Bio maksimal 160 karakter.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, successMessage = null) }
            try {
                authRepository.updateMyProfile(
                    username = username.ifBlank { null },
                    fullName = fullName.ifBlank { null },
                    bio = bio.ifBlank { null },
                    avatarUrl = avatar.ifBlank { null }
                )
                _uiState.update { it.copy(isSaving = false, successMessage = "Profil berhasil disimpan.") }
                loadMyProfile()
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, errorMessage = e.message ?: "Gagal menyimpan profil.") }
            }
        }
    }
}

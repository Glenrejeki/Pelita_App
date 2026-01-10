package com.example.pelitaapp.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pelitaapp.core.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    private var searchJob: Job? = null

    fun onQueryChange(value: String) {
        _uiState.update { it.copy(query = value, errorMessage = null) }

        // Debounce biar tidak spam
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(350)
            search()
        }
    }

    fun search() {
        val keyword = _uiState.value.query.trim()
        if (keyword.isBlank()) {
            _uiState.update { it.copy(postResults = emptyList(), accountResults = emptyList(), errorMessage = null) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                // MVP: ambil feed lalu filter lokal
                val feed = postRepository.getFeed(limit = 50, offset = 0)

                val lower = keyword.lowercase()

                val filtered = feed.filter { post ->
                    val contentMatch = (post.content ?: "").lowercase().contains(lower)
                    val userMatch = post.author.username.lowercase().contains(lower)
                    contentMatch || userMatch
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        postResults = filtered,
                        // accountResults masih kosong (nanti jika ada search profiles)
                        accountResults = emptyList()
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Gagal mencari.") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
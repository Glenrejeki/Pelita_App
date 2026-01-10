package com.example.pelitaapp.feature.search

import com.example.pelitaapp.core.data.model.Post
import com.example.pelitaapp.core.data.model.Profile

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,

    // result
    val postResults: List<Post> = emptyList(),

    // placeholder untuk akun (nanti kalau ada endpoint)
    val accountResults: List<Profile> = emptyList(),

    val errorMessage: String? = null
)

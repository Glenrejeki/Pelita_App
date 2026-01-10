package com.example.pelitaapp.feature.feed

import com.example.pelitaapp.core.data.model.Post

data class FeedUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val posts: List<Post> = emptyList(),
    val errorMessage: String? = null
)

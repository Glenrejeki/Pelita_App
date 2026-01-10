package com.example.pelitaapp.feature.post

import com.example.pelitaapp.core.data.model.Comment
import com.example.pelitaapp.core.data.model.Post

data class PostUiState(
    val isLoading: Boolean = false,

    // create post
    val content: String = "",
    val isPosting: Boolean = false,

    // detail
    val post: Post? = null,
    val comments: List<Comment> = emptyList(),
    val commentInput: String = "",
    val isSubmittingComment: Boolean = false,

    val errorMessage: String? = null,
    val successMessage: String? = null
)

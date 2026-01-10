package com.example.pelitaapp.core.data.model

import java.time.Instant

/**
 * Komentar pada Post
 */
data class Comment(
    val id: String,
    val postId: String,
    val author: Profile,
    val content: String,
    val createdAt: Instant
)

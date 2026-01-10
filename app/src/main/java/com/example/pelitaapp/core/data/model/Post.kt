package com.example.pelitaapp.core.data.model

import java.time.Instant

/**
 * Model Post (Firman / Renungan / Repost)
 */
data class Post(
    val id: String,
    val author: Profile,
    val content: String? = null,

    // Repost / Quote
    val postType: PostType = PostType.ORIGINAL,
    val repostOfPostId: String? = null,
    val originalPost: Post? = null,

    // Statistik
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val repostCount: Int = 0,

    // Status user
    val isLikedByMe: Boolean = false,
    val isRepostedByMe: Boolean = false,

    // Waktu
    val createdAt: Instant
)

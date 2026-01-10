package com.example.pelitaapp.core.data.repository

import com.example.pelitaapp.core.data.model.Comment
import com.example.pelitaapp.core.data.model.Post

interface PostRepository {

    suspend fun createPost(content: String): String

    suspend fun getFeed(limit: Int = 20, offset: Int = 0): List<Post>

    suspend fun addComment(postId: String, content: String): String

    suspend fun getComments(postId: String, limit: Int = 50): List<Comment>

    suspend fun toggleLike(postId: String)

    suspend fun repost(originalPostId: String): String

    suspend fun undoRepost(originalPostId: String)
}

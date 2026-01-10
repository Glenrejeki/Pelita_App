package com.example.pelitaapp.core.data.repository

import com.example.pelitaapp.core.data.model.Comment
import com.example.pelitaapp.core.data.model.Post
import com.example.pelitaapp.core.data.remote.PostRemoteDataSource

class PostRepositoryImpl(
    private val remote: PostRemoteDataSource
) : PostRepository {

    override suspend fun createPost(content: String): String {
        return remote.createPost(content)
    }

    override suspend fun getFeed(limit: Int, offset: Int): List<Post> {
        return remote.getFeed(limit = limit, offset = offset)
    }

    override suspend fun addComment(postId: String, content: String): String {
        return remote.addComment(postId = postId, content = content)
    }

    override suspend fun getComments(postId: String, limit: Int): List<Comment> {
        return remote.getComments(postId = postId, limit = limit)
    }

    override suspend fun toggleLike(postId: String) {
        remote.toggleLike(postId)
    }

    override suspend fun repost(originalPostId: String): String {
        return remote.repost(originalPostId)
    }

    override suspend fun undoRepost(originalPostId: String) {
        remote.undoRepost(originalPostId)
    }
}

package com.example.pelitaapp.core.data.remote

import com.example.pelitaapp.core.data.model.Comment
import com.example.pelitaapp.core.data.model.Post
import com.example.pelitaapp.core.data.model.PostType
import com.example.pelitaapp.core.data.model.Profile
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

class PostRemoteDataSource(
    private val client: SupabaseClient = SupabaseClientProvider.get()
) {

    private fun requireUserId(): String =
        requireNotNull(client.auth.currentUserOrNull()?.id) { "User belum login." }

    /**
     * Buat post original.
     */
    suspend fun createPost(content: String): String {
        val userId = requireUserId()

        val dto = PostRowDto(
            id = null,
            userId = userId,
            content = content,
            postType = "ORIGINAL",
            repostOfPostId = null,
            createdAt = Instant.now().toString()
        )

        val inserted = client.from("posts")
            .insert(dto) { select() }
            .decodeSingle<PostRowDto>()

        return requireNotNull(inserted.id)
    }

    /**
     * Repost: buat row posts type REPOST yang menunjuk post asli.
     */
    suspend fun repost(originalPostId: String): String {
        val userId = requireUserId()

        val dto = PostRowDto(
            id = null,
            userId = userId,
            content = null,
            postType = "REPOST",
            repostOfPostId = originalPostId,
            createdAt = Instant.now().toString()
        )

        val inserted = client.from("posts")
            .insert(dto) { select() }
            .decodeSingle<PostRowDto>()

        return requireNotNull(inserted.id)
    }

    /**
     * Undo repost: hapus row repost milik user untuk originalPostId.
     */
    suspend fun undoRepost(originalPostId: String) {
        val userId = requireUserId()

        client.from("posts").delete {
            filter {
                eq("user_id", userId)
                eq("post_type", "REPOST")
                eq("repost_of_post_id", originalPostId)
            }
        }
    }

    /**
     * Ambil feed sederhana: ambil posts terbaru lalu ambil profiles & original post (untuk repost).
     * Ini aman walau belum bikin VIEW / join relasi PostgREST.
     */
    suspend fun getFeed(limit: Int = 20, offset: Int = 0): List<Post> {
        val rows = client.from("posts")
            .select {
                order("created_at", ascending = false)
                range(offset, offset + limit - 1)
            }
            .decodeList<PostRowDto>()

        if (rows.isEmpty()) return emptyList()

        // Ambil semua userId yang muncul
        val userIds = rows.map { it.userId }.distinct()

        // Ambil original post ids untuk repost
        val originalIds = rows.mapNotNull { it.repostOfPostId }.distinct()

        val profiles = fetchProfilesByIds(userIds)
        val originalPosts = if (originalIds.isNotEmpty()) fetchPostsByIds(originalIds) else emptyMap()

        return rows.mapNotNull { row ->
            val author = profiles[row.userId] ?: return@mapNotNull null
            row.toDomain(
                author = author,
                originalPost = row.repostOfPostId?.let { originalPosts[it] }
            )
        }
    }

    /**
     * Tambah komentar.
     */
    suspend fun addComment(postId: String, content: String): String {
        val userId = requireUserId()

        val dto = CommentRowDto(
            id = null,
            postId = postId,
            userId = userId,
            content = content,
            createdAt = Instant.now().toString()
        )

        val inserted = client.from("comments")
            .insert(dto) { select() }
            .decodeSingle<CommentRowDto>()

        return requireNotNull(inserted.id)
    }

    /**
     * Ambil komentar untuk sebuah post.
     */
    suspend fun getComments(postId: String, limit: Int = 50): List<Comment> {
        val rows = client.from("comments")
            .select {
                filter { eq("post_id", postId) }
                order("created_at", ascending = true)
                limit(limit)
            }
            .decodeList<CommentRowDto>()

        if (rows.isEmpty()) return emptyList()

        val userIds = rows.map { it.userId }.distinct()
        val profiles = fetchProfilesByIds(userIds)

        return rows.mapNotNull { row ->
            val author = profiles[row.userId] ?: return@mapNotNull null
            row.toDomain(author)
        }
    }

    /**
     * Toggle like:
     * - Kalau sudah like => unlike (delete row)
     * - Kalau belum => like (insert row)
     */
    suspend fun toggleLike(postId: String) {
        val userId = requireUserId()

        val existing = client.from("likes")
            .select {
                filter {
                    eq("user_id", userId)
                    eq("post_id", postId)
                }
                limit(1)
            }
            .decodeList<LikeRowDto>()
            .firstOrNull()

        if (existing != null) {
            client.from("likes").delete {
                filter {
                    eq("user_id", userId)
                    eq("post_id", postId)
                }
            }
        } else {
            client.from("likes").insert(
                LikeRowDto(userId = userId, postId = postId, createdAt = Instant.now().toString())
            )
        }
    }

    // ============================
    // Helpers: fetch profiles/posts
    // ============================

    private suspend fun fetchProfilesByIds(ids: List<String>): Map<String, Profile> {
        if (ids.isEmpty()) return emptyMap()

        val rows = client.from("profiles")
            .select {
                filter { `in`("id", ids) }
            }
            .decodeList<AuthRemoteDataSource.ProfileRowDto>()

        return rows.associate { it.id to it.toDomain() }
    }

    private suspend fun fetchPostsByIds(ids: List<String>): Map<String, Post> {
        if (ids.isEmpty()) return emptyMap()

        val rows = client.from("posts")
            .select {
                filter { `in`("id", ids) }
            }
            .decodeList<PostRowDto>()

        if (rows.isEmpty()) return emptyMap()

        val userIds = rows.map { it.userId }.distinct()
        val profiles = fetchProfilesByIds(userIds)

        return rows.mapNotNull { row ->
            val author = profiles[row.userId] ?: return@mapNotNull null
            val post = row.toDomain(author = author, originalPost = null)
            row.id?.let { it to post }
        }.toMap()
    }

    // ============================
    // DTO Supabase table mapping
    // ============================

    @Serializable
    data class PostRowDto(
        val id: String? = null,
        @SerialName("user_id") val userId: String,
        val content: String? = null,
        @SerialName("post_type") val postType: String, // "ORIGINAL" | "REPOST" | "QUOTE"
        @SerialName("repost_of_post_id") val repostOfPostId: String? = null,
        @SerialName("created_at") val createdAt: String
    ) {
        fun toDomain(author: Profile, originalPost: Post?): Post {
            val type = when (postType.uppercase()) {
                "REPOST" -> PostType.REPOST
                "QUOTE" -> PostType.QUOTE
                else -> PostType.ORIGINAL
            }

            return Post(
                id = requireNotNull(id),
                author = author,
                content = content,
                postType = type,
                repostOfPostId = repostOfPostId,
                originalPost = originalPost,
                // stats & flags nanti diisi setelah ada query count/view
                likeCount = 0,
                commentCount = 0,
                repostCount = 0,
                isLikedByMe = false,
                isRepostedByMe = false,
                createdAt = Instant.parse(createdAt)
            )
        }
    }

    @Serializable
    data class CommentRowDto(
        val id: String? = null,
        @SerialName("post_id") val postId: String,
        @SerialName("user_id") val userId: String,
        val content: String,
        @SerialName("created_at") val createdAt: String
    ) {
        fun toDomain(author: Profile): Comment {
            return Comment(
                id = requireNotNull(id),
                postId = postId,
                author = author,
                content = content,
                createdAt = Instant.parse(createdAt)
            )
        }
    }

    @Serializable
    data class LikeRowDto(
        @SerialName("user_id") val userId: String,
        @SerialName("post_id") val postId: String,
        @SerialName("created_at") val createdAt: String
    )
}

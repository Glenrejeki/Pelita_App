package com.example.pelitaapp.feature.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pelitaapp.core.data.model.Comment
import com.example.pelitaapp.core.data.model.Post
import com.example.pelitaapp.core.data.repository.PostRepository
import com.example.pelitaapp.core.util.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostUiState())
    val uiState: StateFlow<PostUiState> = _uiState

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    // ========= Create Post =========
    fun onContentChange(value: String) {
        _uiState.update { it.copy(content = value, errorMessage = null) }
    }

    fun submitPost(onSuccess: (newPostId: String) -> Unit) {
        val content = _uiState.value.content
        if (!Validators.isPostContentValid(content)) {
            _uiState.update { it.copy(errorMessage = "Postingan tidak boleh kosong.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isPosting = true, errorMessage = null, successMessage = null) }
            try {
                val newId = postRepository.createPost(content.trim())
                _uiState.update { it.copy(isPosting = false, successMessage = "Postingan berhasil dibuat!") }
                onSuccess(newId)
            } catch (e: Exception) {
                _uiState.update { it.copy(isPosting = false, errorMessage = e.message ?: "Gagal membuat postingan.") }
            }
        }
    }

    // ========= Detail =========

    /**
     * MVP: kita set post dari luar (misal dari Feed), lalu load komentarnya.
     * Kalau kamu belum punya post object, pass null pun boleh.
     */
    fun setPost(post: Post?) {
        _uiState.update { it.copy(post = post) }
    }

    fun loadComments(postId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val comments = postRepository.getComments(postId = postId, limit = 100)
                _uiState.update { it.copy(isLoading = false, comments = comments) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Gagal memuat komentar.") }
            }
        }
    }

    fun onCommentInputChange(value: String) {
        _uiState.update { it.copy(commentInput = value, errorMessage = null) }
    }

    fun submitComment(postId: String) {
        val text = _uiState.value.commentInput.trim()
        if (text.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Komentar tidak boleh kosong.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingComment = true, errorMessage = null) }
            try {
                postRepository.addComment(postId, text)
                _uiState.update { it.copy(isSubmittingComment = false, commentInput = "", successMessage = "Komentar terkirim.") }
                // reload comments biar masuk
                val comments = postRepository.getComments(postId = postId, limit = 100)
                _uiState.update { it.copy(comments = comments) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSubmittingComment = false, errorMessage = e.message ?: "Gagal mengirim komentar.") }
            }
        }
    }

    fun toggleLike(post: Post) {
        viewModelScope.launch {
            try {
                postRepository.toggleLike(post.id)
                // MVP: reload komentar saja tidak cukup; nanti akan reload post detail via endpoint
                _uiState.update { it.copy(successMessage = "Like diperbarui.") }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Gagal like.") }
            }
        }
    }

    fun toggleRepost(post: Post) {
        viewModelScope.launch {
            try {
                if (post.isRepostedByMe) {
                    postRepository.undoRepost(post.id)
                } else {
                    postRepository.repost(post.id)
                }
                _uiState.update { it.copy(successMessage = "Repost diperbarui.") }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Gagal repost.") }
            }
        }
    }
}

package com.example.pelitaapp.core.data.repository

import com.example.pelitaapp.core.data.model.Profile

interface AuthRepository {
    suspend fun signUp(
        email: String,
        password: String,
        username: String,
        fullName: String? = null
    ): Profile

    suspend fun signIn(email: String, password: String)

    suspend fun signOut()

    fun getCurrentUserId(): String?

    suspend fun getMyProfile(): Profile?

    suspend fun updateMyProfile(
        username: String? = null,
        fullName: String? = null,
        bio: String? = null,
        avatarUrl: String? = null
    )
}
